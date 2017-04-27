package results;

import models.SocialProfile;
import models.User;

/**
 * Created by yuva on 18/4/17.
 */
public class UserData {
    public final User user ;
    public final String userStatus ;
    public final String serverTimeAtStart ;

    public UserData(User user, String userStatus, String serverTimeAtStart) {
        this.user = user ;
        this.userStatus = userStatus ;
        this.serverTimeAtStart = serverTimeAtStart ;
    }
}
