package models.helpers;

import com.google.inject.Inject;
import models.interfaces.IPost;
import models.Post;
import models.User;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;
import utils.DateFormatter;

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
        super(jpaApi, executionContext, Post.class);
    }

//    @Override
//    public Post get(Long socialProfileId) {
//        TypedQuery<Post> typedQuery = jpaApi.em().createNamedQuery("post_get_by_id", Post.class)
//                .setParameter("postId", socialProfileId)
//                .setParameter("softDeleted", false) ;
//        try {
//            return typedQuery.getSingleResult();
//        } catch (NoResultException e) {
//            return  null ;
//        }
//    }

    public Post insertPostAndPostDetails(Post post) {
        post = this.insert(post) ;
        return post ;
    }

    @Override
    public CompletionStage<Post> insertPostAndPostDetailsAsync(Post post) {
        return supplyAsync(() -> wrapInTransaction(em -> insertPostAndPostDetails(post)), dbExecutionContext);
    }

    public List<Post> getPostsOlderThan(String referenceTime, int offset, int count) {
        TypedQuery<Post> morePosts = jpaApi.em().createNamedQuery("get_older_posts", Post.class)
                                                .setParameter("referenceTime", referenceTime)
                                                .setFirstResult(offset)
                                                .setMaxResults(count) ;
        return morePosts.getResultList() ;
    }

    @Override
    public CompletionStage<List<Post>> getPostsOlderThanAsync(String referenceTime, int offset, int count) {
        return supplyAsync(() -> wrapInTransaction(em -> getPostsOlderThan(referenceTime, offset, count)), dbExecutionContext) ;
    }

    /**
     * TODO - Add support for count in next versions, for now the expectation is there will be very less updates
     * @param mostRecentPostTime
     * @param count
     * @return
     */
    public List<Post> getPostsNewerThan(String mostRecentPostTime, int count) {
        TypedQuery<Post> typedQuery = jpaApi.em().createNamedQuery("get_newer_posts", Post.class)
                .setParameter("mostRecentPostTime", mostRecentPostTime) ;

        return typedQuery.getResultList() ;
    }

    @Override
    public CompletionStage<List<Post>> getPostsNewerThanAsync(String mostRecentPostTime, int count) {
        return supplyAsync(() -> wrapInTransaction(em -> getPostsNewerThan(mostRecentPostTime, count)), dbExecutionContext);
    }

    public List<Post> getPostsInLastXDays(int x) {
        String time = DateFormatter.getTimeBeforeXdays(x) ;
        TypedQuery<Post> typedQuery = jpaApi.em().createNamedQuery("get_posts_in_last_x_days", Post.class)
                .setParameter("time", time) ;

        return typedQuery.getResultList() ;

    }

    @Override
    public CompletionStage<List<Post>> getPostsInLastXDaysAsync(int x) {
        return supplyAsync(() -> wrapInTransaction(em -> getPostsInLastXDays(x)), dbExecutionContext);
    }

    public List<Post> getAllOrphanedPosts(User user) {
        TypedQuery<Post> typedQuery = jpaApi.em().createNamedQuery("get_orphaned_posts_by_user", Post.class)
                                            .setParameter("user", user) ;
        return typedQuery.getResultList() ;
    }

    @Override
    public CompletionStage<List<Post>> getAllOrphanedPostsAsync(User user) {
        return supplyAsync(() -> wrapInTransaction(em -> getAllOrphanedPosts(user)), dbExecutionContext) ;
    }
}
