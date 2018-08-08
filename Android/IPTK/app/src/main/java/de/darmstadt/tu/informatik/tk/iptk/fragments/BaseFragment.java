package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.activities.FriendActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.InboxActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.ProfileActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.SearchActivity;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import io.reactivex.disposables.CompositeDisposable;


/**
 * The type Base fragment.
 */
public class BaseFragment extends Fragment {

    /**
     * The M composite disposable.
     */
    protected CompositeDisposable mCompositeDisposable;

    /**
     * The M shared preferences.
     */
    protected SharedPreferences mSharedPreferences, /**
     * The M shared preferences map.
     */
    mSharedPreferences_map, /**
     * The M shared preferences place.
     */
    mSharedPreferences_place;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
        mSharedPreferences = getActivity().getSharedPreferences(Constants.USER_INFO_PREFERENCE,
                Context.MODE_PRIVATE);
        mSharedPreferences_map = getActivity().getSharedPreferences(Constants.MAP_INFO_PREFERENCE,Context.MODE_PRIVATE);
        mSharedPreferences_place = getActivity().getSharedPreferences(Constants.PLACE_INFO_PREFERENCE,Context.MODE_PRIVATE);

    }


    /**
     * Set up bottom bar.
     *
     * @param bottomBar the bottom bar
     * @param index     the index
     */
    public void setUpBottomBar(BottomBar bottomBar, final int index){
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (index){
                    case 1:
                        if (tabId == R.id.tab_messages){
                            Intent intent = new Intent(getActivity(), InboxActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_profile){
                            Intent intent = new Intent(getActivity(),ProfileActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_friends){
                            Intent intent = new Intent(getActivity(),FriendActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        break;
                    case 2:
                        if (tabId == R.id.tab_profile){
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_friends){
                            Intent intent = new Intent(getActivity(), FriendActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_search){
                            Intent intent = new Intent(getActivity(),SearchActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        break;
                    case 3:
                        if (tabId == R.id.tab_messages){
                            Intent intent = new Intent(getActivity(), InboxActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_profile){
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_search){
                            Intent intent = new Intent(getActivity(),SearchActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        break;
                    case 4:
                        if (tabId == R.id.tab_messages){
                            Intent intent = new Intent(getActivity(), InboxActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_friends){
                            Intent intent = new Intent(getActivity(), FriendActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if (tabId == R.id.tab_search){
                            Intent intent = new Intent(getActivity(),SearchActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        break;
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
