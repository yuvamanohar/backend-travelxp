package controllers;

import modelhelpers.IAlbum;
import modelhelpers.IFanRelation;
import modelhelpers.IPost;
import modelhelpers.IUser;
import models.FanRelation;
import models.User;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 3/5/17.
 */
public class FanRelationController extends BaseController {
    public static String FAN_USER_KEY = "fanUserId" ;
    public static String INFLUENCER_USER_KEY = "influencerUserId" ;

    private final IUser iUser ;
    private final IFanRelation iFanRelation ;

    @Inject
    public FanRelationController(IUser user, IFanRelation iFanRelation) {
        this.iUser = user;
        this.iFanRelation = iFanRelation ;
    }

    public CompletionStage<Result> addFanRelation() {
        Map<String, String> allParams =  getAllParams(request()) ;
        Long fanUserId = Long.parseLong(allParams.get(FAN_USER_KEY)) ;
        Long influencerUserId = Long.parseLong(allParams.get(INFLUENCER_USER_KEY)) ;

        CompletionStage<User> fanUserCompletionStage = iUser.getAsync(fanUserId) ;
        CompletionStage<User> influencerUserCompletionStage = iUser.getAsync(influencerUserId) ;

        return fanUserCompletionStage.thenCombineAsync(influencerUserCompletionStage,
                                (fanUser, influencerUser) -> iFanRelation.insertAsync(new FanRelation(fanUser, influencerUser)))
                                        .thenApplyAsync(fanRelation -> ok(Json.toJson(fanRelation))) ;
    }
}
