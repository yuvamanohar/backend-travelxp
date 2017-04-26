package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by yuva on 25/4/17.
 */

@Entity
@Table(name="postDetails")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="postDetailId")
public class PostDetail extends BaseModel {

    public enum MediaType {
        GIF,
        PHOTO,
        VIDEO ;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long postDetailId ;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "postId")
    private Post post ;

    public String media ;

    @Enumerated(EnumType.STRING)
    public MediaType mediaType ;

    public PostDetail() {}

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        PostDetail postDetail = (PostDetail) o;
        return Objects.equals( media, postDetail.media ) && Objects.equals(mediaType, postDetail.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(media, mediaType);
    }
}
