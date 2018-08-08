package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.entities.Photos;
import de.darmstadt.tu.informatik.tk.iptk.views.PhotosViews.PhotosViewsAdapter;

/**
 * Created by aditya on 8/18/17.
 */
public class PhotosFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    /**
     * This string stores the entire Json from the place results
     */
    String json;

    /**
     * The instance of Photos class.
     */
    Photos photos;

    /**
     * The adapter for recycler view.
     */
    PhotosViewsAdapter mAdapter;

    /**
     * The list to store photo elements extracted from json result.
     */
    ArrayList<Photos> mPhotoList;


    /**
     * New instance photos fragment.
     *
     * @return the photos fragment
     */
    public static PhotosFragment newInstance(){
        return new PhotosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        json = mSharedPreferences_place.getString("data","");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos,container,false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_photos_recyclerView);
        TextView tv = (TextView) rootView.findViewById(R.id.fragment_photos_textView);
        JSONObject jsonResonse = null;
        try {
            //prepare json object from string
            jsonResonse = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            JSONObject jsonMainNode = jsonResonse.optJSONObject("result");
            if (jsonMainNode.has("photos")) {
                //get required items from response
                JSONArray phots_array = jsonMainNode.getJSONArray("photos");
                mPhotoList = new ArrayList<>();

                for (int i = 0; i < phots_array.length(); i++) {
                    JSONObject c = phots_array.getJSONObject(i);
                    // get photo_reference from photo object
                    String photo = c.getString("photo_reference");
                    //make url to get pictures
                    String photourl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo + "&key=AIzaSyClCLJKqezNVRBfBGPgHFxV5rlT72_vgZU";

                    photos = new Photos(photourl);
                    mPhotoList.add(photos);

                    initRecyclerView();
                    mAdapter = new PhotosViewsAdapter(mPhotoList, getContext(), new PhotosViewsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {

                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);






                }

            }
            else{
                tv.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        return rootView;
    }

/*
* Initiating recycler view
* */
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
