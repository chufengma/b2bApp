package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.logistics.ChooseDeadLineEvent;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChooseDeadlineActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.day)
    AppCompatSpinner day;
    @BindView(R.id.hour)
    AppCompatSpinner hour;
    @BindView(R.id.minute)
    AppCompatSpinner minute;
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.activity_choose_deadline)
    LinearLayout activityChooseDeadline;

    public static void start(Activity activity, int day, int hour, int minute) {
        Intent intent = new Intent(activity, ChooseDeadlineActivity.class);
        intent.putExtra("day", day);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_deadline);
        ButterKnife.bind(this);

        int dayValue = getIntent().getIntExtra("day", 0);
        int hourValue = getIntent().getIntExtra("hour", 0);
        int minuteValue = getIntent().getIntExtra("minute", 0);

        day.setSelection(dayValue);
        hour.setSelection(hourValue);
        minute.setSelection(minuteValue);
    }

    @OnClick(R.id.done)
    public void onDone() {
        if (day.getSelectedItemPosition() == 0 && hour.getSelectedItemPosition() == 0 && minute.getSelectedItemPosition() == 0) {
            ToastUtils.showInfoTasty("请选择合法的时间期限");
            return;
        }
        EventBusHelper.post(new ChooseDeadLineEvent(day.getSelectedItemPosition(), hour.getSelectedItemPosition(), minute.getSelectedItemPosition()));
        finish();
    }

}
