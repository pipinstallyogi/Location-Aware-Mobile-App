package de.darmstadt.tu.informatik.tk.iptk.views.UserFriendViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.User;

/**
 * The type User friend adapter.
 */
public class UserFriendAdapter extends RecyclerView.Adapter {
    private BaseFragmentActivity mActivity;
    private List<User> mUsers;
    private LayoutInflater mInflator;
    private UserClickedListener mListener;

    /**
     * Instantiates a new User friend adapter.
     *
     * @param mActivity the m activity
     * @param mListener the m listener
     */
    public UserFriendAdapter(BaseFragmentActivity mActivity, UserClickedListener mListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        mInflator = mActivity.getLayoutInflater();
        mUsers = new ArrayList<>();
    }


    /**
     * Sets users.
     *
     * @param users the users
     */
    public void setmUsers(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_users_friends,parent,false);

        final UserFriendViewHolder userFriendViewHolder = new UserFriendViewHolder(view);
        userFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = (User) userFriendViewHolder.itemView.getTag();
                mListener.OnUserClicked(user);
            }
        });

        return userFriendViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserFriendViewHolder) holder).populate(mActivity,mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    /**
     * The interface User clicked listener.
     */
    public interface UserClickedListener{
        /**
         * On user clicked.
         *
         * @param user the user
         */
        void OnUserClicked(User user);
    }
}
