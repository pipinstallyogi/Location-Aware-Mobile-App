package de.darmstadt.tu.informatik.tk.iptk.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.darmstadt.tu.informatik.tk.iptk.fragments.CheckinsFragment;
import de.darmstadt.tu.informatik.tk.iptk.fragments.PhotosFragment;
import de.darmstadt.tu.informatik.tk.iptk.fragments.ReviewsFragment;

/**
 * Created by aditya on 8/24/17.
 */
public class PlacesViewPageAdapter extends FragmentStatePagerAdapter {


    /**
     * Instantiates a new Places view page adapter.
     *
     * @param fm the fm
     */
    public PlacesViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment returnFragment;

        switch (position){
            case 0:
                returnFragment = CheckinsFragment.newInstance();
                break;
            case 1:
                returnFragment = PhotosFragment.newInstance();
                break;
            case 2:
                returnFragment = ReviewsFragment.newInstance();
                break;

            default:
                return null;
        }
        return returnFragment;

    }


    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title;

        switch (position){
            case 0:
                title = "Check-Ins";
                break;
            case 1:
                title = "Photos";
                break;
            case 2:
                title = "Reviews";
                break;
            default:
                return null;
        }

        return title;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
