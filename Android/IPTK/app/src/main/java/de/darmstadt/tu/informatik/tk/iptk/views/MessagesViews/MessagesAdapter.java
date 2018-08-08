package de.darmstadt.tu.informatik.tk.iptk.views.MessagesViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.Message;

/**
 * The type Messages adapter.
 */
public class MessagesAdapter extends RecyclerView.Adapter {

    private BaseFragmentActivity mActivity;
    private List<Message> mMessages;
    private LayoutInflater mInflator;
    private String mCurrentUserEmail;

    /**
     * Instantiates a new Messages adapter.
     *
     * @param mActivity         the m activity
     * @param mCurrentUserEmail the m current user email
     */
    public MessagesAdapter(BaseFragmentActivity mActivity, String mCurrentUserEmail) {
        this.mActivity = mActivity;
        this.mCurrentUserEmail = mCurrentUserEmail;
        mInflator = mActivity.getLayoutInflater();
        mMessages = new ArrayList<>();
    }

    /**
     * Sets messages.
     *
     * @param messages the messages
     */
    public void setmMessages(List<Message> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    /**
     * Gets messages.
     *
     * @return the messages
     */
    public List<Message> getmMessages() {
        return mMessages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_messages,parent,false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MessagesViewHolder) holder).populate(mActivity,mMessages.get(position),mCurrentUserEmail);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
