package modelhelpers;

import models.BaseModel;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by yuva on 18/4/17.
 */
public abstract class BaseModelHelper<T extends BaseModel, K> {

    protected final JPAApi jpaApi;
    protected final DatabaseExecutionContext dbExecutionContext;

    @Inject
    public BaseModelHelper(JPAApi jpaApi, DatabaseExecutionContext dbExecutionContext) {
        this.jpaApi = jpaApi;
        this.dbExecutionContext = dbExecutionContext;
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

    public abstract T get(K primaryKey) ;

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
}
