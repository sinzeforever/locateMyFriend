package hkec.yahoo.locatemyfriends;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;

/**
 * Created by sinze on 3/9/15.
 */
public class MainActivity extends FragmentActivity {

    public BaseFragment currentFragment;
    private int currentPage;
    private ImageView mapButton;
    private ImageView myGroupButton;
    private ImageView addGroupButton;
    private ImageView settingButton;
    private Toast toast;
    private final int MAP_PAGE = 1;
    private final int MY_GROUP_PAGE = 2;
    private final int ADD_GROUP_PAGE = 3;
    private final int SETTING_PAGE = 4;
    private final int GROUP_MEMBER_PAGE = 5;
    private final float highlightAlpha = 1.0f;
    private final float defaultButtonAlpha = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToast();
        setContentView(R.layout.main);
        setButtons();
        setDefaultPage();
    }

    private void setToast() {
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    }

    private void setButtons() {

        mapButton = (ImageView) findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == MAP_PAGE) {
                    return;
                } else {
                    setMapPage();
                }
            }
        });
        myGroupButton = (ImageView) findViewById(R.id.my_group_button);
        myGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == MY_GROUP_PAGE) {
                    return;
                } else {
                    setMyGroupPage();
                }
            }
        });
        addGroupButton = (ImageView) findViewById(R.id.add_group_button);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == ADD_GROUP_PAGE) {
                    return;
                } else {
                    setAddGroupPage();
                }
            }
        });
        settingButton = (ImageView) findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == SETTING_PAGE) {
                    return;
                } else {
                    setSettingPage();
                }
            }
        });
    }

    public void setMapPage() {
        currentPage = MAP_PAGE;
        resetAllButton();
        mapButton.setAlpha(highlightAlpha);
        toast.cancel();
        currentFragment = new MapFragment();
        setPage(currentFragment);
    }

    public void setMyGroupPage() {
        currentPage = MY_GROUP_PAGE;
        resetAllButton();
        myGroupButton.setAlpha(highlightAlpha);
        toast.cancel();
        currentFragment = new MyGroupFragment();
        setPage(currentFragment);
    }

    public void setAddGroupPage() {
        currentPage = ADD_GROUP_PAGE;
        resetAllButton();
        addGroupButton.setAlpha(highlightAlpha);
        toast.cancel();
        currentFragment = new AddGroupFragment();
        setPage(currentFragment);
    }

    public void setSettingPage() {
        currentPage = SETTING_PAGE;
        resetAllButton();
        settingButton.setAlpha(highlightAlpha);
        toast.cancel();
        currentFragment = new SettingFragment();
        setPage(currentFragment);
    }

    public void setGroupMemberPage(GroupObject group) {
        currentPage = GROUP_MEMBER_PAGE;
        toast.cancel();
        // create a new group fragment and assign the target group
        GroupMemberFragment groupMemberFragment = new GroupMemberFragment();
        groupMemberFragment.setGroup(group);
        currentFragment = groupMemberFragment;
        setPage(groupMemberFragment);
    }


    private void setDefaultPage() {
        setMapPage();
    }

    private void resetAllButton() {
        mapButton.setAlpha(defaultButtonAlpha);
        addGroupButton.setAlpha(defaultButtonAlpha);
        myGroupButton.setAlpha(defaultButtonAlpha);
        settingButton.setAlpha(defaultButtonAlpha);
    }

    // main window fragment swap
    private void setPage(Fragment pageFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_window, pageFragment);
        fragmentTransaction.commit();
    }

    public void makeToast(String input) {
        toast.setText(input);
        toast.show();
    }
}
