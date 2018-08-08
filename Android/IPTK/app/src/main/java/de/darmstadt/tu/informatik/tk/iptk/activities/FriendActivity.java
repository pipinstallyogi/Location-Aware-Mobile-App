package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.support.v4.app.Fragment;

import de.darmstadt.tu.informatik.tk.iptk.fragments.FriendFragment;

/**
 * The type Friend activity.
 */
public class FriendActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return FriendFragment.newInstance();
    }
}
