package hkec.yahoo.locatemyfriends;

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
import android.os.Handler;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.LocationSyncroner;

/**ckday_demo_lib.API;

 * Created by sinze on 3/9/15.
 */
public class AddGroupFragment extends BaseFragment{

    private Button confirmButton;
    private Button cancelButton;
    private ImageButton addMemberButton;
    private EditText groupNameInput;
    private EditText memberNameInput;
    private TextView memberCountView;
    private ViewGroup memberListLayout;
    private View rootView;
    private MainActivity mainActivity;
    private ArrayList<String> tmpMemberList;
    private String tmpGroupName;
    private EventBus eventBus;
    private int currentAPI = 0;
    private final int API_NONE = 0;
    private final int API_CREATE_GROUP = 1;
    private final int API_ADD_MEMBERS_TO_GROUP = 2;


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
        tmpMemberList = new ArrayList<String>();
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
        memberCountView = (TextView) rootView.findViewById(R.id.addGroupMemberCount);
    }

    public void clickCancelButton() {
        mainActivity.setMapPage();
    }

    public void clickConfirmButton() {
        tmpGroupName = groupNameInput.getText().toString();
        if (validateCreateGroup()) {
            //call api to create group
            callCreateGroupAPI();
        }
    }

    public boolean validateCreateGroup() {
        if (tmpGroupName.length() == 0) {
            mainActivity.makeToast("Please enter group name");
            return false;
        } else if (tmpMemberList.size() == 0){
            mainActivity.makeToast("Please enter at least one member");
            return false;
        } else {
            return true;
        }
    }

    public void callCreateGroupAPI() {
        if (currentAPI != API_NONE) {
            return;
        }
        currentAPI = API_CREATE_GROUP;
        String[] apiParam = {groupNameInput.getText().toString()};
        new API().addGroups(getActivity(), apiParam, eventBus);
    }

    public void postCreateGroup() {
        currentAPI = API_NONE;
        callAddMemberToGroupAPI();
    }

    public void callAddMemberToGroupAPI() {

        if (currentAPI != API_NONE) {
            return;
        }
        // add the user himself into the tmp member list
        tmpMemberList.add(UserProfile.getInstance().id);

        // parse param
        String[] apiParam = new String[tmpMemberList.size()];
        tmpMemberList.toArray(apiParam);

        // call api
        currentAPI = API_ADD_MEMBERS_TO_GROUP;
        new API().addMembersToGroup(getActivity(), tmpGroupName, apiParam, eventBus);
    }


    public void postAddMemberToGroup() {
        currentAPI = API_NONE;
        mainActivity.makeToast("Successfully create group");
        Handler handler = new Handler();
        // delay and back to my group page
        handler.postDelayed(new Runnable() {
            public void run() {
                mainActivity.setMyGroupPage();
            }
        }, 1200);
    }

    public void clickAddMemberButton() {
        String memberId = memberNameInput.getText().toString();
        // hide keyboard
        Util.hideKeyBoard(getActivity());
        // call api to check if the user exist
        // if user exists, add user to the tmp group
        if (memberId.length() > 0 ) {
            addMemberToTmpGroup(memberId);
        } else {
            mainActivity.makeToast("Please enter valid user id");
        }
    }

    public void addMemberToTmpGroup(String memberId) {
        // put member in tmp list
        tmpMemberList.add(memberId);
        // show member in view entry
        showNewUserInTheList(memberId);
        // change the group member count
        setMemberCountView();
    }

    public void setMemberCountView() {
        memberCountView.setText("(" + tmpMemberList.size() +")");
    }

    public void showNewUserInTheList(final String memberId) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entry = vi.inflate(R.layout.add_member_entry, null);

        // fill in any details dynamically here
        TextView memberIdView = (TextView) entry.findViewById(R.id.addMemberEntryId);
        memberIdView.setText(memberId);
        TextView closeButton = (TextView) entry.findViewById(R.id.addMemberEntryCloseButton);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMemberEntry(memberId, v);
            }
        });
        // insert into main view
        ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.addGroupMemberList);
        //insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        memberListLayout.addView(entry);

        // clear member name input
        memberNameInput.getText().clear();
    }
    public void removeMemberEntry(String memberId, View memberEntryView) {
        // remove member from tmp list
        tmpMemberList.remove(memberId);
        // remove member view
        //View memberEntryView =  (View)v.getParent();
        ((ViewManager)memberEntryView.getParent()).removeView(memberEntryView);
        // change member count view
        setMemberCountView();
    }

    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {
        Log.e("return data", dma.data.toString());
        if (currentAPI == API_CREATE_GROUP) {
            if (Util.getAPIResponseStatus(dma.data.toString())) {
                postCreateGroup();
            } else {
                mainActivity.makeToast("Error parsing api when creating group");
            }
        }
        else if (currentAPI == API_ADD_MEMBERS_TO_GROUP) {
             Log.d("myLog", "add m ca ");
            if (Util.getAPIResponseStatus(dma.data.toString())) {
                postAddMemberToGroup();
            } else {
                mainActivity.makeToast("Error parsing api when add members to group ");
            }
        }
    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {
        //
    }
}