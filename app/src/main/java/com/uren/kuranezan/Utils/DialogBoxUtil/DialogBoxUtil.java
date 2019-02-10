package com.uren.kuranezan.Utils.DialogBoxUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.CommonUtils;
import com.uren.kuranezan.Utils.Config;
import com.uren.kuranezan.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;

import java.util.List;

public class DialogBoxUtil {

    public static void showYesNoDialog(Context context, String title, String message, final YesNoDialogBoxCallback yesNoDialogBoxCallback) {
        CommonUtils.hideKeyBoard(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);

        if (title != null && !title.isEmpty())
            builder.setTitle(title);

        builder.setPositiveButton(context.getResources().getString(R.string.UPPERYES), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                yesNoDialogBoxCallback.yesClick();
            }
        });

        builder.setNegativeButton(context.getResources().getString(R.string.UPPERNO), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                yesNoDialogBoxCallback.noClick();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showRadioDialogBox(Context context, String title, List<String> stringList, String identifier, final YesNoDialogBoxCallback yesNoDialogBoxCallback) {

        // custom dialog
        CommonUtils.hideKeyBoard(context);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.radiobutton_dialog);
        dialog.setTitle(title);

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnApply = (Button) dialog.findViewById(R.id.btnApply);

        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(context); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);

            if(stringList.get(i).toLowerCase().equals(Config.lang)){

            }

        }

        dialog.show();

        //set Listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                yesNoDialogBoxCallback.noClick();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                yesNoDialogBoxCallback.yesClick();
            }
        });

    }

}
