package results;

import models.Album;
import models.Post;
import models.User;

import java.util.List;

/**
 * Created by yuva on 2/5/17.
 */
public class ProfileData {

    public User user ;
    public List<Album> albums ;
    public List<Post> orphanedPosts ;

    public ProfileData(User user, List<Album> albums, List<Post> posts) {
        this.user = user ;
        this.albums = albums ;
        this.orphanedPosts = posts ;
    }

}
