package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import utils.Config;
import utils.GeoUtils;
import utils.Location;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.List;
import java.util.Locale;

/**
 * Created by yuva on 17/4/17.
 */

@NamedQueries({
        @NamedQuery(
                name = "get_older_posts",
                query = "select p from Post p where p.updatedAt < :referenceTime and softDeleted = false order by p.updatedAt desc, p.postId desc"),
        @NamedQuery(
                name = "get_newer_posts",
                query = "select p from Post p where p.updatedAt >= :mostRecentPostTime and softDeleted = false order by p.updatedAt desc, p.postId desc"),
        @NamedQuery(
                name = "get_posts_in_last_x_days",
                query = "select p from Post p where p.createdAt >= :time and softDeleted = false order by p.updatedAt desc, p.postId desc"),
        @NamedQuery(
                name = "get_orphaned_posts_by_user",
                query = "select p from Post p where p.user = :user and p.album = NULL and softDeleted = false order by p.updatedAt desc, p.postId desc"),
        @NamedQuery(
                name = "get_posts_by_album",
                query = "select p from Post p where p.album = :album and p.softDeleted = false")

})

@Entity
@Table(name="posts")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="postId")
public class Post extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long postId ;

    @ManyToOne
    @JoinColumn(name = "userId",
            foreignKey = @ForeignKey(name = "fk_post_user"))
    @JsonIgnore
    private User user ;

    @ManyToOne
    @JoinColumn(name = "albumId",
            foreignKey = @ForeignKey(name = "fk_post_album"))
    @JsonIgnore
    private Album album ;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<PostDetail> postDetails ;

    public String scribble ;
    public Double latitude ;
    public Double longitude ;
    public String location ;
    public Integer likes ;
    public Integer comments ;
    public Integer shares ;

    @Transient
    private Location geoLocation ;

    public Post() {}

    public User getUser() {
        return user ;
    }

    public Post setUser(User user) {
        this.user = user;
        return this ;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<PostDetail> getPostDetails() {
        return postDetails;
    }

    public void setPostDetails(List<PostDetail> postDetails) {
        this.postDetails = postDetails;
    }

    public void addPostDetail(PostDetail postDetail) {
        if(!postDetails.contains(postDetail))
            postDetails.add( postDetail );
        postDetail.setPost( this );
    }

    public void removePostDetail(PostDetail postDetail) {
        postDetails.remove( postDetail );
        postDetail.setPost( null );
    }

    public Long getUserId() {
        return user.userId ;
    }

    public Long getAlbumId() {
        if(album != null)
            return album.albumId ;

        return null ;
    }

    @JsonIgnore(false)
    public String getCreatedAt() {
        return super.getCreatedAt() ;
    }

    @JsonIgnore(false)
    public String getUpdatedAt() {
        return super.getUpdatedAt() ;
    }

    public Location getGeoLocation() {
        if(geoLocation == null) {
            loadGeoLocation();
        }
        return geoLocation;
    }

    public void setGeoLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }

    @PostLoad
    public void loadGeoLocation() {
        if(this.latitude != null && this.longitude != null) {
            geoLocation = Location.get(this.latitude, this.longitude);
        }
    }
}

