package results;

import models.SocialProfile;
import models.User;

/**
 * Created by yuva on 18/4/17.
 */
public class UserData {
    public User user ;
    public String userStatus ;

    public UserData(User _user, String _userStatus) {
        this.user = _user ;
        this.userStatus = _userStatus ;
    }
}
