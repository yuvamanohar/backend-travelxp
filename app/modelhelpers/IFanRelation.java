package modelhelpers;

import com.google.inject.ImplementedBy;
import models.FanRelation;
import models.User;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 3/5/17.
 */
@ImplementedBy(FanRelationHelper.class)
public interface IFanRelation {
    public CompletionStage<FanRelation> insertAsync(FanRelation fanRelation) ;

    public CompletionStage<List<FanRelation>> getFansAsync(User user) ;
    public CompletionStage<List<FanRelation>> getInfluencersAsync(User user) ;
}
