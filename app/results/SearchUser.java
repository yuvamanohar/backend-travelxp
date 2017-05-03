package results;

/**
 * Created by yuva on 3/5/17.
 */
public class SearchUser {
    public final Long userId ;
    public final String name ;
    public final String userName ;
    public final String profilePic ;


    public SearchUser(Long userId, String name, String userName, String profilePic) {
        this.userId = userId;
        this.name = name;
        this.userName = userName;
        this.profilePic = profilePic;
    }
}
