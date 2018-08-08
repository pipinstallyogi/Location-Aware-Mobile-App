package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.support.v4.app.Fragment;

import de.darmstadt.tu.informatik.tk.iptk.fragments.ThisPlaceFragment;

/**
 * Created by aditya on 8/24/17.
 */
public class ThisPlaceActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return ThisPlaceFragment.newInstance();
    }
}
