package modelhelpers;

import com.google.inject.ImplementedBy;
import modelhelpers.SocialProfileHelper;
import models.SocialProfile;
import models.User;

import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 17/4/17.
 */
@ImplementedBy(SocialProfileHelper.class)
public interface ISocialProfile extends IBaseModel {
    public SocialProfile getByNetworkAndNetworkId(String socialNetwork, String socialNetworkId) ;
    public CompletionStage<SocialProfile> getByNetworkAndNetworkIdAsync(String socialNetwork, String socialNetworkId) ;
}
