package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

/**
 * Created by yuva on 17/4/17.
 */

@NamedQueries({
        @NamedQuery(
                name = "post_get_by_id",
                query = "select p from Post p where p.postId = :postId and softDeleted = :softDeleted")
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
    private User user ;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PostDetail> postDetails ;

    public String scribble ;
    public Double latitude ;
    public Double longitude ;
    public Integer likes ;
    public Integer comments ;
    public Integer shares ;

    public Post() {}

    public User getUser() {
        return user ;
    }

    public Post setUser(User user) {
        this.user = user;
        return this ;
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
}

