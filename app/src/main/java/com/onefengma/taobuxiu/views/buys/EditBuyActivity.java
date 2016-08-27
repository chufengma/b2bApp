package com.onefengma.taobuxiu.views.buys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBuyActivity extends AppCompatActivity {

    @BindView(R.id.item)
    EditText item;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;

    public static void start(Context context) {
        Intent starter = new Intent(context, EditBuyActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buy);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.item)
    public void clickItem() {
//        DialogUtils.showItemDialog(this, "选择时间", new String[]{"刚才", "现在", "未来"}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ToastUtils.showInfoTasty("item:" + which);
//            }
//        });

        PopupMenu menu = new PopupMenu(this, item);
        menu.getMenu().add("hahaha");
        menu.getMenu().add("ssdf");
        menu.getMenu().add("dfadsf");
        menu.getMenu().add("ffff");

        menu.show();
    }

}
