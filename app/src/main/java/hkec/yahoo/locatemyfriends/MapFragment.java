package hkec.yahoo.locatemyfriends;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import demo.android.jonaswu.yahoo.com.hackday_demo_lib.API;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.BaseFragment;
import demo.android.jonaswu.yahoo.com.hackday_demo_lib.LocationSyncroner;

import com.squareup.picasso.Picasso;


/**
 * Created by sinze on 3/10/15.
 */
public class MapFragment extends BaseFragment implements LocationListener {

    private static View rootView;

    private GoogleMap mMap;
    private Map<String, Marker> mMarker = new HashMap<String, Marker>();

    Double latitude, longitude;

    private String apiMethod = "";
    private static float previousZoomLevel = 15;

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
        // 重新連線
        try {
            LocationSyncroner.init(this, getActivity(), UserProfile.getInstance().id);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        setUpMapIfNeeded();

        // 重畫點點
        resetMarkers();
        super.onResume();
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
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition position) {
                    Log.d("Zoom", "Zoom: " + position.zoom);
                    previousZoomLevel = position.zoom;
                }
            });
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

        if (myLocation != null) {
            latitude = myLocation.getLatitude();
            longitude = myLocation.getLongitude();
            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(121.6212150797775, 25.056027960549997)));
        }
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(previousZoomLevel));

        //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
    }

    public void getGroupsFromMember() {
        this.apiMethod = "getGroupsFromMember";
        new API().getGroupsFromMember(getActivity(), UserProfile.getInstance().id, this.getEventBus());
    }

    public void getMembersFromGroups(String[] groups) {
        this.apiMethod = "getMembersFromGroups";
        new API().getMembersFromGroups(getActivity(), groups, this.getEventBus());
    }

    // 重設 marker
    public void resetMarkers() {
        // 清空地圖
        //mMap.clear();

        if (mMap != null)
            mMap.clear();
        mMarker.clear();
        //
        getGroupsFromMember();
    }

    // 新增 marker
    public void addMarker(String name, String lat, String lng, String imageUrl) {
        Marker marker;
        if (mMarker.containsKey(name)) {

            Log.e("######### update marker", name);

            // update marker
            marker = mMarker.get(name);
            marker.setPosition(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

        } else {

            Log.e("######### add marker", name);

            // add marker
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap(80, 80, conf);

            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green))
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                    .title(name));

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(getActivity()).load("https://s.yimg.com/qs/auc/hackday/userphoto/" + imageUrl + ".png").into(new PicassoMarker(marker));
            } else {
                //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green));
                Picasso.with(getActivity()).load("https://s.yimg.com/qs/auc/hackday/userphoto/user9.png").into(new PicassoMarker(marker));
            }

            mMarker.put(name, marker);
        }
    }

    @Override
    public void onEventMainThread(API.ReturnDataEvent dma) {
        if (this.apiMethod == "getGroupsFromMember") {
            Log.e("return data [getGroupsFromMember]", dma.data.toString());

            try {
                JSONArray groupsArray = dma.data.getJSONArray("data");
                if (groupsArray.length() > 0) {
                    String[] groupStrArray = new String[groupsArray.length()];
                    for (int i = 0; i < groupsArray.length(); i++) {
                        JSONObject group = groupsArray.getJSONObject(i);
                        if (group.getBoolean("visible")) {
                            groupStrArray[i] = group.getString("name");
                        }

                    }
                    getMembersFromGroups(groupStrArray);
                }

                //String[] groups = {"group1", "group2", "group3"};
                //getMembersFromGroups(groups);
            } catch (Exception e) {

            }

        } else if (this.apiMethod == "getMembersFromGroups") {
            Log.e("return data [getMembersFromGroups]", dma.data.toString());

            try {
                JSONArray groupsArray = dma.data.getJSONArray("data");
                String[] groupStrArray = new String[groupsArray.length()];
                for (int i = 0; i < groupsArray.length(); i++) {


                    JSONObject group = groupsArray.getJSONObject(i);
                    //groupStrArray[i] = group.getString("name");

                    JSONArray membersArray = group.getJSONArray("members");
                    for (int j = 0; j < membersArray.length(); j++) {
                        JSONObject member = membersArray.getJSONObject(j);

                        addMarker(member.getString("name"), member.getString("lat"), member.getString("lng"), member.getString("imageUrl"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.e("return data", dma.data.toString());
        }
    }

    @Override
    public void onEventMainThread(LocationSyncroner.LocationEvent le) {
        //Log.e("return data member", le.member);
        //Log.e("return data lat", le.newLat);
        //Log.e("return data lng", le.newLng);

        addMarker(le.member, le.newLat, le.newLng, le.imageUrl);

    }

    @Override
    public void onLocationChanged(Location myLocation) {    //當地點改變時
        // TODO Auto-generated method stub

        // 更新自己所在位置
        //latitude = latitude + 0.0005;
        //longitude = longitude + 0.0001;

        Double latitude = myLocation.getLatitude();
        Double longitude = myLocation.getLongitude();

        try {
            LocationSyncroner.updateMyLocation(UserProfile.getInstance().id, String.valueOf(latitude), String.valueOf(longitude), "user0");
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
}
