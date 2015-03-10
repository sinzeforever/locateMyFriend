package hkec.yahoo.locatemyfriends;

import java.util.HashMap;

/**
 * Created by sinze on 3/10/15.
 */
public class UserProfile {
    public String id;
    public HashMap<String, GroupObject> groupList;
    public static UserProfile instance;

    public UserProfile(String _id) {
        this.id = _id;
        groupList = new HashMap<String, GroupObject>();

        // temporarily put fake group data
        testPrepareUserGroup();
    }

    public void addGroup(GroupObject group) {
        groupList.put(group.name, group);
    }

    public void testPrepareUserGroup() {
        GroupObject group1 = new GroupObject("group1");
        group1.addMember("Allen");
        group1.addMember("Andy");
        group1.addMember("Annie");
        addGroup(group1);
        GroupObject group2 = new GroupObject("group2");
        group2.addMember("Betty");
        group2.addMember("Bob");
        addGroup(group2);
        GroupObject group3 = new GroupObject("group3");
        group3.addMember("Casar");
        group3.visibility = false;
        addGroup(group3);
    }
}
