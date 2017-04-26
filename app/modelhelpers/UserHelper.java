package modelhelpers;

import com.google.inject.Inject;
import models.SocialProfile;
import models.User;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
        super(jpaApi, executionContext);
    }

    @Override
    public User get(Long userId) {
        TypedQuery<User> typedQuery = jpaApi.em().createNamedQuery("user_get_by_id", User.class)
                .setParameter("userId", userId)
                .setParameter("softDeleted", false) ;
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return  null ;
        }
    }

    @Override
    public User updatePlatformandDeviceId(Long userId, String platform, String deviceId) {
        TypedQuery<User> typedQuery = jpaApi.em().createNamedQuery("user_update_platform_and_device_id", User.class) ;
        return typedQuery.getSingleResult() ;
    }

    @Override
    public CompletionStage<User> updatePlatformandDeviceIdAsync(Long userId, String platform, String deviceId) {
        return supplyAsync(() -> wrapInTransaction(em -> updatePlatformandDeviceId(userId, platform, deviceId)), executionContext) ;
    }

    @Override
    public User addUserAndSocialProfile(User user, SocialProfile socialProfile, ISocialProfile iSocialProfile) {
        this.insert(user) ;
        return user ;
    }

    @Override
    public CompletionStage<User> addUserAndSocialProfileAsync(User user, SocialProfile socialProfile, ISocialProfile iSocialProfile) {
        return supplyAsync(() -> wrapInTransaction(entityManager -> addUserAndSocialProfile(user, socialProfile, iSocialProfile)), executionContext);
    }
}
