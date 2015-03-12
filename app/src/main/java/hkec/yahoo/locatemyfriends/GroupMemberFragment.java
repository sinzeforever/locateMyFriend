package hkec.yahoo.locatemyfriends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    private MemberListAdapter memberListAdapter;

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
        setTitle();
        setListView();
        setBackButton();
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

    public void setTitle() {
        if (group != null) {
            titleView = (TextView) rootView.findViewById(R.id.memberListGroupName);
            titleView.setText(group.name);
        }
    }

    public void setBackButton() {

        titleWrapperView = rootView.findViewById(R.id.memberListTitleWrapper);
        titleWrapperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setMyGroupPage();
            }
        });
    }
    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {

    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {

    }
}
