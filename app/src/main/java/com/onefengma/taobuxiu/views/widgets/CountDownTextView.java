package com.onefengma.taobuxiu.views.widgets;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.OnGetMsgCodeEvent;
import com.onefengma.taobuxiu.utils.SPHelper;

import org.greenrobot.eventbus.Subscribe;

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

        long lastGetVerifyCodeTime = SPHelper.instance().getLong(GET_VERIFY_CODE_TIME, 0);
        if (lastGetVerifyCodeTime > 0 && System.currentTimeMillis() - lastGetVerifyCodeTime < 60 * 1000) {
            startCountDown((int) ((System.currentTimeMillis() - lastGetVerifyCodeTime) / 1000));
        }
    }

    @Subscribe
    public void onGetCode(OnGetMsgCodeEvent event) {
        if (event.status == BaseStatusEvent.STARTED) {
            setText(R.string.get_verify_code_started);
        } else if (event.status == BaseStatusEvent.FAILED) {
            setText(R.string.get_verify_code);
        } else {
            SPHelper.instance().save(GET_VERIFY_CODE_TIME, System.currentTimeMillis());
            startCountDown(0);
        }
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBusHelper.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBusHelper.unregister(this);
    }
}
