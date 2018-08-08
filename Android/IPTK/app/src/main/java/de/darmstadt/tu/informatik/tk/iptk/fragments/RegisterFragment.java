package de.darmstadt.tu.informatik.tk.iptk.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import de.darmstadt.tu.informatik.tk.iptk.services.LiveAccountServices;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * The type Register fragment.
 */
public class RegisterFragment extends BaseFragment {

    /**
     * The M user name et.
     */
    @BindView(R2.id.fragment_register_userName)
    EditText mUserNameEt;

    /**
     * The M user email et.
     */
    @BindView(R2.id.fragment_register_userEmail)
    EditText mUserEmailEt;

    /**
     * The M user password et.
     */
    @BindView(R2.id.fragment_register_userPassword)
    EditText mUserPasswordEt;

    /**
     * The M register button.
     */
    @BindView(R2.id.fragment_register_registerButton)
    Button mRegisterButton;

    /**
     * The M login button.
     */
    @BindView(R2.id.fragment_register_loginButton)
    Button mLoginButton;

    private Unbinder mUnbinder;
    private Socket mSocket;

    private BaseFragmentActivity mActivity;


    private LiveAccountServices mLiveAccountServices;

    /**
     * New instance register fragment.
     *
     * @return the register fragment
     */
    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(Constants.IP_HOST);
        } catch (URISyntaxException e) {
            Log.i(RegisterFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();
        mSocket.on("message",accountResponse());

        mLiveAccountServices = LiveAccountServices.getInstance();



    }

    /**
     * Setm register button.
     */
    @OnClick(R2.id.fragment_register_registerButton)
    public void setmRegisterButton(){
        mCompositeDisposable.add(mLiveAccountServices.sendRegistrationInfo(
                mUserNameEt,mUserEmailEt,mUserPasswordEt,mSocket
        ));
    }

    /**
     * Setm login button.
     */
    @OnClick(R2.id.fragment_register_loginButton)
    public void setmLoginButton(){
        getActivity().finish();
    }


    private Emitter.Listener accountResponse(){
        return  new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                mCompositeDisposable.add(mLiveAccountServices.registerResponse(data,mActivity));
            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity =null;
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
