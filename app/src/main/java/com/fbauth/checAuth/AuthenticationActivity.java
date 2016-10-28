package com.fbauth.checAuth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.fbauth.checAuth.fragment.BaseFragment;
import com.fbauth.checAuth.fragment.SignupFragment;


/**
 * Created by labattula on 24/10/16.
 */

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = AuthenticationActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //setting the Toobar as actionBar for navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signUpPage();

    }

    private void signUpPage() {
        BaseFragment signUpFragment = SignupFragment.newInstance();
        launchFragment(signUpFragment, "signUp");

    }

    /**
     * For lauching the fragment
     *
     * @param fragment
     * @param tag
     */
    public void launchFragment(BaseFragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.content_frame, fragment, tag).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
