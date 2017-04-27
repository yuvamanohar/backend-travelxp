package controllers;

import modelhelpers.IPost;
import modelhelpers.IUser;
import models.PartialFeed;
import models.PartialFeed.FeedType;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 27/4/17.
 */
public class FeedController extends BaseController {
    public static String MOST_RECENT_POST_TIME = "mostRecentPostTime" ;
    public static String LEAST_RECENT_POST_TIME = "leastRecentPostTime" ;
    public static Integer FEED_BATCH_COUNT = 20 ;

    private final IUser iUser ;
    private final IPost iPost ;

    @Inject
    public FeedController(IPost post, IUser user) {
        this.iUser = user;
        this.iPost = post ;
    }


    @Transactional
    public CompletionStage<Result> getOlderFeed(Long userId) {
        Map<String, String> allParams = getAllParams(request()) ;
//        String mostRecentPostTime = allParams.get(MOST_RECENT_POST_TIME) ;
        String leastRecentPostTime = allParams.get(LEAST_RECENT_POST_TIME) ;

        return iPost.getPostsOlderThanAsync(leastRecentPostTime, FEED_BATCH_COUNT)
                    .thenApplyAsync(p -> ok(Json.toJson(new PartialFeed(p, FeedType.OLDER_FEED)))) ;
    }

    @Transactional
    public CompletionStage<Result> getUpdatedFeed(Long userId) {
        Map<String, String> allParams = getAllParams(request()) ;
        String mostRecentPostTime = allParams.get(MOST_RECENT_POST_TIME) ;
//        String leastRecentPostTime = allParams.get(LEAST_RECENT_POST_TIME) ;

        return iPost.getPostsNewerThanAsync(mostRecentPostTime, FEED_BATCH_COUNT)
                    .thenApplyAsync(p -> ok(Json.toJson(new PartialFeed(p, FeedType.REFRESH_FEED)))) ;
    }

}