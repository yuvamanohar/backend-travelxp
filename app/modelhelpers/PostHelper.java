package modelhelpers;

import com.google.inject.Inject;
import models.Post;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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
}
