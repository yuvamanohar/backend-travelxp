package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * Created by yuva on 17/4/17.
 */

@NamedQueries({
        @NamedQuery(
                name = "post_get_by_id",
                query = "select p from Post p where p.postId = :postId and softDeleted = :softDeleted"),
        @NamedQuery(
                name = "post_soft_delete",
                query = "update Post p set p.softDeleted = :softDeleted where p.postId = :postId")
})

@Entity
@Table(name="posts")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="postId")
public class Post extends BaseModel {

    public enum MediaType {
        GIF,
        PHOTO,
        VIDEO ;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long postId ;

    @ManyToOne
    @JoinColumn(name = "userId",
            foreignKey = @ForeignKey(name = "fk_post_user"))
    private User user ;

    public String media ;
    @Enumerated(EnumType.STRING)
    public MediaType mediaType ;
    public String scribble ;
    public Double latitude ;
    public Double longitude ;
    public Integer likes ;
    public Integer comments ;
    public Integer shares ;


    public User getUser() {
        return user ;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post() {}

}

