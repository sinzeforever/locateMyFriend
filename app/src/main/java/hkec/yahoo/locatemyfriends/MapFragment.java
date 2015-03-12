package hkec.yahoo.locatemyfriends;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.LocationSyncroner;

/**
 * Created by sinze on 3/10/15.
 */
public class MapFragment extends BaseFragment implements LocationListener {

    private static View rootView;

    private GoogleMap mMap;
    private Map<String, Marker> mMarker = new HashMap<String, Marker>();

    Double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpMapIfNeeded();
    }

    @Override
    public void onResume() {
        try {
            LocationSyncroner.init(this, getActivity(), UserProfile.getInstance().id);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
        LocationSyncroner.reset();
        super.onPause();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) ((FragmentActivity) getActivity()).getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        // Enable MyLocation Layer of Google Map
        //mMap.setMyLocationEnabled(true); // blue dot
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // set map type

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        //Criteria criteria = new Criteria();

        // Get the name of the best provider
        //String provider = locationManager.getBestProvider(criteria, true);
        //Location myLocation = locationManager.getLastKnownLocation(provider);

        // Get Current Location
        //Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        latitude = myLocation.getLatitude();
        longitude = myLocation.getLongitude();

        // 把自己加進去
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green))
                .title("Suzy"));
        mMarker.put("Suzy", marker);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);

    }

    @Override
    public void onLocationChanged(Location myLocation) {    //當地點改變時
        // TODO Auto-generated method stub

        // 更新自己所在位置
        latitude = latitude + 0.0005;
        longitude = longitude + 0.0001;

        //Double latitude = myLocation.getLatitude();
        //Double longitude = myLocation.getLongitude();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast.makeText(getActivity(), "Time: " + currentDateTimeString + " Place: (" + latitude + "," + longitude + ")", Toast.LENGTH_LONG).show();

        try {
            LocationSyncroner.updateMyLocation(UserProfile.getInstance().id, String.valueOf(latitude), String.valueOf(longitude));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String arg0) {    //當GPS或網路定位功能關閉時
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String arg0) {    //當GPS或網路定位功能開啟
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {    //定位狀態改變
        //status=OUT_OF_SERVICE 供應商停止服務
        //status=TEMPORARILY_UNAVAILABLE 供應商暫停服務
    }

    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {
        Log.e("return data", dma.data.toString());
    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {
        Log.e("return data member", le.member);
        Log.e("return data lat", le.newLat);
        Log.e("return data lng", le.newLng);

        Marker marker;
        if (mMarker.containsKey(le.member)) {
            // update marker
            marker = mMarker.get(le.member);
            marker.setPosition(new LatLng(Double.parseDouble(le.newLat), Double.parseDouble(le.newLng)));
        } else {
            // add marker
            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(le.newLat), Double.parseDouble(le.newLng)))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green))
                    .title(le.member));
            mMarker.put(le.member, marker);
        }
    }

}
