package modelhelpers;

import models.BaseModel;
import models.User;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by yuva on 18/4/17.
 */
public abstract class BaseModelHelper<T extends BaseModel, K> {

    private String primaryKeyCol ;
    protected final JPAApi jpaApi;
    protected final DatabaseExecutionContext dbExecutionContext;
    protected Class<T> tClass ;

    @Inject
    public BaseModelHelper(JPAApi jpaApi, DatabaseExecutionContext dbExecutionContext, Class tClass) {
        this.jpaApi = jpaApi;
        this.dbExecutionContext = dbExecutionContext;
        this.tClass = tClass ;
    }

    public T insert(T model) {
        jpaApi.em().persist(model) ;
        return model ;
    }

    public CompletionStage<T> insertAsync(T model) {
        return  supplyAsync(() -> wrapInTransaction(em -> insert(model)), dbExecutionContext) ;
    }

    public T merge(T model) {
        jpaApi.em().merge(model) ;
        return model ;
    }

    public CompletionStage<T> mergeAsync(T model) {
        return  supplyAsync(() -> wrapInTransaction(em -> merge(model)), dbExecutionContext) ;
    }

   public T get(K primaryKey)  {

       CriteriaBuilder builder = jpaApi.em().getCriteriaBuilder();

       CriteriaQuery<T> criteria = builder.createQuery(tClass);
       Root<T> root = criteria.from( tClass );
       criteria.select( root );
       criteria.where(builder.and(
               builder.equal(root.get(getPrimaryKeyCol()), primaryKey),
               builder.equal( root.get( "softDeleted" ), false ))) ;

       List<T> results = jpaApi.em().createQuery( criteria ).getResultList() ;

       if(results.size() > 0)
           return results.get(0) ;

       return null ;
    }

    public CompletionStage<T> getAsync(K primaryKey) {
        return  supplyAsync(() -> wrapInTransaction(em -> get(primaryKey)), dbExecutionContext) ;
    }

    public T softDelete(T model) {
        model.setSoftDeleted();
        jpaApi.em().merge(model) ;
        return model ;
    }

    public CompletionStage<T> softDeleteAsync(T model) {
        return supplyAsync(() -> wrapInTransaction(em -> softDelete(model)), dbExecutionContext) ;
    }

    protected <T> T wrapInTransaction(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    public String getPrimaryKeyCol() {
        if(primaryKeyCol == null) {
            String clazz = tClass.getSimpleName() ;
            primaryKeyCol = Character.toLowerCase(clazz.charAt(0)) + clazz.substring(1) + "Id" ;
        }
        return primaryKeyCol ;
    }
}
