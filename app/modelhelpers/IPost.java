package modelhelpers;

import com.google.inject.ImplementedBy;
import models.Post;

import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 20/4/17.
 */
@ImplementedBy(PostHelper.class)
public interface IPost {
    public CompletionStage<Post> insertAsync(Post post) ;
}
