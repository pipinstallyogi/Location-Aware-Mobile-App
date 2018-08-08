package de.darmstadt.tu.informatik.tk.iptk.views.MessagesViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.entities.Message;

/**
 * The type Messages view holder.
 */
public class MessagesViewHolder extends RecyclerView.ViewHolder {

    /**
     * The M friend picture.
     */
    @BindView(R2.id.list_messages_friendPicture)
    ImageView mFriendPicture;

    /**
     * The M user picture.
     */
    @BindView(R2.id.list_messages_userPicture)
    ImageView mUserPicture;

    /**
     * The M user text.
     */
    @BindView(R2.id.list_messages_UserText)
    TextView mUserText;

    /**
     * The M friend text.
     */
    @BindView(R2.id.list_messages_friendText)
    TextView mFriendText;

    /**
     * Instantiates a new Messages view holder.
     *
     * @param itemView the item view
     */
    public MessagesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }


    /**
     * Populate.
     *
     * @param context          the context
     * @param message          the message
     * @param currentUserEmail the current user email
     */
    public void populate(Context context, Message message,String currentUserEmail){
        if (!currentUserEmail.equals(message.getMessageSenderEmail())){
            mUserPicture.setVisibility(View.GONE);
            mUserText.setVisibility(View.GONE);
            mFriendPicture.setVisibility(View.VISIBLE);
            mFriendText.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(message.getMessageSenderPicture())
                    .into(mFriendPicture);
            mFriendText.setText(message.getMessageText());
        } else{
            mUserPicture.setVisibility(View.VISIBLE);
            mUserText.setVisibility(View.VISIBLE);
            mFriendPicture.setVisibility(View.GONE);
            mFriendText.setVisibility(View.GONE);

            Picasso.with(context)
                    .load(message.getMessageSenderPicture())
                    .into(mUserPicture);
            mUserText.setText(message.getMessageText());
        }
    }
}
