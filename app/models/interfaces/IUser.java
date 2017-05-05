package models.interfaces;

import com.google.inject.ImplementedBy;
import models.helpers.UserHelper;
import models.User;

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
    public CompletionStage<User> updatePlatformandDeviceIdAsync(Long userId, String platform, String deviceId) ;

    public CompletionStage<User> addUserAndSocialProfileAsync(User user) ;

    public CompletionStage<User> getAsync(Long userId) ;

    public CompletionStage<User> mergeAsync(User user) ;

    public CompletionStage<List<User>> getSearchUsersAsync(String name) ;
}
