package de.darmstadt.tu.informatik.tk.iptk.activities;


import android.support.v4.app.Fragment;

import de.darmstadt.tu.informatik.tk.iptk.fragments.LoginFragment;

/**
 * The type Login activity.
 */
public class LoginActivity extends BaseFragmentActivity {

    @Override
    Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}
