package de.darmstadt.tu.informatik.tk.iptk.views.ChatRoomViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.ChatRoom;

/**
 * The type Chat room adapter.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter {

    private BaseFragmentActivity mActivity;
    private List<ChatRoom> mChatRooms;
    private LayoutInflater mInflator;
    private ChatRoomListener mListener;
    private String mCurrentUserEmailString;

    /**
     * Instantiates a new Chat room adapter.
     *
     * @param mActivity               the m activity
     * @param mListener               the m listener
     * @param mCurrentUserEmailString the m current user email string
     */
    public ChatRoomAdapter(BaseFragmentActivity mActivity, ChatRoomListener mListener, String mCurrentUserEmailString) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        this.mCurrentUserEmailString = mCurrentUserEmailString;
        mInflator = mActivity.getLayoutInflater();
        mChatRooms = new ArrayList<>();
    }

    /**
     * Sets chat rooms.
     *
     * @param chatRooms the chat rooms
     */
    public void setmChatRooms(List<ChatRoom> chatRooms) {
        mChatRooms.clear();
        mChatRooms.addAll(chatRooms);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_chat_room,parent,false);
        final ChatRoomViewHolder chatRoomViewHolder = new ChatRoomViewHolder(view);
        chatRoomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatRoom chatRoom =(ChatRoom) chatRoomViewHolder.itemView.getTag();
                mListener.OnChatRoomClicked(chatRoom);
            }
        });
        return chatRoomViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ChatRoomViewHolder) holder).populate(mActivity,mChatRooms.get(position),mCurrentUserEmailString);
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }

    /**
     * The interface Chat room listener.
     */
    public interface ChatRoomListener{
        /**
         * On chat room clicked.
         *
         * @param chatRoom the chat room
         */
        void OnChatRoomClicked(ChatRoom chatRoom);
    }
}
