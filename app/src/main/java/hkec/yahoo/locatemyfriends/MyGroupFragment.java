package hkec.yahoo.locatemyfriends;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by sinze on 3/9/15.
 */
public class MyGroupFragment extends Fragment{
    private ListView groupListView;
    private View rootView;
    private GroupListAdapter groupListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // bind group list array adapter
        int listSize = UserProfile.instance.groupList.values().size();
        GroupObject[] groupList = (GroupObject [])UserProfile.instance.groupList.values().toArray(new GroupObject[listSize]);
        groupListAdapter = new GroupListAdapter(getActivity().getApplicationContext(), R.layout.group_list, groupList);
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
    }

    public void setListView() {
        groupListView = (ListView) rootView.findViewById(R.id.groupList);
        groupListView.setAdapter(groupListAdapter);
    }
}
