package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.activities.UserProfileActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.Checkin;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.views.PlaceCheckinViews.CheckinsViewAdapter;
import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * The type Checkins fragment.
 */
public class CheckinsFragment extends BaseFragment {

    private TextView mNoCheckinsTextView;

    /**
     * The M recycler view.
     */
    private RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private Socket mSocket;

    private CheckinsViewAdapter mAdapter;

    /**
     * The M place id string.
     */
    String mPlaceIdString;


    /**
     * The User ckeckin list.
     */
    ArrayList<Checkin> userCkeckinList;

    /**
     * The Checkin.
     */
    Checkin checkin;

    /**
     * New instance checkins fragment.
     *
     * @return the checkins fragment
     */
    public static CheckinsFragment newInstance(){
        return new CheckinsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaceIdString = mSharedPreferences_place.getString("placeid","");
        try {
            mSocket = IO.socket(Constants.IP_HOST);
        } catch (URISyntaxException e) {
            Log.i(LoginFragment.class.getSimpleName(), e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }


        mSocket.connect();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkins,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        mNoCheckinsTextView = (TextView) rootView.findViewById(R.id.fragment_checkins_noCheckins);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_checkins_recyclerView);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference checkInsRef = dbRef.child("usersCheckIns");
        DatabaseReference placeCheckinRef = checkInsRef.child(mPlaceIdString);

        placeCheckinRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userCkeckinList = new ArrayList<>();
                if (dataSnapshot.getValue() != null){
                    mNoCheckinsTextView.setVisibility(View.GONE);
                    for (DataSnapshot dsp : dataSnapshot.getChildren()){
                        String name = dsp.child("userName").getValue().toString();
                        String email = dsp.child("email").getValue().toString();
                        String picture = dsp.child("userPicture").getValue().toString();
                        Long temp_time = Long.parseLong(dsp.child("latestCheckin").child("data").getValue().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                        Date resultDate = new Date(temp_time);
                        String latestCheckin = sdf.format(resultDate).toString();

                        checkin = new Checkin(picture,name,email,latestCheckin);
                        userCkeckinList.add(checkin);
                        Log.d("email checin",userCkeckinList.get(0).getEmail());

                    }
                    initRecyclerView();
                    mAdapter = new CheckinsViewAdapter(userCkeckinList,getContext(), new CheckinsViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            //Toast.makeText(getContext(),userCkeckinList.get(position).getEmail(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(),UserProfileActivity.class);
                            intent.putExtra("name",userCkeckinList.get(position).getUserName());
                            intent.putExtra("email",userCkeckinList.get(position).getEmail());
                            intent.putExtra("picture",userCkeckinList.get(position).getUserPicture());
                            startActivity(intent);

                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);


                }
                else{

                    mRecyclerView.setVisibility(View.GONE);
                    mNoCheckinsTextView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

          return rootView;
    }




    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
