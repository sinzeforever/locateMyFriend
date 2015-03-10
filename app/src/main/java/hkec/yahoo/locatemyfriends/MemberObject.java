package hkec.yahoo.locatemyfriends;

/**
 * Created by sinze on 3/10/15.
 */
public class MemberObject {
    public String name;
    public double latitude;
    public double longitude;
    public boolean visibility;

    public MemberObject(String name) {
        this.name = name;
        this.visibility = true;
    }
}
