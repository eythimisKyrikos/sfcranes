package com.safeline.safelinecranes.ui.sync;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.safeline.safelinecranes.R;

public class LoadingDialog {
    Activity activity;
    AlertDialog alertDialog;

    LoadingDialog (Activity myActivity) {
        activity = myActivity;
    }

    void startingLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading_dialog, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissDialog() {
        alertDialog.dismiss();
    }

}
