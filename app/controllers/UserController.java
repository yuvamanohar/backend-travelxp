package controllers;

import models.interfaces.ISocialProfile;
import models.interfaces.IUser;
import models.*;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import results.UserData;
import utils.DateFormatter;
import views.html.index;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

/**
 * Created by yuva on 17/4/17.
 */
public class UserController extends BaseController {

    private final IUser iUser ;
    private final ISocialProfile iSocialProfile ;
    private final Executor ec;

    @Inject
    public UserController(IUser user, ISocialProfile socialProfile, HttpExecutionContext ec) {
        this.iUser = user;
        this.iSocialProfile = socialProfile ;
        this.ec = ec.current() ;
    }

    @Transactional
    public CompletionStage<Result> bootstrap() {
/*        Map<String, String> params = getAllParams(request()) ;

        String mobile = params.get("mobile") ;
        Long mobileNumber = mobile != null ? Long.parseLong(mobile) : null ;
        String email = params.get("email") ;
        String platform = params.get("platform") ;
        String deviceId = params.get("deviceId") ;
        String socialNetwork = params.get("socialNetwork") ;
        String socialNetworkId = params.get("socialNetworkId") ;
        String socialData = params.get("socialData") ;
        String firstName = params.get("firstName") ;
        String lastName = params.get("lastName") ;
        String completeName = params.get("completeName") ;
        String profilePic = params.get("profilePic") ;
*/
        User user = Json.fromJson(request().body().asJson(), User.class) ;
        SocialProfile sp = user.getSocialProfile() ;

        CompletionStage<SocialProfile> existingSocialProfile =
                iSocialProfile.getByNetworkAndNetworkIdAsync(sp.socialNetwork, sp.socialNetworkId) ;

        return existingSocialProfile.thenComposeAsync( dbSocialProfile -> {
            if(dbSocialProfile != null) {
                User dbUser = dbSocialProfile.getUser() ;
          //          if(dbUser.platform.equals(user.platform) && dbUser.deviceId.equals(user.deviceId)) {
                        return CompletableFuture.supplyAsync(()
                                -> ok(Json.toJson(new UserData(dbUser, BaseController.OLD_USER_OLD_DEVICE, DateFormatter.getReadableCurrentTime())))) ;
//                    } else {
//                        CompletionStage<User> updatedUser = getUpdatedUser(dbUser, user.platform, user.deviceId) ;
//                        return updatedUser.thenApplyAsync(j ->
//                                    ok(Json.toJson(new UserData(j, BaseController.OLD_USER_NEW_DEVICE))), ec.current()) ;
//                    }
            } else {
                User newUser = new User(user.mobile, sp.completeName, user.email, user.platform, user.deviceId, false) ;
                SocialProfile socialProfile = new SocialProfile(sp.socialNetwork, sp.socialNetworkId,
                                                        sp.firstName, sp.middleName, sp.lastName,
                                                        sp.completeName, sp.profilePic, false) ;
                newUser.addSocialProfile(socialProfile);

                CompletionStage<User> userCompletionStage = iUser.addUserAndSocialProfileAsync(newUser) ;
                return userCompletionStage.thenApplyAsync(u -> ok(Json.toJson(new UserData(u, BaseController.NEW_USER, DateFormatter.getReadableCurrentTime())))) ;
            }
        }) ;
    }

    @Transactional
    public Result deleteUser(Long userId) {
        return ok(index.render("Your new application is ready."));
    }

    private CompletionStage<User> getUpdatedUser(User user, String platform, String deviceId) {
        user.platform = platform ;
        user.deviceId = deviceId ;
        return iUser.mergeAsync(user) ;
    }

    @Transactional
    public CompletionStage<Result> getUser(Long userId) {
        return  iUser.getAsync(userId).thenApplyAsync(u -> ok(Json.toJson(u))) ;
    }

}
