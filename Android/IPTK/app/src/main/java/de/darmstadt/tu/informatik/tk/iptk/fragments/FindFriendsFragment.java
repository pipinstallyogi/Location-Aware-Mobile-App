package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.User;
import de.darmstadt.tu.informatik.tk.iptk.services.LiveFriendsServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.views.FindFriendsViews.FindFriendsAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * The type Find friends fragment.
 */
public class FindFriendsFragment extends BaseFragment implements FindFriendsAdapter.UserListener {

    /**
     * The M search bar et.
     */
    @BindView(R2.id.fragment_find_friends_searchBar)
    EditText mSearchBarEt;

    /**
     * The M recycler view.
     */
    @BindView(R2.id.fragment_find_friends_recyclerView)
    RecyclerView mRecyclerView;

    /**
     * The M text view.
     */
    @BindView(R2.id.fragment_find_friends_noResults)
    TextView mTextView;





    private Unbinder mUnbinder;


    private DatabaseReference mGetAllUsersReference;
    private ValueEventListener mGetAllUsersListener;


    private DatabaseReference mGetAllFriendRequestsSentReference;
    private ValueEventListener mGetAllFriendRequestsSentListener;


    private DatabaseReference mGetAllFriendRequestRecievedReference;
    private ValueEventListener mGetAllFriendRequestRecievedListener;

    private DatabaseReference mGetAllCurrentUsersFriendsReference;
    private ValueEventListener mGetAllCurrentUsersFriendsListener;




    private String mUserEmailString;
    private FindFriendsAdapter mAdapter;


    private List<User> mAllUsers;

    private LiveFriendsServices mLiveFriendsService;

    /**
     * The M friend requests sent map.
     */
    public HashMap<String,User> mFriendRequestsSentMap;


    private Socket mSocket;


    private PublishSubject<String> mSearchBarString;


    /**
     * New instance find friends fragment.
     *
     * @return the find friends fragment
     */
    public static FindFriendsFragment newInstance(){
        return new FindFriendsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSocket = IO.socket(Constants.IP_HOST);
        } catch (URISyntaxException e) {
            Log.i(RegisterFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();


        mUserEmailString = mSharedPreferences.getString(Constants.USER_EMAIL,"");
        mLiveFriendsService = LiveFriendsServices.getInstance();
        mFriendRequestsSentMap = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_friends,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        mAllUsers = new ArrayList<>();
        mAdapter = new FindFriendsAdapter((BaseFragmentActivity) getActivity(),this);

        mGetAllUsersListener = getAllUsers(mAdapter,mUserEmailString);
        mGetAllUsersReference= FirebaseDatabase.getInstance().getReference().child(Constants.FIRE_BASE_PATH_USERS);
        mGetAllUsersReference.addValueEventListener(mGetAllUsersListener);

        mGetAllFriendRequestsSentReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIRE_BASE_PATH_FRIEND_REQUEST_SENT)
                .child(Constants.encodeEmail(mUserEmailString));
        mGetAllFriendRequestsSentListener = mLiveFriendsService.getFriendRequestsSent(mAdapter,this);

        mGetAllFriendRequestRecievedListener = mLiveFriendsService.getFriendRequestsRecived(mAdapter);



        mGetAllFriendRequestRecievedReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED)
                .child(Constants.encodeEmail(mUserEmailString));

        mGetAllFriendRequestsSentReference.addValueEventListener(mGetAllFriendRequestsSentListener);

        mGetAllFriendRequestRecievedReference.addValueEventListener(mGetAllFriendRequestRecievedListener);


        mGetAllCurrentUsersFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIRE_BASE_PATH_USER_FRIENDS).child(Constants.encodeEmail(mUserEmailString));

        mGetAllCurrentUsersFriendsListener = mLiveFriendsService.getAllCurrentUsersFriendMap(mAdapter);
        mGetAllCurrentUsersFriendsReference.addValueEventListener(mGetAllCurrentUsersFriendsListener);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);


        mCompositeDisposable.add(createSearchBarSubscription());
        listenToSearchBar();

        return rootView;
    }

    private Disposable createSearchBarSubscription(){
        mSearchBarString = PublishSubject.create();
        return mSearchBarString
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<User>>() {
                    @Override
                    public List<User> apply(@NonNull String searchString) throws Exception {
                        return mLiveFriendsService.getMatchingUsers(mAllUsers,searchString);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<User>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<User> users) {
                        if (users.isEmpty()){
                            mTextView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        } else{
                            mTextView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        mAdapter.setmUsers(users);
                    }
                });
    }

    private void listenToSearchBar(){
        mSearchBarEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchBarString.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Sets friend requests sent map.
     *
     * @param friendRequestsSentMap the friend requests sent map
     */
    public void setmFriendRequestsSentMap(HashMap<String, User> friendRequestsSentMap) {
        mFriendRequestsSentMap.clear();
        mFriendRequestsSentMap.putAll(friendRequestsSentMap);
    }

    /**
     * Get all users value event listener.
     *
     * @param adapter           the adapter
     * @param currentUsersEmail the current users email
     * @return the value event listener
     */
    public ValueEventListener getAllUsers(final FindFriendsAdapter adapter, String currentUsersEmail){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAllUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (!user.getEmail().equals(mUserEmailString) && user.isHasLoggedIn()){
                        mAllUsers.add(user);
                    }
                }
                adapter.setmUsers(mAllUsers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Can't Load Users",Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if (mGetAllUsersListener !=null){
            mGetAllUsersReference.removeEventListener(mGetAllUsersListener);
        }

        if (mGetAllFriendRequestsSentListener!=null){
            mGetAllFriendRequestsSentReference.removeEventListener(mGetAllFriendRequestsSentListener);
        }

        if (mGetAllFriendRequestRecievedListener!=null){
            mGetAllFriendRequestRecievedReference.removeEventListener(mGetAllFriendRequestRecievedListener);
        }

        if(mGetAllCurrentUsersFriendsListener !=null){
            mGetAllCurrentUsersFriendsReference.removeEventListener(mGetAllCurrentUsersFriendsListener);
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    @Override
    public void OnUserClicked(User user) {
       if (Constants.isIncludedInMap(mFriendRequestsSentMap,user)){
           mGetAllFriendRequestsSentReference.child(Constants.encodeEmail(user.getEmail()))
                   .removeValue();

           mCompositeDisposable.add(mLiveFriendsService.addOrRemoveFriendRequest(mSocket,mUserEmailString,
                   user.getEmail(),"1"));
       } else{
           mGetAllFriendRequestsSentReference.child(Constants.encodeEmail(user.getEmail()))
                   .setValue(user);

           mCompositeDisposable.add(mLiveFriendsService.addOrRemoveFriendRequest(mSocket,mUserEmailString,
                   user.getEmail(),"0"));
       }
    }
}
