package hkec.yahoo.locatemyfriends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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
        setListView();
        setDisableAllButton();
    }

    public void setDisableAllButton() {
        disableAllButton = (Button) rootView.findViewById(R.id.disableAllButton);
        disableAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllGroup();
            }
        });
    }

    public void setListView() {
        // bind group list array adapter
        int listSize = UserProfile.getInstance().groupList.values().size();
        GroupObject[] groupList = (GroupObject [])UserProfile.getInstance().groupList.values().toArray(new GroupObject[listSize]);
        groupListAdapter = new GroupListAdapter(getActivity().getApplicationContext(), R.layout.group_list_entry, groupList);
        groupListView = (ListView) rootView.findViewById(R.id.groupList);
        groupListView.setAdapter(groupListAdapter);
    }

    public void disableAllGroup() {
        for(GroupObject group : UserProfile.getInstance().groupList.values()){
            group.setGroupVisibility(false);
        }
        // reload the list
        setListView();
    }

    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {

    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {

    }
}
