package controllers;

import modelhelpers.IAlbum;
import modelhelpers.IPost;
import modelhelpers.IUser;
import play.libs.Json;
import play.mvc.Result;
import results.ProfileData;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 2/5/17.
 */
public class ProfileController extends BaseController {

    private final IAlbum iAlbum ;
    private final IUser iUser ;
    private final IPost iPost ;

    @Inject
    public ProfileController(IAlbum album, IPost post, IUser user) {
        this.iAlbum = album ;
        this.iUser = user;
        this.iPost = post ;
    }

    public CompletionStage<Result> getProfileInfo(Long userId) {
        return iUser.getAsync(userId).thenComposeAsync(u -> iAlbum.getAllAsync(u).thenComposeAsync(a ->
                iPost.getAllOrphanedPostsAsync(u).thenApplyAsync(p -> ok(Json.toJson(new ProfileData(u, a, p)))))) ;
    }

    public CompletionStage<Result> searchUsers(String name) {
        return iUser.getSearchUsersAsync(name).thenApplyAsync(ul -> ok(Json.toJson(ul))) ;
    }
}
