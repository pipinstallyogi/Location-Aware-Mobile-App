package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.Results;
import de.darmstadt.tu.informatik.tk.iptk.services.LivePlacesServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import de.darmstadt.tu.informatik.tk.iptk.views.SearchResultsViews.PlaceResultsViewAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by aditya on 8/21/17.
 */
public class ListViewFragment extends BaseFragment {

    /**
     * The M recycler view.
     */
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private Socket mSocket;

    private BaseFragmentActivity mActivity;

    private LivePlacesServices mLivePlacesServices;

    private PlaceResultsViewAdapter mAdapter;

    private ArrayList<Results> mResultsArrayList;

    private String json;

    /**
     * The Results.
     */
    Results results;

    /**
     * New instance list view fragment.
     *
     * @return the list view fragment
     */
    public static ListViewFragment newInstance(){
        return new ListViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(Constants.IP_HOST);
        } catch (URISyntaxException e) {
            Log.i(ListViewFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();
        mLivePlacesServices = LivePlacesServices.getInstance();
        mSocket.on("response",placeResponse());

        json = mSharedPreferences_map.getString(Constants.MAP_DATA,"");
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_search_results,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        mCompositeDisposable = new CompositeDisposable();
        initRecyclerView();
        loadJSON();
        return rootView;
    }


    private Emitter.Listener placeResponse(){
        return  new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                mCompositeDisposable.add(mLivePlacesServices.placeDetailsResponse(data,mSharedPreferences_place,mActivity));
            }
        };
    }


    /**
     * Load json from intent string.
     *
     * @return the string
     */
    public String loadJSONFromIntent() {
        return json;
    }

    /**
     * Gets result list.
     *
     * @return the result list
     */
    public List<Results> getResultList() {

        Log.d("my data", loadJSONFromIntent());
        try {
            JSONObject jsonResonse = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            JSONArray jsonMainNode = jsonResonse.optJSONArray("results");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject c = jsonMainNode.getJSONObject(i);
                Log.d("name", c.getString("name"));
                String name = c.getString("name");
                String formatted_address = c.getString("formatted_address");
                String id = c.getString("place_id");
                String icon = c.getString("icon");

                results = new Results(name, id, formatted_address, icon);
                mResultsArrayList.add(results);
            }

        } catch (JSONException e) {
            Toast.makeText(getContext(), "error ..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return mResultsArrayList;
    }

    private void loadJSON() {
        mResultsArrayList = new ArrayList<>();
        Observable<List<Results>> newObservable = Observable.just(getResultList());


        mCompositeDisposable.add(newObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Results>>() {
                    @Override
                    public void onNext(@NonNull List<Results> results) {
                        mAdapter = new PlaceResultsViewAdapter(mResultsArrayList,getContext(), new PlaceResultsViewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View itemView, int position) {
                                //Toast.makeText(getContext(),mResultsArrayList.get(position).getPlaceId(),Toast.LENGTH_SHORT).show();
                                mSharedPreferences_place.edit().putString("placeid",mResultsArrayList.get(position).getPlaceId()).apply();
                                mCompositeDisposable.add(mLivePlacesServices.sendPlaceDetailsRequest(mResultsArrayList.get(position).getPlaceId(),mSocket));
                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable error) {
                        handleError(error);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }



    private void handleError(Throwable error) {
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
