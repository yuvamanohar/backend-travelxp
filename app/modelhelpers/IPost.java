package modelhelpers;

import com.google.inject.ImplementedBy;
import models.Post;
import models.PostDetail;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 20/4/17.
 */
@ImplementedBy(PostHelper.class)
public interface IPost {
    public Post insertPostAndPostDetails(Post post) ;
    public CompletionStage<Post> insertPostAndPostDetailsAsync(Post post) ;
}
