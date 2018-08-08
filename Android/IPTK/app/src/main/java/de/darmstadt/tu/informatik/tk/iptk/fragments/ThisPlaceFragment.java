package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.services.LivePlacesServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.views.PlacesViewPageAdapter;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * The type This place fragment.
 */
public class ThisPlaceFragment extends BaseFragment {

    /**
     * The M place name.
     */
    @BindView(R2.id.fragment_this_place_PlaceName)
    TextView mPlaceName;

    /**
     * The M place address.
     */
    @BindView(R2.id.fragment_this_place_PlaceAddress)
    TextView mPlaceAddress;

    /**
     * The M place phone.
     */
    @BindView(R2.id.fragment_this_place_PlacePhone)
    TextView mPlacePhone;

    /**
     * The M place website.
     */
    @BindView(R2.id.fragment_this_place_PlacewWebsite)
    TextView mPlaceWebsite;

    /**
     * The M place id.
     */
    @BindView(R2.id.fragment_this_place_Placeid)
    TextView mPlaceId;

    /**
     * The M place king.
     */
    @BindView(R2.id.fragment_this_place_PlaceKing)
    TextView mPlaceKing;

    /**
     * The M checkin button.
     */
    @BindView(R2.id.fragment_this_place_ImageButton)
    FloatingActionButton mCheckinButton;

    /**
     * The M tab layout.
     */
    @BindView(R2.id.fragment_this_place_tabLayout)
    TabLayout mTabLayout;

    /**
     * The M view pager.
     */
    @BindView(R2.id.fragment_this_place_viewPager)
    ViewPager mViewPager;

    private Socket mSocket;

    private BaseFragmentActivity mActivity;

    private LivePlacesServices mLivePlacesServices;

    private ArrayList<String> mplaceArrayList;

    private Unbinder mUnbinder;

    private String json;

    private List<String> CheckinList;

    /**
     * New instance this place fragment.
     *
     * @return the this place fragment
     */
    public static ThisPlaceFragment newInstance() {
        return new ThisPlaceFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        json = mSharedPreferences_place.getString("data","");
        try {
            mSocket = IO.socket(Constants.IP_HOST);
            Log.d("This pLace Socket",mSocket.toString());
        } catch (URISyntaxException e) {
            Log.i(ListViewFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();
        mLivePlacesServices = LivePlacesServices.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_this_place,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        getData();
        mPlaceName.setText(mplaceArrayList.get(0));
        mPlaceAddress.setText(mplaceArrayList.get(1));
        mPlaceId.setText(mplaceArrayList.get(2));
        if (mplaceArrayList.get(3)==null){
            mPlacePhone.setVisibility(View.GONE);
        }
        else
        {
            mPlacePhone.setText(mplaceArrayList.get(3).replaceAll("\\s",""));
        }
        if (mplaceArrayList.get(4)==null){
            mPlaceWebsite.setVisibility(View.GONE);
        }
        else
        {
            mPlaceWebsite.setText(mplaceArrayList.get(4));
        }

        PlacesViewPageAdapter placesViewPageAdapter = new PlacesViewPageAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(placesViewPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference checkInsRef = dbRef.child("usersCheckIns");
        DatabaseReference placeCheckinRef = checkInsRef.child(mplaceArrayList.get(2));
        placeCheckinRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CheckinList = new ArrayList<>();
                if (dataSnapshot.getValue()!=null){
                for (DataSnapshot dsp : dataSnapshot.getChildren()){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("count",dsp.child("count").getValue());
                        jsonObject.put("name",dsp.child("userName").getValue());
                        Log.d("checkin time",dsp.child("latestCheckin").child("data").getValue().toString());
                        CheckinList.add(jsonObject.toString());
                        Log.d("json print",jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    int count = 0;
                    String name;
                    for (String s : CheckinList) {
                        try {

                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("count") >= count)
                            {
                                count = jsonObject.getInt("count");
                                name = jsonObject.getString("name");
                                mPlaceKing.setText("King of this place is "+name);
                                Log.d("king",name);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                else
                {
                    mPlaceKing.setText("No King Yet");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }


    /**
     * Sets checkin button.
     */
    @OnClick(R2.id.fragment_this_place_ImageButton)
    public void setmCheckinButton() {
        String place_id = mplaceArrayList.get(2);
        String email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        String picture = mSharedPreferences.getString(Constants.USER_PICTURE,"");
        String name = mSharedPreferences.getString(Constants.USER_NAME,"");
        Log.d("mcd",mCompositeDisposable.toString());
        Log.d("socket",mSocket.toString());
        Log.d("email",email);
        Log.d("picture",picture);
        Log.d("name",name);
        Log.d("placeid",place_id);
        mCompositeDisposable.add(mLivePlacesServices.checkInUser(place_id,email,name,picture,mSocket));
    }


    /**
     * Load json from intent string.
     *
     * @return the string
     */
    public String loadJSONFromIntent() {
        return json;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<String> getData() {

        Log.d("my data", loadJSONFromIntent());
        try {
            JSONObject jsonResonse = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            JSONObject jsonMainNode = jsonResonse.optJSONObject("result");

                Log.d("name", jsonMainNode.getString("name"));
                String name = jsonMainNode.getString("name");
                String formatted_address = jsonMainNode.getString("formatted_address");
                String id = jsonMainNode.getString("place_id");
                String phone;
                String website;

            if (jsonMainNode.has("formatted_phone_number")){
                phone = jsonMainNode.getString("formatted_phone_number");
            }
            else {
                phone = "";
            }
            if (jsonMainNode.has("website")){
                website = jsonMainNode.getString("website");
            }else {
                website = "";
            }
            mplaceArrayList = new ArrayList<>();
            mplaceArrayList.clear();
            mplaceArrayList.add(name);
            mplaceArrayList.add(formatted_address);
            mplaceArrayList.add(id);
            mplaceArrayList.add(phone);
            mplaceArrayList.add(website);


        } catch (JSONException e) {
            Toast.makeText(getContext(), "error ..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return mplaceArrayList;
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


    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
