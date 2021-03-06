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
                query = "select sp from SocialProfile sp where sp.socialNetwork = :socialNetwork and sp.socialNetworkId = :socialNetworkId and softDeleted = false")
})

@Entity
@Table(name="socialProfiles")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="socialProfileId")
public class SocialProfile extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long socialProfileId ;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user ;

    public String socialNetwork ;
    public String socialNetworkId ;
    public String firstName ;
    public String middleName ;
    public String lastName ;
    public String completeName ;
    public String profilePic ;

    public User getUser() {
        return user ;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SocialProfile() {}

    public SocialProfile(String socialNetwork, String socialNetworkId,
                         String firstName, String middleName, String lastName, String completeName,
                         String profilePic, boolean softDeleted) {
        this.socialNetwork = socialNetwork ;
        this.socialNetworkId = socialNetworkId ;
        this.firstName = firstName ;
        this.middleName = middleName ;
        this.lastName = lastName ;
        this.completeName = completeName ;
        this.profilePic = profilePic ;
        this.softDeleted = softDeleted ;
    }

    public SocialProfile copy() {
        return new SocialProfile(this.socialNetwork, this.socialNetworkId,
                                this.firstName, this.middleName, this.lastName, this.completeName,
                                this.profilePic, this.softDeleted) ;
    }
}

