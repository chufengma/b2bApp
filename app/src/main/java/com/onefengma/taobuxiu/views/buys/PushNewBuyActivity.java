package com.onefengma.taobuxiu.views.buys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.IronBuyPush;
import com.onefengma.taobuxiu.model.events.IronBuyPushEvent;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.ToolBar;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/21.
 */
public class PushNewBuyActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.list)
    XListView list;

    PushListAdapter pushListAdapter;
    ProgressDialog progressDialog;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, PushNewBuyActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.add_iron)
    public void clickOnAddEdit() {
        EditBuyActivity.start(this, null);
    }

    @OnClick(R.id.right_image)
    public void clickOnRightImage() {
        if (pushListAdapter.getCount() == 0) {
            ToastUtils.showInfoTasty("您尚未添加待发布的求购，请添加求购后再来点击我吧！");
        } else {
            BuyManager.instance().doPushAllIronBuy();
        }
    }

    @Subscribe
    public void onPushEvent(IronBuyPushEvent event) {
        if (event.isStarted()) {
            progressDialog.show("发布中...");
            return;
        }

        if (BuyManager.instance().getCachedIronBuys().size() == 0 && event.isSuccess()) {
            progressDialog.dismiss();
        }

        if (event.isSuccess() && BuyManager.instance().getCachedIronBuys().size() == 0) {
            ToastUtils.showSuccessTasty("全部发布求购成功！");
            finish();
        } else {
            ToastUtils.showSuccessTasty("发布求购失败，请重试！");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_new_buy);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        pushListAdapter = new PushListAdapter();
        list.setAdapter(pushListAdapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogUtils.showItemDialog(view.getContext(), null, new String[]{"删除该项", "复制添加类似求购"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            BuyManager.instance().deleteIronBuy(pushListAdapter.getItem(position - list.getHeaderViewsCount()));
                        } else if (which == 1) {
                            IronBuyPush newPush = pushListAdapter.getItem(position - list.getHeaderViewsCount()).copy();
                            newPush.id = System.currentTimeMillis();
                            EditBuyActivity.start(PushNewBuyActivity.this, newPush);
                        }
                        pushListAdapter.setMyBuys(BuyManager.instance().getCachedIronBuys());
                    }
                });
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditBuyActivity.start((BaseActivity) parent.getContext(), pushListAdapter.getItem(position - list.getHeaderViewsCount()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pushListAdapter.setMyBuys(BuyManager.instance().getCachedIronBuys());
        EventBusHelper.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusHelper.unregister(this);
    }


}
