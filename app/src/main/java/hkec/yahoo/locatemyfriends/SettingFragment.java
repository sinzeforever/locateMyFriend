package hkec.yahoo.locatemyfriends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.LocationSyncroner;

/**
 * Created by sinze on 3/10/15.
 */
public class SettingFragment extends BaseFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting, container, false);
    }
    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {

    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {

    }
}
