package modelhelpers;

import com.google.inject.Inject;
import models.SocialProfile;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;


/**
 * Created by yuva on 17/4/17.
 */
public class SocialProfileHelper extends BaseModelHelper<SocialProfile, Long> implements ISocialProfile {

    @Inject
    public SocialProfileHelper(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext, SocialProfile.class);
    }

//    @Override
//    public SocialProfile get(Long socialProfileId) {
//        TypedQuery<SocialProfile> socialProfileTypedQuery = jpaApi.em().createNamedQuery("sp_get_by_id", SocialProfile.class)
//                .setParameter("socialProfileId", socialProfileId)
//                .setParameter("softDeleted", true) ;
//        try {
//            return socialProfileTypedQuery.getSingleResult();
//        } catch (NoResultException e) {
//            return  null ;
//        }
//    }

    @Override
    public SocialProfile getByNetworkAndNetworkId(String socialNetwork, String socialNetworkId) {
        TypedQuery<SocialProfile> socialProfileTypedQuery = jpaApi.em().createNamedQuery("sp_get_by_network_and_network_id", SocialProfile.class)
                                                                        .setParameter("socialNetwork", socialNetwork)
                                                                        .setParameter("socialNetworkId", socialNetworkId) ;
        try {
            return socialProfileTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            return  null ;
        }
    }

    @Override
    public CompletionStage<SocialProfile> getByNetworkAndNetworkIdAsync(String socialNetwork, String socialNetworkId) {
        return supplyAsync(() -> wrapInTransaction(entityManager -> getByNetworkAndNetworkId(socialNetwork, socialNetworkId)) , dbExecutionContext) ;
    }
}
