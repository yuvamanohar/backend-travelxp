package controllers;

import cdn.CdnRequest;
import cdn.ICdn;
import modelhelpers.IPost;
import modelhelpers.IUser;
import models.Post;
import models.PostDetail;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import utils.Config;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

/**
 * Created by yuva on 20/4/17.
 */
public class PostController extends BaseController {
    public static final String DESC_KEY = "description" ;

    private final IUser iUser ;
    private final IPost iPost ;
    private final Executor ec;
    private final ICdn iCdn;

    @Inject
    public PostController(IPost post, IUser user, HttpExecutionContext ec, ICdn iCdn) {
        this.iUser = user;
        this.iPost = post ;
        this.ec = ec.current();
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

            CompletionStage<Post> postCompletionStage = iUser.getAsync(userId).thenApplyAsync(u -> post.setUser(u)) ;
            return postCompletionStage.thenComposeAsync(
                    p -> iPost.insertPostAndPostDetailsAsync(p)).thenApplyAsync(post1 -> ok(Json.toJson(post1)));
        }) ;
    }

}
