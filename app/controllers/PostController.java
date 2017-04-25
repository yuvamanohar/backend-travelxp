package controllers;

import modelhelpers.IPost;
import modelhelpers.IUser;
import models.Post;
import models.User;
import org.apache.commons.io.FileUtils;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import utils.Config;
import utils.DateFormatter;
import utils.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import static java.util.concurrent.CompletableFuture.supplyAsync ;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 20/4/17.
 */
public class PostController extends BaseController {
    public static final String PHOTO_KEY = "photo" ;
    public static final String DESC_KEY = "description" ;

    private final IUser iUser ;
    private final IPost iPost ;
    private final HttpExecutionContext ec;

    @Inject
    public PostController(IPost post, IUser user, HttpExecutionContext ec) {
        this.iUser = user;
        this.iPost = post ;
        this.ec = ec;
    }

    @Transactional
    public CompletionStage<Result> postContent(Long userId) {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData() ;
        Http.MultipartFormData.FilePart<File> picture = body.getFile(PHOTO_KEY);
        Post post = Json.fromJson(Json.parse(body.asFormUrlEncoded().get(DESC_KEY)[0]) , Post.class) ;

        if (picture != null) {
            String fileName = picture.getFilename();
            File file = picture.getFile();
            String cdnFilePath = Config.CDN_LOCATION + userId + "/" + fileName ;
            post.media = cdnFilePath ;
            post.mediaType = Post.MediaType.valueOf(StringUtils.toUpperCase(picture.getContentType())) ;
            try {
                FileUtils.moveFile(file, new File(cdnFilePath)) ;
            } catch (IOException ioe) {
                ioe.printStackTrace() ;
                return supplyAsync(() -> badRequest()) ;
            }
            return iPost.insertAsync(post).thenComposeAsync(p -> supplyAsync(() -> ok(Json.toJson(post)))) ;
        }

        return supplyAsync(() -> badRequest()) ;
    }


}
