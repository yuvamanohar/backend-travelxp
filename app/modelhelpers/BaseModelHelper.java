package modelhelpers;

import models.BaseModel;
import models.Post;
import play.db.jpa.JPAApi;
import services.DatabaseExecutionContext;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by yuva on 18/4/17.
 */
public abstract class BaseModelHelper<T extends BaseModel, K> {

    protected final JPAApi jpaApi;
    protected final DatabaseExecutionContext executionContext;

    @Inject
    public BaseModelHelper(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    public T insert(T model) {
        jpaApi.em().persist(model) ;
        return model ;
    }

    public CompletionStage<T> insertAsync(T model) {
        return  supplyAsync(() -> wrapInTransaction(em -> insert(model)), executionContext) ;
    }

    public T merge(T model) {
        jpaApi.em().merge(model) ;
        return model ;
    }

    public CompletionStage<T> mergeAsync(T model) {
        return  supplyAsync(() -> wrapInTransaction(em -> merge(model)), executionContext) ;
    }

    public abstract T get(K primaryKey) ;

    public CompletionStage<T> getAsync(K primaryKey) {
        return  supplyAsync(() -> wrapInTransaction(em -> get(primaryKey)), executionContext) ;
    }

    public T softDelete(T model) {
        model.setSoftDeleted();
        jpaApi.em().merge(model) ;
        return model ;
    }

    public CompletionStage<T> softDeleteAsync(T model) {
        return supplyAsync(() -> wrapInTransaction(em -> softDelete(model)), executionContext) ;
    }

    protected <T> T wrapInTransaction(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
}
