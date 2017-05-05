package models.helpers;

import com.google.inject.Inject;
import models.interfaces.IAlbum;
import models.Album;
import models.Post;
import models.User;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by yuva on 2/5/17.
 */
public class AlbumHelper extends BaseModelHelper<Album, Long> implements IAlbum {

    @Inject
    public AlbumHelper(JPAApi jpaApi, DatabaseExecutionContext dbExecutionContext) {
        super(jpaApi, dbExecutionContext, Album.class);
    }

    public List<Album> getAll(User user) {
        TypedQuery<Album> typedQuery = jpaApi.em().createNamedQuery("get_albums_by_user", Album.class)
                .setParameter("user", user) ;

        return typedQuery.getResultList() ;
    }

    @Override
    public CompletionStage<List<Album>> getAllAsync(User user) {
        return supplyAsync(() -> wrapInTransaction(em -> getAll(user)), dbExecutionContext) ;
    }

    public List<Post> getAllPosts(Album album) {
        TypedQuery<Post> typedQuery = jpaApi.em().createNamedQuery("get_posts_by_album", Post.class)
                                                 .setParameter("album", album) ;
        return typedQuery.getResultList() ;
    }

    @Override
    public CompletionStage<List<Post>> getAllPostsAsync(Album album) {
        return supplyAsync(() -> wrapInTransaction(em -> getAllPosts(album)), dbExecutionContext);
    }
}
