package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.support.v4.app.Fragment;

import de.darmstadt.tu.informatik.tk.iptk.fragments.ProfileFragment;

/**
 * The type Profile activity.
 */
public class ProfileActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return ProfileFragment.newInstance();
    }
}
