package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.fragments.ListViewFragment;

/**
 * Created by aditya on 8/17/17.
 */
public class PlaceResultsActivity extends BaseFragmentActivity {

    @Override
    Fragment createFragment() {
        return ListViewFragment.newInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_friends:
                Intent i = new Intent(getApplication(),MapsActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                return true;
            case R.id.action_view_map:
                Intent intent = new Intent(getApplication(),MapViewActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                return true;

        }

        return true;
    }

}
