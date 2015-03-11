package hkec.yahoo.locatemyfriends;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;

/**
 * Created by sinze on 3/9/15.
 */
public class AddGroupFragment extends Fragment implements API.DataHandler{

    private Button confirmButton;
    private Button cancelButton;
    private ImageButton addMemberButton;
    private EditText groupNameInput;
    private EditText memberNameInput;
    private ViewGroup memberListLayout;
    private View rootView;
    private MainActivity mainActivity;
    private HashMap<String, MemberObject> tmpMemberList;
    private boolean isValidGroup = false;
    private EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_group, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mainActivity = (MainActivity) getActivity();
        tmpMemberList = new HashMap<String, MemberObject>();
        eventBus = new EventBus();
        eventBus.register(this);
        setElements();
    }

    public void setElements() {
        confirmButton = (Button) rootView.findViewById(R.id.addGroupConfirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickConfirmButton();
            }
        });
        cancelButton = (Button) rootView.findViewById(R.id.addGroupCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCancelButton();
            }
        });
        addMemberButton = (ImageButton) rootView.findViewById(R.id.addGroupAddMemberButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddMemberButton();
            }
        });
        memberListLayout = (ViewGroup) rootView.findViewById(R.id.addGroupMemberList);
        memberNameInput = (EditText) rootView.findViewById(R.id.addGroupMemberNameInput);
        groupNameInput = (EditText) rootView.findViewById(R.id.addGroupGroupNameInput);
    }

    public void clickCancelButton() {
        mainActivity.setMapPage();
    }

    public void clickConfirmButton() {
        doCreateGroup();
        mainActivity.setMyGroupPage();
    }

    public void doCreateGroup() {
        //call api to create group
    }

    public void clickAddMemberButton() {
        String memberId = memberNameInput.getText().toString();
        // hide keyboard
        Util.hideKeyBoard(getActivity());
        // call api to check if the user exist
        // if user exists, add user to the tmp group
        addMemberToTmpGroup(memberId);
    }

    public void addMemberToTmpGroup(String memberId) {
        // put member in tmp list
        tmpMemberList.put(memberId, new MemberObject(memberId));
        // show member in view entry
        showNewUserInTheList(memberId);
    }

    public void showNewUserInTheList(final String memberId) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.add_member_entry, null);

        // fill in any details dynamically here
        TextView memberIdView = (TextView) v.findViewById(R.id.addMemberEntryId);
        memberIdView.setText(memberId);
        TextView closeButton = (TextView) v.findViewById(R.id.addMemberEntryCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMemberEntry(memberId, v);
            }
        });
        // insert into main view
        ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.addGroupMemberList);
        //insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        memberListLayout.addView(v);

        // clear member name input
        memberNameInput.getText().clear();
    }
    public void removeMemberEntry(String memberId, View v) {
        // remove member from tmp list
        tmpMemberList.remove(memberId);
        // remove member view
        View memberEntryView =  (View)v.getParent();
        ((ViewManager)memberEntryView.getParent()).removeView(memberEntryView);
    }

    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {
        Log.e("return data", dma.data.toString());
    }
}