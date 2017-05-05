package controllers;

import models.interfaces.IAlbum;
import models.interfaces.IPost;
import models.interfaces.IUser;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 5/5/17.
 */
public class AlbumController extends BaseController {

    private final IAlbum iAlbum ;
    private final IUser iUser ;
    private final IPost iPost ;

    @Inject
    public AlbumController(IAlbum album, IPost post, IUser user) {
        this.iAlbum = album ;
        this.iUser = user;
        this.iPost = post ;
    }

    @Transactional
    public CompletionStage<Result> getPosts(Long album) {
        return iAlbum.getAsync(album).thenApplyAsync(a -> ok(Json.toJson(a))) ;
    }
}
