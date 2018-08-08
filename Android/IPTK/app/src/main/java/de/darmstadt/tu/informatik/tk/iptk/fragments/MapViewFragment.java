package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.services.LivePlacesServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.utilities.MarshMellowPermissions;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by aditya on 8/21/17.
 */
public class MapViewFragment extends BaseFragment {

    /**
     * The M map view.
     */
    MapView mMapView;

    private GoogleMap googleMap;

    private Socket mSocket;

    private MarshMellowPermissions mMarshMellowPermission;

    private LivePlacesServices mLivePlacesServices;

    private FusedLocationProviderClient mFusedLocationClient;

    private Location mLastLocation;

    private String json;


    /**
     * New instance map view fragment.
     *
     * @return the map view fragment
     */
    public static MapViewFragment newInstance(){
        return new MapViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MAP_INFO_PREFERENCE, Context.MODE_PRIVATE);
        json = sharedPreferences.getString(Constants.MAP_DATA,"");

        try {
            mSocket = IO.socket(Constants.IP_HOST);
        } catch (URISyntaxException e) {
            Log.i(ListViewFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }

        mLivePlacesServices = LivePlacesServices.getInstance();
        mSocket.connect();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastLocation = location;
                        }
                        }
                    });
        return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                mMap.setMyLocationEnabled(true);


                // For showing results move to my location button

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (!mMarshMellowPermission.checkPermissionForLocation()) {
                        mMarshMellowPermission.requestPermissionForCamera();
                    } else {
                        googleMap.setMyLocationEnabled(true);
                    }
                    return;
                }


                // For dropping results marker at results point on the Map
                LatLng location = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                addMarkers();
            }
        });

        return rootView;
    }


    /**
     * Add markers.
     */
    public void addMarkers() {

        Log.d("my data", json);
        try {
            JSONObject jsonResonse = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            JSONArray jsonMainNode = jsonResonse.optJSONArray("results");
            HashMap<LatLng, String> hmap = new HashMap<LatLng, String>();

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject c = jsonMainNode.getJSONObject(i);
                String name = c.getString("name");
                String place_id = c.getString("place_id");
                Double lat = Double.parseDouble(c.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                Double lng = Double.parseDouble(c.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                LatLng pos = new LatLng(lat,lng);
                hmap.put(pos,place_id);
                final Marker marker =  googleMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(name));

                final ValueAnimator va = ValueAnimator.ofFloat(10, 1);
                va.setDuration(3000);
                va.setInterpolator(new BounceInterpolator());
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        marker.setAnchor(0f, (Float) animation.getAnimatedValue());
                    }
                });
                va.start();


            }

        } catch (JSONException e) {
            Toast.makeText(getContext(), "error ..." + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        mSocket.disconnect();
    }
}