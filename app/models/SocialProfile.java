package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

/**
 * Created by yuva on 17/4/17.
 */

@NamedQueries({
        @NamedQuery(
                name = "sp_get_by_network_and_network_id",
                query = "select sp from SocialProfile sp where sp.socialNetwork = :socialNetwork and sp.socialNetworkId = :socialNetworkId and softDeleted = :softDeleted"),
        @NamedQuery(
                name = "sp_get_by_id",
                query = "select sp from SocialProfile sp where sp.socialProfileId = :socialProfileId and softDeleted = :softDeleted"),
        @NamedQuery(
                name = "sp_soft_delete",
                query = "update SocialProfile sp set sp.softDeleted = :softDeleted where sp.socialProfileId = :socialProfileId")
})

@Entity
@Table(name="socialProfiles")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="socialProfileId")
public class SocialProfile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long socialProfileId ;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonIgnore
    public User user ;

    public String socialNetwork ;
    public String socialNetworkId ;
    public String firstName ;
    public String middleName ;
    public String lastName ;
    public String completeName ;
    public String profilePic ;
    public String createdAt ;
    public String updatedAt ;

    @JsonIgnore
    public Boolean softDeleted ;

    public User getUser() {
        return user ;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SocialProfile() {}

    public SocialProfile(String socialNetwork, String socialNetworkId,
                         String firstName, String middleName, String lastName, String completeName,
                         String profilePic, String createdAt, String updatedAt, boolean softDeleted) {
        this.socialNetwork = socialNetwork ;
        this.socialNetworkId = socialNetworkId ;
        this.firstName = firstName ;
        this.middleName = middleName ;
        this.lastName = lastName ;
        this.completeName = completeName ;
        this.profilePic = profilePic ;
        this.createdAt = createdAt ;
        this.updatedAt = updatedAt ;
        this.softDeleted = softDeleted ;
    }
}

