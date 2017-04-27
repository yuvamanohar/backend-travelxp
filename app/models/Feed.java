package models;

import java.util.List;

/**
 * Created by yuva on 27/4/17.
 */
public class Feed {
    public List<Post> posts ;
    public String mostRecentPostTime ;
    public String leastRecentPostTime ;
    // When refreshing posts, it may so happen that we cannot send all the updated posts at one shot
    // In such case this time be leastRecentPostTime of updated posts
    // TODO it gets tricky if the user keeps refreshing and keep checking partial updates
    // TODO Client has to handle such case..possibly by clearing up the whole list and re-populate
    public String truncatedMostRecentPostTime ;
    public boolean isQueryComplete ;
}
