package com.example.mytabdemoapp;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {
    private ProgressDialog progressDialog;
    private Context context;

    public LoadingDialog(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Set the message you want to display
        progressDialog.setCancelable(false); // Set whether it can be canceled by tapping outside
    }

    public void show() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
