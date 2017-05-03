package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.NaturalId;
import utils.StringUtils;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by yuva on 17/4/17.
 */
@NamedQueries({
        @NamedQuery(
                name = "user_update_platform_and_device_id",
                query = "update User u set u.platform = :platform, u.deviceId = :deviceId  where u.userId = :userId"),
        @NamedQuery(
                name = "user_search_like_name",
                query = "select u from User u where u.userName like :name and softDeleted = false")
})
@Entity
@Table(name="users")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="userId")
public class User extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long userId ;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private SocialProfile socialProfile ;

    @Column(unique = true)
    private String userName ;

    public String aboutMe ;
    public Long mobile ;
    public String email ;
    public String platform  ;
    public String deviceId ;

    @Transient
    public String userNameSeed;

    public User() {} ;

    public User(Long mobile, String userNameSeed, String email, String platform,
                String deviceId, Boolean softDeleted) {
        this.mobile = mobile ;
        this.userName = StringUtils.generateUserName(userNameSeed) ;
        this.userNameSeed = userNameSeed ;
        this.email = email ;
        this.platform = platform ;
        this.deviceId = deviceId ;
        this.softDeleted = softDeleted ;
    }

    public SocialProfile getSocialProfile() {
        return socialProfile ;
    }

    public void addSocialProfile(SocialProfile socialProfile) {
        socialProfile.setUser(this);
        this.socialProfile = socialProfile;
    }

    public void removeDetails() {
        if ( socialProfile != null ) {
            socialProfile.setUser(this);
            this.socialProfile = null;
        }
    }


    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        User user = (User) o;
        return Objects.equals( userId, user.userId ) && Objects.equals(socialProfile.socialProfileId, user.socialProfile.socialProfileId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, socialProfile.socialProfileId);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User copy() {
        User newUser = new User(this.mobile, this.userNameSeed, this.email, this.platform,
                                this.deviceId, this.softDeleted) ;
        newUser.addSocialProfile(this.socialProfile.copy());
        return newUser ;
    }
}
