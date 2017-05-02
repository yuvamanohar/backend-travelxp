package modelhelpers;

import com.google.inject.Inject;
import models.Album;
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

    public List<Album> getAll(Long userId) {
        TypedQuery<Album> typedQuery = jpaApi.em().createNamedQuery("get_albums_by_user", Album.class)
                .setParameter("userId", userId)
                .setParameter("softDeleted", false) ;

        return typedQuery.getResultList() ;
    }

    @Override
    public CompletionStage<List<Album>> getAllAsync(Long userId) {
        return supplyAsync(() -> wrapInTransaction(em -> getAll(userId)), dbExecutionContext) ;
    }
}
