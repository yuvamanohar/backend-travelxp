package models.interfaces;

import com.google.inject.ImplementedBy;
import models.helpers.SocialProfileHelper;
import models.SocialProfile;

import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 17/4/17.
 */
@ImplementedBy(SocialProfileHelper.class)
public interface ISocialProfile extends IBaseModel {
    public CompletionStage<SocialProfile> getByNetworkAndNetworkIdAsync(String socialNetwork, String socialNetworkId) ;
}
