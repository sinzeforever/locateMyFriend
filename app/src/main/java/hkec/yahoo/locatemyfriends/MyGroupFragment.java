package hkec.yahoo.locatemyfriends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.LocationSyncroner;

/**
 * Created by sinze on 3/9/15.
 */
public class MyGroupFragment extends BaseFragment{
    private ListView groupListView;
    private View rootView;
    private GroupListAdapter groupListAdapter;
    private Button disableAllButton;
    private MainActivity mainActivity;
    public GroupObject[] groupList;
    private int currentAPI = 0;
    private final int API_NONE = 0;
    private final int API_GET_GROUP_LIST = 1;
    private final int API_DISABLE_ALL = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_group, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        groupList = new GroupObject[0];
        this.mainActivity = (MainActivity) getActivity();
        setDisableAllButton();
        callGetGroupAPI();
    }

    public void setDisableAllButton() {
        disableAllButton = (Button) rootView.findViewById(R.id.myGroupDisableAllButton);
        disableAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllGroup();
            }
        });
    }

    public void setGoCreateGroupMsg() {
        View createGroupMessage = rootView.findViewById(R.id.myGroupNoGroupLayout);
        createGroupMessage.setVisibility(View.VISIBLE);
        Button goCreateGroupButton = (Button) rootView.findViewById(R.id.myGroupAddGroupButton);
        goCreateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setAddGroupPage();
            }
        });
    }

    public void setListView() {
        //groupList = UserProfile.getInstance().groupList.values().toArray(new GroupObject[0]);
        // show disable all button
        disableAllButton.setVisibility(View.VISIBLE);

        // bind group list array adapter
        groupListAdapter = new GroupListAdapter(getActivity(), R.layout.group_list_entry, groupList);
        groupListView = (ListView) rootView.findViewById(R.id.groupList);
        groupListView.setAdapter(groupListAdapter);
    }

    public void disableAllGroup() {
        // reload the list
        setListView();
        // close all checkbox on the view
        for(int i=0; i<groupList.length; i++) {
            groupList[i].visibility = false;
        }
        callDisableAllGroupAPI();
    }

    public void callDisableAllGroupAPI() {
        if (currentAPI != API_NONE) {
            return;
        }
        String[] groupListStr = new String[groupList.length];
        for(int i=0; i < groupList.length; i++) {
            groupListStr[i] = groupList[i].name;
        }
        new API().delMemberVisibleToGroups(mainActivity, UserProfile.getInstance().id, groupListStr, getEventBus());
    }

    public void callSetGroupVisibilityAPI(GroupObject targetGroup, boolean isVisible) {
        String[] apiParam = {targetGroup.name};
        if (isVisible) {
            new API().setMemberVisibleToGroups(mainActivity, UserProfile.getInstance().id, apiParam, getEventBus());
        } else {
            new API().delMemberVisibleToGroups(mainActivity, UserProfile.getInstance().id, apiParam, getEventBus());
        }
    }

    public void callGetGroupAPI() {
        if (currentAPI != API_NONE) {
            return;
        }
        currentAPI = API_GET_GROUP_LIST;
        new API().getGroupsFromMember(mainActivity, UserProfile.getInstance().id, getEventBus());
    }

    public void postGetGroupAPI(String response) {
        // parsing group list API data and save it
        groupList = parseGroupListJSONData(response);

        // if the user has more than one group
        if (groupList.length > 0) {
            // set list view
            setListView();
        } else {
            // show create-group message
            setGoCreateGroupMsg();
        }
    }

    public void getGroupAPICallback(String response) {
        if (Util.getAPIResponseStatus(response)) {
            postGetGroupAPI(response);
        } else {
            mainActivity.makeToast("Error parsing api when getting group list");
        }
    }

    public GroupObject[] parseGroupListJSONData(String response) {
        JSONArray groupJSONArray = Util.getAPIResponseArrayData(response);
        ArrayList<GroupObject> groupList = new ArrayList<GroupObject>();
        if (groupJSONArray != null) {
            try {
                for (int i = 0; i < groupJSONArray.length(); i++) {
                    JSONObject groupJSONObject = new JSONObject(groupJSONArray.getString(i));
                    String tmpGroupName = groupJSONObject.getString("name");
                    boolean tmpVisibility = groupJSONObject.getBoolean("visible");
                    int tmpMemberCount = groupJSONObject.getInt("count");

                    // create a new group object and set value
                    GroupObject tmpGroup = new GroupObject(tmpGroupName);
                    tmpGroup.visibility = tmpVisibility;
                    tmpGroup.memberCount = tmpMemberCount;
                    groupList.add(tmpGroup);

                }
            } catch (Exception e) {
                Log.d("myLog", "Fail to parse get-group-list API data");
            }
        }
        GroupObject[] groupAry = new GroupObject[groupList.size()];
        return  groupList.toArray(groupAry);
    }

    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {
        if (currentAPI == API_GET_GROUP_LIST) {
            getGroupAPICallback(dma.data.toString());
        }
        currentAPI = API_NONE;
    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {

    }
}
