package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.fragments.MapViewFragment;

/**
 * The type Map view activity.
 */
public class MapViewActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return new MapViewFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_view_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_list:
                finish();
                return true;

        }

        return true;
    }

}
