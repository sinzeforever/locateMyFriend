package hkec.yahoo.locatemyfriends;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by sinze on 3/9/15.
 */
public class MyGroupFragment extends Fragment{
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
        groupListAdapter = new GroupListAdapter(getActivity().getApplicationContext(), R.layout.group_list, groupList);
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
}
