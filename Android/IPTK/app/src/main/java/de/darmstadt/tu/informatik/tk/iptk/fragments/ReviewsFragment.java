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
import android.widget.EditText;
import android.widget.ImageButton;
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
import de.darmstadt.tu.informatik.tk.iptk.entities.Reviews;
import de.darmstadt.tu.informatik.tk.iptk.services.LivePlacesServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.views.PlaceReviewViews.PlaceReviewViewAdapter;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by aditya on 8/18/17.
 */
public class ReviewsFragment extends BaseFragment {


    private TextView mTextView;

    private RecyclerView mRecyclerView;

    private EditText mEditText;

    private ImageButton mImageButton;

    private LivePlacesServices mLivePlacesServices;

    /**
     * The Name.
     */
    String name, /**
     * The Email.
     */
    email, /**
     * The Placeid.
     */
    placeid, /**
     * The Photo.
     */
    photo;

    /**
     * The User reviews list.
     */
    ArrayList<Reviews> userReviewsList;

    /**
     * The Reviews.
     */
    Reviews reviews;

    private Socket mSocket;

    private Unbinder mUnbinder;

    private PlaceReviewViewAdapter mAdapter;

    /**
     * New instance reviews fragment.
     *
     * @return the reviews fragment
     */
    public static ReviewsFragment newInstance(){
        return new ReviewsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(Constants.IP_HOST);
            Log.d("review Socket",mSocket.toString());
        } catch (URISyntaxException e) {
            Log.i(LoginFragment.class.getSimpleName(), e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }

         name =mSharedPreferences.getString(Constants.USER_NAME,"");
         email = mSharedPreferences.getString(Constants.USER_EMAIL,"");
          photo = mSharedPreferences.getString(Constants.USER_PICTURE,"");
         placeid = mSharedPreferences_place.getString("placeid","");

        mSocket.connect();
        mLivePlacesServices = LivePlacesServices.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        mTextView = (TextView) rootView.findViewById(R.id.fragment_reviews_NoReviewsTextView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_reviews_RecyclerView);
        mEditText = (EditText) rootView.findViewById(R.id.fragment_reviews_ReviewEditText);
        mImageButton = (ImageButton) rootView.findViewById(R.id.fragment_reviews_SendReviewImageButton);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reviewsRef = dbRef.child("placeReviews");
        DatabaseReference placeReviewRef = reviewsRef.child(placeid);

        placeReviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userReviewsList = new ArrayList<>();
                if (dataSnapshot.getValue()!= null){
                    for (DataSnapshot dsp: dataSnapshot.getChildren()){
                        String userName = dsp.child("userName").getValue().toString();
                        String userEmail = dsp.child("email").getValue().toString();
                        String picture = dsp.child("userPicture").getValue().toString();
                        String review = dsp.child("review").getValue().toString();
                        Long temp_time = Long.parseLong(dsp.child("latestReviewDate").child("data").getValue().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                        Date resultDate = new Date(temp_time);
                        String time = sdf.format(resultDate).toString();

                        reviews = new Reviews(userEmail,time,review,userName,picture);
                        userReviewsList.add(reviews);
                    }
                    initRecyclerView();
                    mAdapter = new PlaceReviewViewAdapter(userReviewsList,getContext(), new PlaceReviewViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            //Toast.makeText(getContext(),userReviewsList.get(position).getEmail(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(),UserProfileActivity.class);
                            intent.putExtra("name",userReviewsList.get(position).getUserName());
                            intent.putExtra("email",userReviewsList.get(position).getEmail());
                            intent.putExtra("picture",userReviewsList.get(position).getUserPicture());
                            startActivity(intent);
                        }
                    });

                    mRecyclerView.setAdapter(mAdapter);
                }
                else{
                    mRecyclerView.setVisibility(View.GONE);
                    mTextView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("review check",name+" "+email+" "+photo+" "+placeid+" "+mEditText.getText().toString());
                mCompositeDisposable.add(mLivePlacesServices.insertReview(placeid,name,photo,email,mEditText,mSocket));
                mEditText.setText("");
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
