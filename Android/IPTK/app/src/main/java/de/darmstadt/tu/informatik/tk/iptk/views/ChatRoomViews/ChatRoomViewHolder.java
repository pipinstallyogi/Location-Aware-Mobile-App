package de.darmstadt.tu.informatik.tk.iptk.views.ChatRoomViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.entities.ChatRoom;

/**
 * The type Chat room view holder.
 */
public class ChatRoomViewHolder extends RecyclerView.ViewHolder{

    /**
     * The M last message.
     */
    @BindView(R2.id.list_chat_room_lastMessage)
    TextView mLastMessage;

    /**
     * The M last message indicator.
     */
    @BindView(R2.id.list_chat_room_newMessageIndicator)
    ImageView mLastMessageIndicator;

    /**
     * The M user name.
     */
    @BindView(R2.id.list_chat_room_userName)
    TextView mUserName;

    /**
     * The M user picture.
     */
    @BindView(R2.id.list_chat_room_userPicture)
    ImageView mUserPicture;

    /**
     * Instantiates a new Chat room view holder.
     *
     * @param itemView the item view
     */
    public ChatRoomViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    /**
     * Populate.
     *
     * @param context          the context
     * @param chatRoom         the chat room
     * @param currentUserEmail the current user email
     */
    public void populate(Context context, ChatRoom chatRoom, String currentUserEmail){
        itemView.setTag(chatRoom);

        Picasso.with(context)
                .load(chatRoom.getFriendPicture())
                .into(mUserPicture);

        mUserName.setText(chatRoom.getFriendName());

        String lastMessageSent = chatRoom.getLastMessage();

        if (lastMessageSent.length()>40){
            lastMessageSent = lastMessageSent.substring(0,40) + " ...";
        }

        if (!chatRoom.isSentLastMessage()){
            lastMessageSent = lastMessageSent + " (Draft)";
        }

        if (chatRoom.getLastMessageSenderEmail().equals(currentUserEmail)){
            lastMessageSent = "Me: " + lastMessageSent;
        }

        if (!chatRoom.isLastMessageRead()){
            mLastMessageIndicator.setVisibility(View.VISIBLE);
        } else{
            mLastMessageIndicator.setVisibility(View.GONE);
        }



        mLastMessage.setText(lastMessageSent);



    }
}
