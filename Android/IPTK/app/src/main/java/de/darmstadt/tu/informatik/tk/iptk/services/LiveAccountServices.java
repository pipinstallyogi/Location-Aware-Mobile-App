package de.darmstadt.tu.informatik.tk.iptk.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.SearchActivity;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Socket;


/**
 * The type Live account services.
 */
public class LiveAccountServices {
    private static LiveAccountServices mLiveAccountServices;

    private final int USER_ERROR_EMPTY_PASSWORD =1;
    private final int USER_ERROR_EMPTY_EMAIL =2;
    private final int USER_ERROR_EMPTY_USERNAME =3;
    private final int USER_ERROR_PASSWORD_SHORT =4;
    private final int USER_ERROR_EMAIL_BAD_FORMAT =5;


    private final int SERVER_SUCCESS = 6;
    private final int SERVER_FAILURE = 7;

    private final int USER_NO_ERRORS =8;


    /**
     * Get instance live account services.
     *
     * @return the live account services
     */
    public static LiveAccountServices getInstance(){
        if(mLiveAccountServices ==null){
            mLiveAccountServices = new LiveAccountServices();
        }
        return mLiveAccountServices;
    }


    /**
     * Change profile photo disposable.
     *
     * @param storageReference  the storage reference
     * @param uri               the uri
     * @param activity          the activity
     * @param currentUserEmail  the current user email
     * @param imageView         the image view
     * @param sharedPreferences the shared preferences
     * @param socket            the socket
     * @return the disposable
     */
    public Disposable changeProfilePhoto(final StorageReference storageReference, Uri uri, final BaseFragmentActivity activity
            , final String currentUserEmail, final ImageView imageView, final SharedPreferences sharedPreferences, final Socket socket){
      Observable<Uri> uriObservable = Observable.just(uri);
        return uriObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<Uri, byte[]>() {
                    @Override
                    public byte[] apply(@NonNull Uri uri) throws Exception {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uri);
                            int outPutHeight= (int)(bitmap.getHeight() * (512.0/bitmap.getWidth()));
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,512,outPutHeight,true);
                            ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();
                            scaledBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutPutStream);
                            return byteArrayOutPutStream.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<byte[]>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        UploadTask uploadTask = storageReference.putBytes(bytes);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                JSONObject sendData = new JSONObject();
                                try {
                                    sendData.put("email", currentUserEmail);
                                    sendData.put("picUrl", taskSnapshot.getDownloadUrl().toString());
                                    socket.emit("userUpdatedPicture", sendData);
                                    sharedPreferences.edit().putString(Constants.USER_PICTURE,taskSnapshot.getDownloadUrl().toString()).apply();
                                    Picasso.with(activity).load(taskSnapshot.getDownloadUrl().toString())
                                            .into(imageView);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }


    /**
     * Get auth token disposable.
     *
     * @param data              the data
     * @param activity          the activity
     * @param sharedPreferences the shared preferences
     * @return the disposable
     */
    public Disposable getAuthToken(JSONObject data, final BaseFragmentActivity activity, final SharedPreferences sharedPreferences){
        Observable<JSONObject> jsonObservable = Observable.just(data);

        return jsonObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<JSONObject, List<String>>() {
                    @Override
                    public List<String> apply(@io.reactivex.annotations.NonNull JSONObject jsonObject) throws Exception {
                        List<String> userDetails = new ArrayList<>();

                        try {
                            JSONObject serverData = jsonObject.getJSONObject("token");
                            String token = (String) serverData.get("authToken");
                            String email = (String) serverData.get("email");
                            String photo = (String) serverData.get("photo");
                            String userName = (String) serverData.get("displayName");

                            userDetails.add(token);
                            userDetails.add(email);
                            userDetails.add(photo);
                            userDetails.add(userName);
                            return userDetails;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return userDetails;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        String token = strings.get(0);
                        final String email = strings.get(1);
                        final String photo = strings.get(2);
                        final String userName = strings.get(3);

                        if (!email.equals("error")){
                            FirebaseAuth.getInstance().signInWithCustomToken(token)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(activity,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            } else{
                                                sharedPreferences.edit().putString(Constants.USER_EMAIL,email).apply();
                                                sharedPreferences.edit().putString(Constants.USER_NAME,userName).apply();
                                                sharedPreferences.edit().putString(Constants.USER_PICTURE,photo).apply();



                                                Intent intent = new Intent(activity, SearchActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                activity.startActivity(intent);
                                                activity.finish();

                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    /**
     * Send login info disposable.
     *
     * @param userEmailEt    the user email et
     * @param userPasswordEt the user password et
     * @param socket         the socket
     * @param activity       the activity
     * @return the disposable
     */
    public Disposable sendLoginInfo(final EditText userEmailEt, final EditText userPasswordEt, final Socket socket
            , final BaseFragmentActivity activity){
        List<String> userDetails = new ArrayList<>();
        userDetails.add(userEmailEt.getText().toString());
        userDetails.add(userPasswordEt.getText().toString());

        Observable<List<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@io.reactivex.annotations.NonNull List<String> strings) throws Exception {
                        final String userEmail = strings.get(0);
                        String userPassword = strings.get(1);

                        if (userEmail.isEmpty()){
                            return USER_ERROR_EMPTY_EMAIL;
                        } else if (userPassword.isEmpty()){
                            return USER_ERROR_EMPTY_PASSWORD;
                        } else if(userPassword.length()<6){
                            return USER_ERROR_PASSWORD_SHORT;
                        } else if (!isEmailValid(userEmail)){
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        } else {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,userPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(activity,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            } else{
                                                JSONObject sendData = new JSONObject();
                                                try {
                                                    sendData.put("email",userEmail);
                                                    socket.emit("userInfo",sendData);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });


                            try {
                                FirebaseInstanceId.getInstance().deleteInstanceId();
                                FirebaseInstanceId.getInstance().getToken();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            FirebaseAuth.getInstance().signOut();
                            return USER_NO_ERRORS;
                        }


                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                        if (integer.equals(USER_ERROR_EMPTY_EMAIL)){
                            userEmailEt.setError("Email Address Can't Be Empty");
                        } else if (integer.equals(USER_ERROR_EMAIL_BAD_FORMAT)){
                            userEmailEt.setError("Please check your email format");
                        } else if (integer.equals(USER_ERROR_EMPTY_PASSWORD)){
                            userPasswordEt.setError("Password Can't Be Blank");
                        } else if (integer.equals(USER_ERROR_PASSWORD_SHORT)){
                            userPasswordEt.setError("Password must be at least 6 characters long");
                        }

                    }
                });

    }


    /**
     * Send registration info disposable.
     *
     * @param userNameEt     the user name et
     * @param userEmailEt    the user email et
     * @param userPasswordEt the user password et
     * @param socket         the socket
     * @return the disposable
     */
    public Disposable sendRegistrationInfo(final EditText userNameEt, final EditText userEmailEt,
                                             final EditText userPasswordEt, final Socket socket){
        List<String> userDetails = new ArrayList<>();
        userDetails.add(userNameEt.getText().toString());
        userDetails.add(userEmailEt.getText().toString());
        userDetails.add(userPasswordEt.getText().toString());

        Observable<List<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@io.reactivex.annotations.NonNull List<String> strings) throws Exception {
                        String userName = strings.get(0);
                        String userEmail = strings.get(1);
                        String userPassword = strings.get(2);

                        if (userName.isEmpty()){
                            return USER_ERROR_EMPTY_USERNAME;
                        } else if (userEmail.isEmpty()){
                            return USER_ERROR_EMPTY_EMAIL;
                        } else if (userPassword.isEmpty()){
                            return USER_ERROR_EMPTY_PASSWORD;
                        } else if(userPassword.length()<6){
                            return USER_ERROR_PASSWORD_SHORT;
                        } else if (!isEmailValid(userEmail)){
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        } else{
                            JSONObject sendData = new JSONObject();
                            try {
                                sendData.put("email",userEmail);
                                sendData.put("userName",userName);
                                sendData.put("password",userPassword);
                                socket.emit("userData",sendData);
                                return SERVER_SUCCESS;
                            } catch (JSONException e) {
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
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer.equals(USER_ERROR_EMPTY_EMAIL)){
                            userEmailEt.setError("Email Address Can't Be Empty");
                        } else if (integer.equals(USER_ERROR_EMAIL_BAD_FORMAT)){
                            userEmailEt.setError("Please check your email format");
                        } else if (integer.equals(USER_ERROR_EMPTY_PASSWORD)){
                            userPasswordEt.setError("Password Can't Be Blank");
                        } else if (integer.equals(USER_ERROR_PASSWORD_SHORT)){
                            userPasswordEt.setError("Password must be at least 6 characters long");
                        } else if(integer.equals(USER_ERROR_EMPTY_USERNAME)){
                            userNameEt.setError("Username can't be empty");
                        }

                    }
                });
    }


    /**
     * Register response disposable.
     *
     * @param data     the data
     * @param activity the activity
     * @return the disposable
     */
    public Disposable registerResponse(JSONObject data, final BaseFragmentActivity activity){
        Observable<JSONObject> jsonObjectObservable = Observable.just(data);
        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<JSONObject, String>() {
                    @Override
                    public String apply(@io.reactivex.annotations.NonNull JSONObject jsonObject) throws Exception {
                        String message;

                        try {
                            JSONObject json = jsonObject.getJSONObject("message");
                            message = (String) json.get("text");
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
                        if (stringResponse.equals("Success")){
                            activity.finish();
                            Toast.makeText(activity,"Registration Successful!",Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(activity,stringResponse,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Delete user profile disposable.
     *
     * @param mUserEmail        the m user email
     * @param sharedPreferences the shared preferences
     * @param socket            the socket
     * @param activity          the activity
     * @return the disposable
     */
    public Disposable deleteUserProfile(final String mUserEmail,final SharedPreferences sharedPreferences,final Socket socket,final BaseFragmentActivity activity){
        List<String> userDetails = new ArrayList<>();
        userDetails.add(mUserEmail);

        Observable<List<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@io.reactivex.annotations.NonNull List<String> strings) throws Exception {
                        final String userEmail = strings.get(0);
                        if (userEmail!=null){
                            JSONObject sendData = new JSONObject();

                            try {
                                sharedPreferences.edit().putString(Constants.USER_EMAIL,"").apply();
                                sharedPreferences.edit().putString(Constants.USER_NAME,"").apply();
                                sharedPreferences.edit().putString(Constants.USER_PICTURE,"").apply();
                                sendData.put("email",mUserEmail);
                                socket.emit("userDelete",sendData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        FirebaseAuth.getInstance().signOut();
                        return USER_NO_ERRORS;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Integer integer) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Reset password disposable.
     *
     * @param userEmailEt the user email et
     * @param socket      the socket
     * @param activity    the activity
     * @return the disposable
     */
    public Disposable resetPassword(final EditText userEmailEt, final Socket socket
            , final BaseFragmentActivity activity){
        List<String> userDetails = new ArrayList<>();
        userDetails.add(userEmailEt.getText().toString());

        Observable<List<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, Integer>() {
                    @Override
                    public Integer apply(@io.reactivex.annotations.NonNull List<String> strings) throws Exception {
                        final String userEmail = strings.get(0);

                        if (userEmail.isEmpty()){
                            return USER_ERROR_EMPTY_EMAIL;
                        }
                        else
                        {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(activity,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                            }else
                                            {
                                                Toast.makeText(activity,"Password Reset Instructions sent to your Registered Email",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            JSONObject sendData = new JSONObject();
                            sendData.put("email",userEmail);
                            socket.emit("resetUserPassword",sendData);
                        }
                        return USER_NO_ERRORS;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Integer integer) {
                        if (integer.equals(USER_ERROR_EMPTY_EMAIL)) {
                            userEmailEt.setError("Email Address Can't Be Empty");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }




}
