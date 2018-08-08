package de.darmstadt.tu.informatik.tk.iptk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by aditya on 8/26/17.
 */
public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,SearchActivity.class));
        finish();
    }
}
