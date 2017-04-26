package modelhelpers;

import com.google.inject.Inject;
import models.Post;
import models.PostDetail;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.CompletionStage;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by yuva on 20/4/17.
 */
public class PostHelper extends BaseModelHelper<Post, Long> implements IPost {

    @Inject
    public PostHelper(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext);
    }

    @Override
    public Post get(Long socialProfileId) {
        TypedQuery<Post> typedQuery = jpaApi.em().createNamedQuery("post_get_by_id", Post.class)
                .setParameter("postId", socialProfileId)
                .setParameter("softDeleted", true) ;
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return  null ;
        }
    }

    @Override
    public Post insertPostAndPostDetails(Post post) {
        post = this.insert(post) ;
//        post.postDetails.forEach(i -> jpaApi.em().persist(i));
        return post ;
    }

    @Override
    public CompletionStage<Post> insertPostAndPostDetailsAsync(Post post) {
        return supplyAsync(() -> wrapInTransaction(em -> insertPostAndPostDetails(post)), executionContext);
    }
}
