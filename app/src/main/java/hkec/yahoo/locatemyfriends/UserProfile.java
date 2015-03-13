package hkec.yahoo.locatemyfriends;

/**
 * Created by sinze on 3/10/15.
 */
public class UserProfile {
    public String id;
    private static UserProfile instance;

    public UserProfile(String _id) {
        this.id = _id;
        UserProfile.instance = this;
    }

    public static UserProfile getInstance() {
        return instance;
    }
}
