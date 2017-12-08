package com.foxyawn.onu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sangjun on 2017-09-11.
 */

public class CustomDialog extends AppCompatActivity{
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View dialogView;
    public TextView dialogText;
    public Button dialogButton1;
    public Button dialogButton2;

    @SuppressLint("RestrictedApi")
    public AlertDialog getInstance(Context context, LayoutInflater inflater, int layout) {
        dialogView = inflater.inflate(layout, null);

        builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Dialog));
        builder.setView(dialogView);
        dialog = builder.create();

        return dialog;
    }

    public void show(String contentText, String buttonText) {


        dialogText = (TextView) dialogView.findViewById(R.id.dialog_text);
        dialogText.setText(contentText);
        dialogButton1 = (Button) dialogView.findViewById(R.id.dialog_button);
        dialogButton1.setText(buttonText);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void show(String contentText, String buttonText1, String buttonText2) {

        dialogText = (TextView) dialogView.findViewById(R.id.dialog_text);
        dialogText.setText(contentText);
        dialogButton1 = (Button) dialogView.findViewById(R.id.dialog_button1);
        dialogButton1.setText(buttonText1);

        dialogButton2 = (Button) dialogView.findViewById(R.id.dialog_button2);
        dialogButton2.setText(buttonText2);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
