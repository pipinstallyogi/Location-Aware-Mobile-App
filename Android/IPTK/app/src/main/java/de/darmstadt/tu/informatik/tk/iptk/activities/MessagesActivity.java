package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import de.darmstadt.tu.informatik.tk.iptk.fragments.MessageFragment;

/**
 * The type Messages activity.
 */
public class MessagesActivity extends BaseFragmentActivity {

    /**
     * The constant EXTRA_FRIEND_DETAILS.
     */
    public static final String EXTRA_FRIEND_DETAILS = "EXTRA_FRIENDS_DETAILS";

    @Override
    Fragment createFragment() {
        ArrayList<String> friendDetails = getIntent().getStringArrayListExtra(EXTRA_FRIEND_DETAILS);

        getSupportActionBar().setTitle(friendDetails.get(2));

        return MessageFragment.newInstance(friendDetails);
    }

    /**
     * New instance intent.
     *
     * @param context       the context
     * @param friendDetails the friend details
     * @return the intent
     */
    public static Intent newInstance(Context context,ArrayList<String>friendDetails){
        Intent intent = new Intent(context,MessagesActivity.class);
        intent.putStringArrayListExtra(EXTRA_FRIEND_DETAILS,friendDetails);
        return intent;
    }
}
