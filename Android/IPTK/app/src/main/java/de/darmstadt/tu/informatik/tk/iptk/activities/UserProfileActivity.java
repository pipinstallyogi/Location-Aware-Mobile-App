package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.darmstadt.tu.informatik.tk.iptk.R;

/**
 * The type User profile activity.
 */
public class UserProfileActivity extends AppCompatActivity {

    private TextView mNameTextView,mEmailTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String name = bundle.getString("name");
        String photoUrl = bundle.getString("picture");
        Log.d("photo",photoUrl);
        getSupportActionBar().setTitle(name+"\'s Profile");

        mNameTextView = (TextView)findViewById(R.id.activity_user_profile_NameTextView);
        mEmailTextView = (TextView)findViewById(R.id.activity_user_profile_EmailTextView);
        mImageView = (ImageView)findViewById(R.id.activity_user_profile_ImageView);

        mNameTextView.setText(name);
        mEmailTextView.setText(email);
        Picasso.with(this).load(photoUrl).into(mImageView);

    }
}
