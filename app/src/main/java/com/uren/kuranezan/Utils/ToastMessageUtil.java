package com.uren.kuranezan.Utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uren.kuranezan.R;

public class ToastMessageUtil {

    public static final void showToastShort(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static final void showToastLong(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static final void showCustomToast(Context context, String message) {

            if (context == null) return;
            if (message == null || message.isEmpty()) return;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_custom_toast, null);
            View layout = (LinearLayout) view.findViewById(R.id.custom_toast_container);

            layout.setBackground(ShapeUtil.getShape(context.getResources().getColor(R.color.CornflowerBlue),
                    context.getResources().getColor(R.color.White), GradientDrawable.RECTANGLE, 15, 3));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(message);
            text.setTextColor(context.getResources().getColor(R.color.White));

            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
    }
}
