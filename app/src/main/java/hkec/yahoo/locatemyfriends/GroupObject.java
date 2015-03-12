package hkec.yahoo.locatemyfriends;

import java.util.HashMap;

/**
 * Created by sinze on 3/10/15.
 */
public class GroupObject {
    public String name;
    public HashMap<String,MemberObject> memberList;
    public String color;
    public boolean visibility;

    public GroupObject(String name) {
        this.name = name;
        this.memberList = new HashMap<String, MemberObject>();
        this.visibility = true;
        this.color = "#FFFFFF";
    }

    public void addMember(MemberObject member) {
        memberList.put(member.name, member);
    }

    public void addMember(String _name) {
        memberList.put(_name, new MemberObject(_name));
    }

    public void setGroupVisibility(boolean flag) {
        this.visibility = flag;
    }
}
