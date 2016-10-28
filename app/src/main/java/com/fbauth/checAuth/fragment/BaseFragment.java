package com.fbauth.checAuth.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by labattula on 24/10/16.
 */

public class BaseFragment extends Fragment {

    /**
     * Showing the snackBar
     * @param parentLayout
     * @param string
     */
    public void showSnackBar(View parentLayout,String string){
        Snackbar snackbar = Snackbar
                .make(parentLayout, string, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

}
