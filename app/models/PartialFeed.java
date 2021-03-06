package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuva on 27/4/17.
 */
public class PartialFeed {
    public enum FeedType {
        UPDATED_FEED,
        OLDER_FEED ;
    }

    public final List<Post> posts ;
    public Set<User> users ;
    public String mostRecentPostTime ;
    public Integer offset ;
    public final FeedType feedType ;
    public final Boolean olderFeedEnd;

    // When refreshing posts, it may so happen that we cannot send all the updated posts at one shot
    // In such case this time be referenceTime of updated posts
    // TODO in later version it gets tricky if the user keeps refreshing and keep checking partial updates
    // TODO Client has to handle such case..possibly by clearing up the whole list and re-populate
//    public String truncatedMostRecentPostTime ;
//    public boolean isEndOfFeed ;

    public PartialFeed(List<Post> posts, FeedType feedType, int offset, boolean olderFeedEnd) {
        this.posts = posts ;
        this.olderFeedEnd = olderFeedEnd;
        this.offset = offset ;
        this.feedType = feedType ;

        if(posts.size() > 0) {
            users = new HashSet<>() ;
            for(Post post : posts) {
                users.add(post.getUser()) ;
            }

            this.mostRecentPostTime = posts.get(0).getUpdatedAt();
        }
    }
}
