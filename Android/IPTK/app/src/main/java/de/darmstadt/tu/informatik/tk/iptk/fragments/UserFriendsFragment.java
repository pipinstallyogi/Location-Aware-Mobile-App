package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.MessagesActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.User;
import de.darmstadt.tu.informatik.tk.iptk.services.LiveFriendsServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.views.UserFriendViews.UserFriendAdapter;

/**
 * The type User friends fragment.
 */
public class UserFriendsFragment extends BaseFragment implements UserFriendAdapter.UserClickedListener {

    /**
     * The M recycler view.
     */
    @BindView(R2.id.fragment_user_friends_reyclerView)
    RecyclerView mRecyclerView;

    /**
     * The M text view.
     */
    @BindView(R2.id.fragment_user_friends_message)
    TextView mTextView;

    private LiveFriendsServices mLiveFriendServices;
    private String mUserEmailString;

    private DatabaseReference mGetAllCurrenUsersFriendsReference;
    private ValueEventListener mGetAllCurrentUsersFriendsListener;

    private Unbinder mUnbinder;

    /**
     * New instance user friends fragment.
     *
     * @return the user friends fragment
     */
    public static UserFriendsFragment newInstance(){
        return new UserFriendsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLiveFriendServices = LiveFriendsServices.getInstance();
        mUserEmailString = mSharedPreferences.getString(Constants.USER_EMAIL,"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_friends,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);


        UserFriendAdapter adapter = new UserFriendAdapter((BaseFragmentActivity) getActivity(),this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mGetAllCurrenUsersFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIRE_BASE_PATH_USER_FRIENDS).child(Constants.encodeEmail(mUserEmailString));

        mGetAllCurrentUsersFriendsListener = mLiveFriendServices.getAllFriends(mRecyclerView,adapter,mTextView);

        mGetAllCurrenUsersFriendsReference.addValueEventListener(mGetAllCurrentUsersFriendsListener);

        mRecyclerView.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if (mGetAllCurrentUsersFriendsListener!=null){
            mGetAllCurrenUsersFriendsReference.removeEventListener(mGetAllCurrentUsersFriendsListener);
        }
    }

    @Override
    public void OnUserClicked(User user) {
        ArrayList<String> friendDetails = new ArrayList<>();
        friendDetails.add(user.getEmail());
        friendDetails.add(user.getUserPicture());
        friendDetails.add(user.getUserName());
        Intent intent = MessagesActivity.newInstance(getActivity(),friendDetails);

        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);

    }
}
