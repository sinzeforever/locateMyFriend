package hkec.yahoo.locatemyfriends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.LocationSyncroner;

/**
 * Created by sinze on 3/12/15.
 */
public class GroupMemberFragment extends BaseFragment {

    private GroupObject group;
    private View rootView;
    private ListView memberListView;
    private ImageView backButton;
    private TextView titleView;
    private View titleWrapperView;
    private EditText addMemberInput;
    private ImageButton addMemberButton;
    private Button leaveButton;
    private MemberListAdapter memberListAdapter;
    private MainActivity mainActivity;
    private String newMemberId;
    private int currentAPI = 0;
    private final int API_NONE = 0;
    private final int API_GET_GROUP_MEMBERS = 1;
    private final int API_ADD_MEMBER = 2;
    private final int API_LEAVE_GROUP = 3;

    public void setGroup(GroupObject group) {
        this.group = group;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_member, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mainActivity = (MainActivity) getActivity();
        callGetGroupMembersAPI();
        setTitle();
        setBackButton();
        setAddMember();
        setLeaveGroupButton();
    }

    public boolean setListView() {
        // bind group member list array adapter
        if (group == null) {
            return false;
        } else {
            int listSize = group.memberList.size();
            MemberObject[] memberList = (MemberObject [])group.memberList.values().toArray(new MemberObject[listSize]);
            memberListAdapter = new MemberListAdapter(getActivity(), R.layout.member_list_entry, memberList);
            memberListView = (ListView) rootView.findViewById(R.id.memberList);
            memberListView.setAdapter(memberListAdapter);
            return true;
        }
    }

    public void setAddMember() {
        addMemberInput = (EditText) rootView.findViewById(R.id.memberListNameInput);
        addMemberButton = (ImageButton) rootView.findViewById(R.id.memberListAddMemberButton);
        addMemberButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clickAddNewMember();
            }
        });
    }

    public void setLeaveGroupButton() {
        leaveButton = (Button) rootView.findViewById(R.id.memberListLeaveButton);
        leaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callLeaveGroupAPI();
            }
        });
    }

    public void callGetGroupMembersAPI() {
        if (currentAPI != API_NONE) {
            return;
        }
        currentAPI = API_GET_GROUP_MEMBERS;
        new API().getMembersFromGroup(mainActivity, group.name, getEventBus());
    }

    public void postGetGroupMembersAPI(String response) {
        // parse JSON data and put all the member into the list
        parseGroupMembersJSONData(response);

        // set title
        setTitle();

        // set list
        setListView();
    }

    public void parseGroupMembersJSONData(String response) {
        JSONArray memberJSONArray = Util.getAPIResponseArrayData(response);
        if (memberJSONArray != null) {
            try {
                for (int i = 0; i < memberJSONArray.length(); i++) {
                    String memberName = new JSONObject(memberJSONArray.getString(i)).getString("name");
                    Log.d("myLog", memberName);
                    group.memberList.put(memberName, new MemberObject(memberName));
                }
            } catch (Exception e) {
                Log.d("myLog", "Fail to parse get-group-members API data");
            }
        }
    }

    public void callLeaveGroupAPI() {
        if (currentAPI != API_NONE) {
            return;
        }
        currentAPI = API_LEAVE_GROUP;
        String[] apiParam = {UserProfile.getInstance().id};
        new API().delMembersFromGroup(mainActivity, group.name, apiParam, getEventBus());
    }

    public void postLeaveGroupAPI() {
        mainActivity.setMyGroupPage();
    }

    public void clickAddNewMember() {
        Util.hideKeyBoard(mainActivity);
        newMemberId = addMemberInput.getText().toString();
        if (group == null) {
            Log.d("myLog", "Error - can't not find group in group member list page");
        } else if (newMemberId.length() > 0 ) {
            callAddGroupToMemberAPI(newMemberId);
        } else {
            mainActivity.makeToast("Please enter new member id");
        }
    }

    public void callAddGroupToMemberAPI(String memberId) {
        if (currentAPI != API_NONE) {
            return;
        }
        currentAPI = API_ADD_MEMBER;
        String[] apiParam = {group.name};
        new API().addGroupsToMember(mainActivity, memberId, apiParam, getEventBus());
    }

    public void postAddGroupToMemberAPI() {
        mainActivity.makeToast("Successfully add new member " + newMemberId + " into group");
        addMemberInput.getText().clear();
        group.memberList.put(newMemberId, new MemberObject(newMemberId));
        setListView();
    }

    public void setTitle() {
        if (titleView == null) {
            titleView = (TextView) rootView.findViewById(R.id.memberListGroupName);
        }
        if (group.memberList.size() > 0) {
            titleView.setText(group.name + " (" + group.memberList.size() + ")");
        } else {
            titleView.setText(group.name);
        }
    }

    public void setBackButton() {

        titleWrapperView = rootView.findViewById(R.id.memberListTitleWrapper);
        titleWrapperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setMyGroupPage();
            }
        });
    }
    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {
        if (currentAPI == API_GET_GROUP_MEMBERS) {
            if (Util.getAPIResponseStatus(dma.data.toString())) {
                postGetGroupMembersAPI(dma.data.toString());
            } else {
                mainActivity.makeToast("Fail to get group members");
            }
        } else if (currentAPI == API_ADD_MEMBER) {
            if (Util.getAPIResponseStatus(dma.data.toString())) {
                postAddGroupToMemberAPI();
            } else {
                mainActivity.makeToast("Fail to add user to the group");
            }
        } else if (currentAPI == API_LEAVE_GROUP) {
           postLeaveGroupAPI();
        }
        currentAPI = API_NONE;
    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {

    }
}
