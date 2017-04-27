package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuva on 27/4/17.
 */
public class PartialFeed {
    public enum FeedType {
        REFRESH_FEED,
        OLDER_FEED ;
    }

    public final List<Post> posts ;
    public Set<User> users ;
    public String mostRecentPostTime ;
    public String leastRecentPostTime ;
    public final FeedType feedType ;

    // When refreshing posts, it may so happen that we cannot send all the updated posts at one shot
    // In such case this time be leastRecentPostTime of updated posts
    // TODO in later version it gets tricky if the user keeps refreshing and keep checking partial updates
    // TODO Client has to handle such case..possibly by clearing up the whole list and re-populate
//    public String truncatedMostRecentPostTime ;
//    public boolean isEndOfFeed ;

    public PartialFeed(List<Post> posts, FeedType feedType) {
        this.posts = posts ;

        if(posts.size() > 0) {
            users = new HashSet<>() ;
            for(Post post : posts) {
                users.add(post.getUser()) ;
            }

            this.mostRecentPostTime = posts.get(0).getUpdatedAt();
            this.leastRecentPostTime = posts.get(posts.size() - 1).getUpdatedAt();
        }
        this.feedType = feedType ;
    }
}
