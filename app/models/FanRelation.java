package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * Created by yuva on 3/5/17.
 */
@NamedQueries({
        @NamedQuery(
                name = "get_fans_for_user",
                query = "select f from FanRelation f where f.influencerUser = :user and f.softDeleted = false"),
        @NamedQuery(
                name = "get_influencers_for_user",
                query = "select f from FanRelation f where f.fanUser = :user and f.softDeleted = false")
})

@Entity
@Table(name="fanRelations")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="fanRelationId")
public class FanRelation extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long fanRelationId ;

    @ManyToOne
    @JoinColumn(name = "fanUserId",
            foreignKey = @ForeignKey(name = "fk_fan_relation_fan_user"))
    @JsonIgnore
    private User fanUser ;

    @ManyToOne
    @JoinColumn(name = "influencerUserId",
            foreignKey = @ForeignKey(name = "fk_fan_relation_influencer_user"))
    @JsonIgnore
    private User influencerUser ;

    public FanRelation(User fanUser, User influencerUser) {
        this.fanUser = fanUser ;
        this.influencerUser = influencerUser ;
    }

    public User getFanUser() {
        return fanUser;
    }

    public void setFanUser(User fanUser) {
        this.fanUser = fanUser;
    }

    public User getInfluencerUser() {
        return influencerUser;
    }

    public void setInfluencerUser(User influencerUser) {
        this.influencerUser = influencerUser;
    }

    public Long getFanUserId() {
        return fanUser.userId ;
    }

    public Long getInfluencerUserId() {
        return influencerUser.userId ;
    }
}
