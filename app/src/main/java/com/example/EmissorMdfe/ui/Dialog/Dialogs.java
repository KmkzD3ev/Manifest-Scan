package com.example.EmissorMdfe.ui.Dialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.EmissorMdfe.R;


public class Dialogs {
    private Activity activity;
    private AlertDialog dialog;

    public Dialogs(Activity activity) {
        this.activity = activity;
    }

    public void iniciarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.dialog_carregamento, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void liberarDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}