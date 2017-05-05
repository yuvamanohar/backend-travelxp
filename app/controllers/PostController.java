package controllers;

import cdn.CdnRequest;
import cdn.ICdn;
import models.interfaces.IAlbum;
import models.interfaces.IPost;
import models.interfaces.IUser;
import models.Album;
import models.Post;
import models.PostDetail;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.Config;
import utils.Location;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 20/4/17.
 */
public class PostController extends BaseController {
    public static final String DESC_KEY = "description" ;

    private final IAlbum iAlbum ;
    private final IUser iUser ;
    private final IPost iPost ;
    private final ICdn iCdn;

    @Inject
    public PostController(IAlbum album, IPost post, IUser user, ICdn iCdn) {
        this.iAlbum = album ;
        this.iUser = user;
        this.iPost = post ;
        this.iCdn = iCdn ;
    }

    @Transactional
    public CompletionStage<Result> postContent(Long userId) {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Post post = Json.fromJson(Json.parse(body.asFormUrlEncoded().get(DESC_KEY)[0]), Post.class);

        List<PostDetail> postDetails = post.getPostDetails() ;
        List<CdnRequest> cdnRequests = new ArrayList<>() ;

        for(PostDetail postDetail : postDetails) {
            Http.MultipartFormData.FilePart<File> part = body.getFile(postDetail.media) ;
            String fileName = part.getFilename();
            File file = part.getFile();
            String cdnRelativePath = userId + "/" + fileName ;
            cdnRequests.add(CdnRequest.get(file, cdnRelativePath, cdnRelativePath));
            postDetail.media = cdnRelativePath ; // Update media path
            post.addPostDetail(postDetail);
        }

        return iCdn.pushToCdnAsyncBatch(cdnRequests).thenComposeAsync(cdnRequestList -> {

            for (CdnRequest request : cdnRequestList) {
                if (!request.getStatus()) {
                    PostDetail pd = post.postDetails.stream().filter(
                            postDetail -> postDetail.media.equals(request.key)).findFirst().orElse(null);
                    if (pd != null)
                        post.removePostDetail(pd);
                }
            }

            return  iUser.getAsync(userId).thenComposeAsync(u ->
                    {
                        post.setUser(u) ;
                        CompletionStage<Album> albumCompletionStage = getOrCreateAlbumForPost(post) ;
                        if(albumCompletionStage != null) {
                            return albumCompletionStage.thenComposeAsync( a ->
                            {
                                if(a.albumId == null) {
                                    a.setUser(u);
                                    return iAlbum.insertAsync(a).thenApplyAsync(a1 -> ok(Json.toJson(post))) ;
                                } else {
                                    return iAlbum.mergeAsync(a).thenApplyAsync(a1 -> ok(Json.toJson(post))) ;
                                }
                            }) ;
                        } else {
                            return iPost.insertPostAndPostDetailsAsync(post).thenApplyAsync(post1 -> ok(Json.toJson(post1)));
                        }
                    }) ;

        }) ;
    }

    private CompletionStage<Album> getOrCreateAlbumForPost(Post newPost) {
        // No album for null locations
        if(newPost.latitude == null || newPost.longitude == null) {
            return null ;
        }

        CompletionStage<List<Post>> postInLastXDays = iPost.getPostsInLastXDaysAsync(Config.ALBUM_TIME_SPAN_THRESHOLD) ;
        Location newPostLocation = Location.get(newPost.latitude, newPost.longitude) ;

        return postInLastXDays.thenApplyAsync(i -> {
            double leastDistance = Double.MAX_VALUE ;
            Post nearestPost = null ;
            for(Post oldPost : i) {
                if(oldPost.getGeoLocation() != null) {
                    if(newPostLocation.isInSameVicinityAs(oldPost.getGeoLocation())) {
                        if(newPostLocation.getDistanceTo(oldPost.getGeoLocation()) < leastDistance) {
                            leastDistance = newPostLocation.getDistanceTo(oldPost.getGeoLocation());
                            nearestPost = oldPost;
                        }
                    }

                }
            }

            return  nearestPost ;
        }).thenApplyAsync(nearestPost -> {
            if(nearestPost != null) {
                return nearestPost.getAlbum().addPost(newPost) ;
            } else {
                return new Album().addPost(newPost);
            }
        }) ;
    }

    @Transactional
    public CompletionStage<Result> getPost(Long postId) {
        return iPost.getAsync(postId).thenApplyAsync(p -> ok(Json.toJson(p))) ;
    }
}
