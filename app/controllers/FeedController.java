package controllers;

import models.interfaces.IPost;
import models.interfaces.IUser;
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
    public static String REFERENCE_TIME = "referenceTime" ;
    public static String FEED_BATCH_SIZE = "feedBatchSize" ;
    public static String OFFSET = "offset" ; // Offset from first reference_post

    public static Integer DEFAULT_FEED_BATCH_SIZE = 20 ;
    public static Integer DEFAULT_OFFSET = 0 ;

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

        String referencePostTime = allParams.get(REFERENCE_TIME) ;
        Integer batchCount = allParams.get(FEED_BATCH_SIZE) != null ? Integer.parseInt(allParams.get(FEED_BATCH_SIZE)) : DEFAULT_FEED_BATCH_SIZE;
        Integer offset = allParams.get(OFFSET) != null ? Integer.parseInt(allParams.get(OFFSET)) : DEFAULT_OFFSET ;

        return iPost.getPostsOlderThanAsync(referencePostTime, offset, batchCount +1)
                    .thenApplyAsync(p -> {
                        if(p.size() <= DEFAULT_FEED_BATCH_SIZE) {
                            return ok(Json.toJson(new PartialFeed(p, FeedType.OLDER_FEED,
                                        offset + p.size(), true))) ;
                        } else {
                            return ok(Json.toJson(new PartialFeed(p.subList(0, p.size()-1), FeedType.OLDER_FEED,
                                            offset + batchCount, false))) ;
                        }
                    }) ;

    }

    @Transactional
    public CompletionStage<Result> getUpdatedFeed(Long userId) {
        Map<String, String> allParams = getAllParams(request()) ;
        String mostRecentPostTime = allParams.get(MOST_RECENT_POST_TIME) ;
//        String referenceTime = allParams.get(LEAST_RECENT_POST_TIME) ;

        return iPost.getPostsNewerThanAsync(mostRecentPostTime, DEFAULT_FEED_BATCH_SIZE)
                    .thenApplyAsync(p -> ok(Json.toJson(new PartialFeed(p, FeedType.UPDATED_FEED,
                                                        -1, false)))) ;
    }

}