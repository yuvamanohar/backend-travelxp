package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by yuva on 17/4/17.
 */
@NamedQueries({
        @NamedQuery(
                name = "user_get_by_id",
                query = "select u from User u where u.userId = :userId and softDeleted = :softDeleted"),
        @NamedQuery(
                name = "user_soft_delete",
                query = "update User u set u.softDeleted = :softDeleted where u.userId = :userId"),
        @NamedQuery(
                name = "user_update_platform_and_device_id",
                query = "update User u set u.platform = :platform, u.deviceId = :deviceId  where u.userId = :userId")
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

    public Long mobile ;
    public String email ;
    public String platform  ;
    public String deviceId ;

    public User() {} ;

    public User(Long mobile, String email, String platform,
                String deviceId, Boolean softDeleted) {
        this.mobile = mobile ;
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
}
