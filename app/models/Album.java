package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuva on 2/5/17.
 */

@NamedQueries({
        @NamedQuery(
                name = "get_albums_by_user",
                query = "select a from Album a where a.user = :user and a.softDeleted = false")
})

@Entity
@Table(name="albums")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="albumId")
public class Album extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long albumId ;

    public String name ;
    public String description ;

    @ManyToOne
    @JoinColumn(name = "userId",
            foreignKey = @ForeignKey(name = "fk_album_user"))
    @JsonIgnore
    private User user ;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<Post> posts ;

    public List<Post> getPosts() {
        return posts ;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts ;
    }

    public Album addPost(Post post) {
        if(posts == null) {
            posts = new ArrayList<>() ;
        }
        if(!posts.contains(post))
            posts.add( post );
        post.setAlbum( this );

        if(this.name == null) {
            this.name = post.location ;
        }

        return this ;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.userId ;
    }
}
