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
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.MessagesActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.ChatRoom;
import de.darmstadt.tu.informatik.tk.iptk.services.LiveFriendsServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.views.ChatRoomViews.ChatRoomAdapter;

/**
 * The type Inbox fragment.
 */
public class InboxFragment extends BaseFragment implements ChatRoomAdapter.ChatRoomListener {

    /**
     * The M bottom bar.
     */
    @BindView(R2.id.bottomBar)
    BottomBar mBottomBar;

    /**
     * The M recycler.
     */
    @BindView(R2.id.fragment_inbox_recyclerView)
    RecyclerView mRecycler;

    /**
     * The M text view.
     */
    @BindView(R2.id.fragment_inbox_message)
    TextView mTextView;

    private Unbinder mUnbinder;


    private LiveFriendsServices mLiveFriendsService;

    private DatabaseReference mAllFriendRequestsReference;
    private ValueEventListener mAllFriendRequestsListener;


    private DatabaseReference mUserChatRoomReference;
    private ValueEventListener mUserChatRoomListener;

    private String mUserEmailString;

    private DatabaseReference mUsersNewMessagesReference;
    private ValueEventListener mUsersNewMessagesListener;


    /**
     * New instance inbox fragment.
     *
     * @return the inbox fragment
     */
    public static InboxFragment newInstance(){
        return new InboxFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLiveFriendsService = LiveFriendsServices.getInstance();
        mUserEmailString = mSharedPreferences.getString(Constants.USER_EMAIL,"");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        mBottomBar.selectTabWithId(R.id.tab_messages);
        setUpBottomBar(mBottomBar,2);


        ChatRoomAdapter adapter = new ChatRoomAdapter((BaseFragmentActivity)getActivity(),this,mUserEmailString);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUserChatRoomReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIRE_BASE_PATH_USER_CHAT_ROOMS).child(Constants.encodeEmail(mUserEmailString));

        mUserChatRoomListener = mLiveFriendsService.getAllChatRooms(mRecycler,mTextView,adapter);

        mUserChatRoomReference.addValueEventListener(mUserChatRoomListener);


        mAllFriendRequestsReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED).child(Constants.encodeEmail(mUserEmailString));
        mAllFriendRequestsListener = mLiveFriendsService.getFriendRequestBottom(mBottomBar,R.id.tab_friends);
        mAllFriendRequestsReference.addValueEventListener(mAllFriendRequestsListener);



        mUsersNewMessagesReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIRE_BASE_PATH_USER_NEW_MESSAGES).child(Constants.encodeEmail(mUserEmailString));
        mUsersNewMessagesListener = mLiveFriendsService.getAllNewMessages(mBottomBar,R.id.tab_messages);

        mUsersNewMessagesReference.addValueEventListener(mUsersNewMessagesListener);

        mRecycler.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();


        if (mAllFriendRequestsListener!=null){
            mAllFriendRequestsReference.removeEventListener(mAllFriendRequestsListener);
        }


        if (mUserChatRoomListener !=null){
            mUserChatRoomReference.removeEventListener(mUserChatRoomListener);
        }


        if (mUsersNewMessagesListener!=null){
            mUsersNewMessagesReference.removeEventListener(mUsersNewMessagesListener);
        }
    }

    @Override
    public void OnChatRoomClicked(ChatRoom chatRoom) {
        ArrayList<String> friendDetails = new ArrayList<>();
        friendDetails.add(chatRoom.getFriendEmail());
        friendDetails.add(chatRoom.getFriendPicture());
        friendDetails.add(chatRoom.getFriendName());
        Intent intent = MessagesActivity.newInstance(getActivity(),friendDetails);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }
}
