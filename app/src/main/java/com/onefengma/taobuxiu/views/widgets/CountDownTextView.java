package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.utils.SPHelper;

import static com.onefengma.taobuxiu.model.Constant.StorageKeys.GET_VERIFY_CODE_TIME;

/**
 * @author yfchu
 * @date 2016/8/11
 */
public class CountDownTextView extends TextView {

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetCodeSuccess();
            }
        });

        long lastGetVerifyCodeTime = SPHelper.instance().getLong(GET_VERIFY_CODE_TIME, 0);
        if (lastGetVerifyCodeTime > 0 && System.currentTimeMillis() - lastGetVerifyCodeTime < 60 * 1000) {
            startCountDown((int) ((System.currentTimeMillis() - lastGetVerifyCodeTime) / 1000));
        }
    }

    private void onGetCodeSuccess() {
        SPHelper.instance().save(GET_VERIFY_CODE_TIME, System.currentTimeMillis());
        startCountDown(0);
    }

    private void startCountDown(final int currentValue) {
        int maxValue = 60 - currentValue;
        setEnabled(false);
        new CountDownTimer(maxValue * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                setText(getContext().getString(R.string.get_verify_code_desc, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                setText(R.string.get_verify_code);
                setEnabled(true);
            }

        }.start();
    }

}
