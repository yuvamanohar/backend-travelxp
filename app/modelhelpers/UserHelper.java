package modelhelpers;

import com.google.inject.Inject;
import models.User;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by yuva on 18/4/17.
 */
public class UserHelper extends BaseModelHelper<User, Long> implements IUser {

    @Inject
    public UserHelper(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext, User.class);
    }

//    @Override
//    public User get(Long userId) {
//        TypedQuery<User> typedQuery = jpaApi.em().createNamedQuery("user_get_by_id", User.class)
//                .setParameter("userId", userId)
//                .setParameter("softDeleted", false) ;
//        try {
//            return typedQuery.getSingleResult();
//        } catch (NoResultException e) {
//            return  null ;
//        }
//    }

    @Override
    public User updatePlatformandDeviceId(Long userId, String platform, String deviceId) {
        TypedQuery<User> typedQuery = jpaApi.em().createNamedQuery("user_update_platform_and_device_id", User.class) ;
        return typedQuery.getSingleResult() ;
    }

    @Override
    public CompletionStage<User> updatePlatformandDeviceIdAsync(Long userId, String platform, String deviceId) {
        return supplyAsync(() -> wrapInTransaction(em -> updatePlatformandDeviceId(userId, platform, deviceId)), dbExecutionContext) ;
    }

    @Override
    public User addUserAndSocialProfile(User user) {
        this.insert(user) ;
        return user ;
    }

    @Override
    public CompletionStage<User> addUserAndSocialProfileAsync(User user) {
        return supplyAsync(() -> wrapInTransaction(entityManager -> addUserAndSocialProfile(user)), dbExecutionContext);
    }
}
