package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.activities.RegisterActivity;
import de.darmstadt.tu.informatik.tk.iptk.services.LiveAccountServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * The type Login fragment.
 */
public class LoginFragment extends BaseFragment {

    /**
     * The M user email et.
     */
    @BindView(R2.id.fragment_login_userEmail)
    EditText mUserEmailEt;

    /**
     * The M user password et.
     */
    @BindView(R2.id.fragment_login_userPassword)
    EditText mUserPasswordEt;

    /**
     * The M login button.
     */
    @BindView(R2.id.fragment_login_login_button)
    Button mLoginButton;

    /**
     * The M register button.
     */
    @BindView(R2.id.fragment_login_register_button)
    Button mRegisterButton;

    /**
     * The M forgot text view.
     */
    @BindView(R2.id.fragment_login_forgot_textView)
    TextView mForgotTextView;

    private Unbinder mUnbinder;

    private Socket mSocket;

    private BaseFragmentActivity mActivity;
    private LiveAccountServices mLiveAccountServices;


    /**
     * New instance login fragment.
     *
     * @return the login fragment
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSocket = IO.socket(Constants.IP_HOST);
        } catch (URISyntaxException e) {
            Log.i(LoginFragment.class.getSimpleName(), e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }


        mSocket.connect();

        mLiveAccountServices = LiveAccountServices.getInstance();
        mSocket.on("token", tokenListener());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    /**
     * Sets login button.
     */
    @OnClick(R2.id.fragment_login_login_button)
    public void setmLoginButton() {
        mCompositeDisposable.add(mLiveAccountServices.sendLoginInfo(mUserEmailEt
                , mUserPasswordEt, mSocket, mActivity));
    }

    /**
     * Sets register button.
     */
    @OnClick(R2.id.fragment_login_register_button)
    public void setmRegisterButton() {
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }


    /**
     * Setm forgot text view.
     */
    @OnClick(R2.id.fragment_login_forgot_textView)
    public void setmForgotTextView(){
        mCompositeDisposable.add(mLiveAccountServices.resetPassword(mUserEmailEt,mSocket,mActivity));
    }


    private Emitter.Listener tokenListener() {
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                mCompositeDisposable.add(mLiveAccountServices
                        .getAuthToken(jsonObject, mActivity, mSharedPreferences));
            }
        };
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
