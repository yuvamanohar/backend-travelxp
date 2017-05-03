package modelhelpers;

import com.google.inject.ImplementedBy;
import modelhelpers.ISocialProfile;
import modelhelpers.UserHelper;
import models.SocialProfile;
import models.User;

import javax.persistence.PersistenceException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 18/4/17.
 */
@ImplementedBy(UserHelper.class)
public interface IUser {

    /**
     * Assumes that user already exists
     * @param userId
     * @param platform
     * @param deviceId
     * @return
     */
    public User updatePlatformandDeviceId(Long userId, String platform, String deviceId) ;
    public CompletionStage<User> updatePlatformandDeviceIdAsync(Long userId, String platform, String deviceId) ;

    public User addUserAndSocialProfile(User user) ;
    public CompletionStage<User> addUserAndSocialProfileAsync(User user) ;

    public User get(Long userId) ;
    public CompletionStage<User> getAsync(Long userId) ;

    public User merge(User user) ;
    public CompletionStage<User> mergeAsync(User user) ;

    public CompletionStage<List<User>> getSearchUsersAsync(String name) ;
}
