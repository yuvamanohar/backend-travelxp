package models.helpers;

import com.google.inject.Inject;
import models.interfaces.IFanRelation;
import models.FanRelation;
import models.User;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.persistence.TypedQuery;
import java.util.List;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 3/5/17.
 */
public class FanRelationHelper extends BaseModelHelper<FanRelation, Long> implements IFanRelation {

    @Inject
    public FanRelationHelper(JPAApi jpaApi, DatabaseExecutionContext dbExecutionContext) {
        super(jpaApi, dbExecutionContext, FanRelation.class);
    }

    public List<FanRelation> getFans(User user) {
        TypedQuery<FanRelation> typedQuery = jpaApi.em().createNamedQuery("get_fans_for_user", FanRelation.class)
                                                        .setParameter("user", user) ;

        return typedQuery.getResultList();
    }

    @Override
    public CompletionStage<List<FanRelation>> getFansAsync(User user) {
        return supplyAsync(() -> wrapInTransaction(em -> getFans(user)), dbExecutionContext) ;
    }

    public List<FanRelation> getInfluencers(User user) {
        TypedQuery<FanRelation> typedQuery = jpaApi.em().createNamedQuery("get_influencers_for_user", FanRelation.class)
                                                        .setParameter("user", user) ;

        return typedQuery.getResultList();
    }

    @Override
    public CompletionStage<List<FanRelation>> getInfluencersAsync(User user) {
        return supplyAsync(() -> wrapInTransaction(em -> getInfluencers(user)), dbExecutionContext) ;
    }

}
