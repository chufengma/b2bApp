package com.onefengma.taobuxiu.views.widgets;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class ProgressDialog extends Dialog {

    private TextView messageText;

    public ProgressDialog(Context context) {
        super(context, R.style.progress_dialog);

        setContentView(R.layout.progress_dialog);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        messageText = (TextView) findViewById(R.id.id_tv_loadingmsg);
    }

    public void setMessage(CharSequence message) {
        messageText.setText(message);
    }

    public void show(CharSequence charSequence) {
        messageText.setText(charSequence);
        show();
    }

}
