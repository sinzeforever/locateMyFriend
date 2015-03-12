package hkec.yahoo.locatemyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.LocationSyncroner;

/**
 * Created by sinze on 3/10/15.
 */
public class SettingFragment extends BaseFragment{

    private View rootView;
    private TextView userName;
    private Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.setting, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUser();
        setLogoutButton();
    }

    public void setUser() {
        userName = (TextView) rootView.findViewById(R.id.settingUserName);
        userName.setText(UserProfile.getInstance().id);
    }

    public void setLogoutButton() {
        logoutButton = (Button) rootView.findViewById(R.id.settingLogout);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doLogout();
            }
        });
    }

    public void doLogout() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {

    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {

    }
}
