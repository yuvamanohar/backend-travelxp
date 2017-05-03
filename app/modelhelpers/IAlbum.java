package modelhelpers;

import com.google.inject.ImplementedBy;
import models.Album;
import models.User;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by yuva on 2/5/17.
 */
@ImplementedBy(AlbumHelper.class)
public interface IAlbum {
    public CompletionStage<Album> insertAsync(Album album) ;
    public CompletionStage<Album> mergeAsync(Album album) ;

    public CompletionStage<List<Album>> getAllAsync(User user) ;
}
