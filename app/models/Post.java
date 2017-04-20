package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;

/**
 * Created by yuva on 17/4/17.
 */

@NamedQueries({
        @NamedQuery(
                name = "post_get_by_id",
                query = "select p from Post p where p.scribId = :scribId and softDeleted = :softDeleted"),
        @NamedQuery(
                name = "post_soft_delete",
                query = "update Post p set p.softDeleted = :softDeleted where sb.postId = :postId")
})

@Entity
@Table(name="posts")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="postId")
public class Post {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long postId ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonIgnore
    public User user ;

    public String photo ;
    public String scribble ;
    public List<String> tags ;
    public String location ; // Spatial data type needs to be used
    public String createdAt ; // This should be optimized for queries on day basis, recent etc
    public String updatedAt ;

    @JsonIgnore
    public Boolean softDeleted ;

    public User getUser() {
        return user ;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post() {}

}

