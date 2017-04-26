package modelhelpers;

import com.google.inject.ImplementedBy;
import modelhelpers.ISocialProfile;
import modelhelpers.UserHelper;
import models.SocialProfile;
import models.User;

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

    public User addUserAndSocialProfile(User user, SocialProfile socialProfile, ISocialProfile iSocialProfile) ;
    public CompletionStage<User> addUserAndSocialProfileAsync(User user, SocialProfile socialProfile, ISocialProfile iSocialProfile) ;

    public User get(Long userId) ;
    public CompletionStage<User> getAsync(Long userId) ;

    public User merge(User user) ;
    public CompletionStage<User> mergeAsync(User user) ;
}
