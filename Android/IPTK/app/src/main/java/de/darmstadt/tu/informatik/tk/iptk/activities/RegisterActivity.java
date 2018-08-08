package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.support.v4.app.Fragment;

import de.darmstadt.tu.informatik.tk.iptk.fragments.RegisterFragment;

/**
 * The type Register activity.
 */
public class RegisterActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return RegisterFragment.newInstance();
    }
}
