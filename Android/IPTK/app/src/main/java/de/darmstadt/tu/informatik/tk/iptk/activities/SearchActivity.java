package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.support.v4.app.Fragment;

import de.darmstadt.tu.informatik.tk.iptk.fragments.SearchFragment;

/**
 * Created by aditya on 8/10/17.
 */
public class SearchActivity extends BaseFragmentActivity{

    @Override
    Fragment createFragment() {
        return SearchFragment.newInstance();
    }
}
