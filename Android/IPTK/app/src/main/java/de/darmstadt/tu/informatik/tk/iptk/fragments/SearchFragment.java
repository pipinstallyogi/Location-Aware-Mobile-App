package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.roughike.bottombar.BottomBar;

import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.services.LivePlacesServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.utilities.MarshMellowPermissions;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by aditya on 8/10/17.
 */
public class SearchFragment extends BaseFragment {

    /**
     * The M breakfast.
     */
    @BindView(R2.id.fragment_search_breakfast)
    Button mBreakfast;

    /**
     * The M lunch.
     */
    @BindView(R2.id.fragment_search_lunch)
    Button mLunch;

    /**
     * The M dinner.
     */
    @BindView(R2.id.fragment_search_dinner)
    Button mDinner;

    /**
     * The M coffee.
     */
    @BindView(R2.id.fragment_search_coffee)
    Button mCoffee;

    /**
     * The M nightlife.
     */
    @BindView(R2.id.fragment_search_nightlife)
    Button mNightlife;

    /**
     * The M things to do.
     */
    @BindView(R2.id.fragment_search_thingsToDo)
    Button mThingsToDo;

    /**
     * The M search query et.
     */
    @BindView(R2.id.fragment_search_query_editText)
    EditText mSearchQueryEt;

    /**
     * The M search button.
     */
    @BindView(R2.id.fragment_search_searchButton)
    Button mSearchButton;

    /**
     * The M bottom bar.
     */
    @BindView(R2.id.bottomBar)
    BottomBar mBottomBar;

    private Unbinder mUnbinder;

    private Socket mSocket;

    private MarshMellowPermissions mMarshMellowPermission;


    private Location mLastLocation;
    private String mLatitude;
    private String mLongitude;
    private String mLocation;


    private BaseFragmentActivity mActivity;
    private LivePlacesServices mLivePlacesServices;

    private FusedLocationProviderClient mFusedLocationClient;


    /**
     * New instance search fragment.
     *
     * @return the search fragment
     */
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            mSocket = IO.socket(Constants.IP_HOST);
        } catch (URISyntaxException e) {
            Log.i(SearchFragment.class.getSimpleName(), e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();
        mLivePlacesServices = LivePlacesServices.getInstance();
        mSocket.on("response", searchResponse());

        mMarshMellowPermission = new MarshMellowPermissions((BaseFragmentActivity) getActivity());


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (!mMarshMellowPermission.checkPermissionForLocation()) {
            mMarshMellowPermission.requestPermissionForLocation();
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastLocation = location;
                                mLatitude = Double.toString(mLastLocation.getLatitude());
                                mLongitude = Double.toString(mLastLocation.getLongitude());
                                mLocation = "[" + mLatitude + "," + mLongitude + "]";
                                Log.d("Location", mLocation);

                            }
                        }
                    });
        }
            return;
        }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        mBottomBar.selectTabWithId(R.id.tab_search);
        setUpBottomBar(mBottomBar,1);
        return rootView;
    }

    private Emitter.Listener searchResponse(){
        return  new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                mCompositeDisposable.add(mLivePlacesServices.searchResponse(data,mActivity,mSharedPreferences_map));
            }
        };
    }

    /**
     * Setm breakfast.
     */
    @OnClick(R2.id.fragment_search_breakfast)
    public void setmBreakfast(){
        String latitude = mLatitude;
        String longitude = mLongitude;
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String uname = mSharedPreferences.getString(Constants.USER_NAME,"");
        mCompositeDisposable.add(mLivePlacesServices.sendButtonSearchQuery("Breakfast",latitude,longitude,email,uname,mSocket));
    }

    /**
     * Setm lunch.
     */
    @OnClick(R2.id.fragment_search_lunch)
    public void setmLunch(){
        String latitude = mLatitude;
        String longitude = mLongitude;
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String uname = mSharedPreferences.getString(Constants.USER_NAME,"");
        mCompositeDisposable.add(mLivePlacesServices.sendButtonSearchQuery("Lunch",latitude,longitude,email,uname,mSocket));
    }

    /**
     * Setm dinner.
     */
    @OnClick(R2.id.fragment_search_dinner)
    public void setmDinner(){
        String latitude = mLatitude;
        String longitude = mLongitude;
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String uname = mSharedPreferences.getString(Constants.USER_NAME,"");
        mCompositeDisposable.add(mLivePlacesServices.sendButtonSearchQuery("Dinner",latitude,longitude,email,uname,mSocket));
    }

    /**
     * Setm coffee.
     */
    @OnClick(R2.id.fragment_search_coffee)
    public void setmCoffee(){
        String latitude = mLatitude;
        String longitude = mLongitude;
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String uname = mSharedPreferences.getString(Constants.USER_NAME,"");
        mCompositeDisposable.add(mLivePlacesServices.sendButtonSearchQuery("Coffee",latitude,longitude,email,uname,mSocket));
    }

    /**
     * Setm nightlife.
     */
    @OnClick(R2.id.fragment_search_nightlife)
    public void setmNightlife(){
        String latitude = mLatitude;
        String longitude = mLongitude;
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String uname = mSharedPreferences.getString(Constants.USER_NAME,"");
        mCompositeDisposable.add(mLivePlacesServices.sendButtonSearchQuery("nightlife",latitude,longitude,email,uname,mSocket));
    }

    /**
     * Setm things to do.
     */
    @OnClick(R2.id.fragment_search_thingsToDo)
    public void setmThingsToDo(){
        String latitude = mLatitude;
        String longitude = mLongitude;
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String uname = mSharedPreferences.getString(Constants.USER_NAME,"");
        mCompositeDisposable.add(mLivePlacesServices.sendButtonSearchQuery("things to do",latitude,longitude,email,uname,mSocket));
    }

    /**
     * Setm search button.
     */
    @OnClick(R2.id.fragment_search_searchButton)
    public void setmSearchButton(){
        String latitude = mLatitude;
        String longitude = mLongitude;
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String uname = mSharedPreferences.getString(Constants.USER_NAME,"");
        mCompositeDisposable.add(mLivePlacesServices.sendTextSearchQuery(
                mSearchQueryEt,latitude,longitude,email,uname,mSocket));
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
