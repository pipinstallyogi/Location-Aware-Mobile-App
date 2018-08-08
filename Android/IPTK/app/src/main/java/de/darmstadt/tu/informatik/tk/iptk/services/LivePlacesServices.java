package de.darmstadt.tu.informatik.tk.iptk.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.PlaceResultsActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.ThisPlaceActivity;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Socket;


/**
 * Created by aditya on 8/15/17.
 */
public class LivePlacesServices {
    private static LivePlacesServices mLivePlacesServices;

    private String Send2ActivityMes;

    private final int SERVER_SUCCESS = 6;
    private final int SERVER_FAILURE = 7;
    private final int USER_ERROR_EMPTY_QUERY = 9;
    private final int REVIEW_EMPTY_ERROR = 15;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static LivePlacesServices getInstance() {
        if (mLivePlacesServices == null) {
            mLivePlacesServices = new LivePlacesServices();
        }
        return mLivePlacesServices;
    }


    /**
     * Send text search query disposable.
     *
     * @param searchQueryEt the search query et
     * @param latitude      the latitude
     * @param longitude     the longitude
     * @param email         the email
     * @param uname         the uname
     * @param socket        the socket
     * @return the disposable
     */
    public Disposable sendTextSearchQuery(final EditText searchQueryEt,final String latitude,final String longitude,final String email,final String uname, final Socket socket) {
        List<String> searchQuery = new ArrayList<>();
        searchQuery.add(searchQueryEt.getText().toString());
        searchQuery.add(latitude);
        searchQuery.add(longitude);
        searchQuery.add(email);
        searchQuery.add(uname);


        Observable<List<String>> searchQueryObservable = Observable.just(searchQuery);

        return searchQueryObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@NonNull List<String> strings) throws Exception {
                        String queryKeyword = strings.get(0);
                        String latitude = strings.get(1);
                        String longitude = strings.get(2);
                        String email = strings.get(3);
                        String uname = strings.get(4);

                        if (queryKeyword.isEmpty()) {
                            return USER_ERROR_EMPTY_QUERY;
                        } else {
                            JSONObject searchData = new JSONObject();
                            try {
                                searchData.put("query", queryKeyword);
                                searchData.put("latitude",latitude);
                                searchData.put("longitude",longitude);
                                searchData.put("email",email);
                                searchData.put("uname",uname);
                                socket.emit("getPlacesInfo", searchData);
                                return SERVER_SUCCESS;
                            } catch (JSONException e) {
                                Log.i(LivePlacesServices.class.getSimpleName(),
                                        e.getMessage());
                                return SERVER_FAILURE;
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer.equals(USER_ERROR_EMPTY_QUERY)) {
                            searchQueryEt.setError("Search query Can't Be Empty");
                        }
                    }
                });
    }


    /**
     * Search response disposable.
     *
     * @param data              the data
     * @param activity          the activity
     * @param sharedPreferences the shared preferences
     * @return the disposable
     */
    public Disposable searchResponse(JSONObject data, final BaseFragmentActivity activity,final SharedPreferences sharedPreferences){
        Observable<JSONObject> jsonObjectObservable = Observable.just(data);
        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<JSONObject, String>() {
                    @Override
                    public String apply(@NonNull JSONObject jsonObject) throws Exception {
                        String message;

                        try {
                            JSONObject json = jsonObject.getJSONObject("response").getJSONObject("json");
                            message =  (String) json.get("status");
                            Send2ActivityMes = json.toString();
                            sharedPreferences.edit().clear();
                            sharedPreferences.edit().putString(Constants.MAP_DATA,Send2ActivityMes).apply();
                            return message;
                        } catch (JSONException e) {
                            return e.getMessage();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String stringResponse) {
                        if (stringResponse.equals("OK")){
                            Toast.makeText(activity,"Fetch Successful!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, PlaceResultsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            //activity.finish();
                        } else{
                            Toast.makeText(activity,stringResponse,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Send button search query disposable.
     *
     * @param buttonValue the button value
     * @param latitute    the latitute
     * @param longitude   the longitude
     * @param email       the email
     * @param uname       the uname
     * @param socket      the socket
     * @return the disposable
     */
    public Disposable sendButtonSearchQuery(final String buttonValue, final String  latitute,final String longitude,final String email,final String uname, final Socket socket) {
        List<String> searchQuery = new ArrayList<>();
        searchQuery.add(buttonValue);
        searchQuery.add(latitute);
        searchQuery.add(longitude);
        searchQuery.add(email);
        searchQuery.add(uname);
        //Log.d("Latitude",searchQuery.get(1));
        //Log.d("Longitude",searchQuery.get(2));
        //Log.d("socket button",socket.toString());



        Observable<List<String>> searchQueryObservable = Observable.just(searchQuery);

        return searchQueryObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@NonNull List<String> strings) throws Exception {
                        String queryKeyword = strings.get(0);
                        String latitude = strings.get(1);
                        String longitude = strings.get(2);
                        String email = strings.get(3);
                        String uname = strings.get(4);

                        if (queryKeyword.isEmpty()) {
                            return USER_ERROR_EMPTY_QUERY;
                        } else {
                            JSONObject searchData = new JSONObject();
                            try {
                                searchData.put("query", queryKeyword);
                                searchData.put("latitude",latitude);
                                searchData.put("longitude",longitude);
                                searchData.put("email",email);
                                searchData.put("uname",uname);
                                socket.emit("getPlacesInfo", searchData);
                                return SERVER_SUCCESS;
                            } catch (JSONException e) {
                                Log.i(LivePlacesServices.class.getSimpleName(),
                                        e.getMessage());
                                return SERVER_FAILURE;
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }


    /**
     * Send place details request disposable.
     *
     * @param PlaceId the place id
     * @param socket  the socket
     * @return the disposable
     */
    public Disposable sendPlaceDetailsRequest(final String PlaceId, final Socket socket) {
        List<String> searchQuery = new ArrayList<>();
        searchQuery.add(PlaceId);
        Log.d("Place Search",searchQuery.get(0));
        Log.d("socket place",socket.toString());


        Observable<List<String>> searchQueryObservable = Observable.just(searchQuery);

        return searchQueryObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@NonNull List<String> strings) throws Exception {
                        String queryKeyword = strings.get(0);

                        if (queryKeyword.isEmpty()) {
                            return USER_ERROR_EMPTY_QUERY;
                        } else {
                            JSONObject searchData = new JSONObject();
                            try {
                                searchData.put("placeid", queryKeyword);
                                socket.emit("getThisPlace", searchData);
                                return SERVER_SUCCESS;
                            } catch (JSONException e) {
                                Log.i(LivePlacesServices.class.getSimpleName(),
                                        e.getMessage());
                                return SERVER_FAILURE;
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }


    /**
     * Place details response disposable.
     *
     * @param data              the data
     * @param sharedPreferences the shared preferences
     * @param activity          the activity
     * @return the disposable
     */
    public Disposable placeDetailsResponse(JSONObject data, final SharedPreferences sharedPreferences, final BaseFragmentActivity activity){
        Observable<JSONObject> jsonObjectObservable = Observable.just(data);
        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<JSONObject, String>() {
                    @Override
                    public String apply(@NonNull JSONObject jsonObject) throws Exception {
                        String message;

                        try {
                            JSONObject json = jsonObject.getJSONObject("response").getJSONObject("json");
                            message =  (String) json.get("status");
                            Send2ActivityMes = json.toString();
                            return message;
                        } catch (JSONException e) {
                            return e.getMessage();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String stringResponse) {
                        if (stringResponse.equals("OK")){
                            Toast.makeText(activity,"Fetch Successful!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, ThisPlaceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            sharedPreferences.edit().clear();
                            sharedPreferences.edit().putString("data",Send2ActivityMes).apply();
                            activity.startActivity(intent);
                        } else{
                            Toast.makeText(activity,stringResponse,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Check in user disposable.
     *
     * @param placeId     the place id
     * @param userEmail   the user email
     * @param userName    the user name
     * @param userPicture the user picture
     * @param socket      the socket
     * @return the disposable
     */
    public Disposable checkInUser(String placeId, String userEmail, String userName, String userPicture, final Socket socket){
        List<String> checkInDetails = new ArrayList<>();
        checkInDetails.add(placeId);
        checkInDetails.add(userEmail);
        checkInDetails.add(userName);
        checkInDetails.add(userPicture);

        Observable<List<String>> checkInDetailsObservable = Observable.just(checkInDetails);

        return checkInDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@NonNull List<String> strings) throws Exception {
                        String place_id = strings.get(0);
                        String user_email = strings.get(1);
                        String user_picture = strings.get(3);
                        String user_name = strings.get(2);

                        JSONObject checkInData = new JSONObject();
                        try {
                            checkInData.put("placeid",place_id);
                            checkInData.put("email",user_email);
                            checkInData.put("picture",user_picture);
                            checkInData.put("name",user_name);
                            socket.emit("checkUserIn", checkInData);
                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            Log.i(LivePlacesServices.class.getSimpleName(),
                                    e.getMessage());
                            return SERVER_FAILURE;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer integer) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Get checkin data disposable.
     *
     * @param placeId the place id
     * @param socket  the socket
     * @return the disposable
     */
    public Disposable getCheckinData(String placeId, final Socket socket){
        List<String> checkInDetails = new ArrayList<>();
        checkInDetails.add(placeId);

        Observable<List<String>> checkInDetailsObservable = Observable.just(checkInDetails);

        return checkInDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@NonNull List<String> strings) throws Exception {
                        String place_id = strings.get(0);

                        JSONObject checkInData = new JSONObject();
                        try {
                            checkInData.put("placeid",place_id);
                            socket.emit("getCheckins",checkInData);
                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            Log.i(LivePlacesServices.class.getSimpleName(),
                                    e.getMessage());
                            return SERVER_FAILURE;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer integer) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Insert review disposable.
     *
     * @param placeId        the place id
     * @param userName       the user name
     * @param userPicture    the user picture
     * @param userEmail      the user email
     * @param ReviewEditText the review edit text
     * @param socket         the socket
     * @return the disposable
     */
    public Disposable insertReview(final String placeId, final String userName,final String userPicture,final String userEmail,final EditText ReviewEditText, final Socket socket){
        List<String> reviewDetails = new ArrayList<>();
        reviewDetails.add(ReviewEditText.getText().toString());
        reviewDetails.add(placeId);
        reviewDetails.add(userName);
        reviewDetails.add(userEmail);
        reviewDetails.add(userPicture);


        Observable<List<String>> reviewDetailsObservable = Observable.just(reviewDetails);

        return reviewDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@NonNull List<String> strings) throws Exception {
                        final String review = strings.get(0);
                        final String placeid = strings.get(1);
                        final String name = strings.get(2);
                        final String email = strings.get(3);
                        final String photo = strings.get(4);

                        if (review.isEmpty()){
                            return REVIEW_EMPTY_ERROR;
                        }
                        else
                        {
                            JSONObject reviewData = new JSONObject();
                            try{
                                reviewData.put("email",email);
                                reviewData.put("name",name);
                                reviewData.put("placeid",placeid);
                                reviewData.put("review",review);
                                reviewData.put("picture",photo);
                                socket.emit("insertPlaceReview",reviewData);
                                return SERVER_SUCCESS;
                            }
                            catch (JSONException e){
                                Log.i(LiveAccountServices.class.getSimpleName(),
                                        e.getMessage());
                                return SERVER_FAILURE;

                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer integer) {
                        if (integer.equals(REVIEW_EMPTY_ERROR)){
                            ReviewEditText.setError("Review Can't Be Empty");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
