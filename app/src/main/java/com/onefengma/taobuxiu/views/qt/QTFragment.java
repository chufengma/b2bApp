package com.onefengma.taobuxiu.views.qt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.QtManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.SystemHelper;
import com.onefengma.taobuxiu.model.events.OnMineTabEvent;
import com.onefengma.taobuxiu.model.events.QtListEvent;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.auth.WebViewActivity;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.ToolBar;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/7.
 */
public class QTFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.qt_view_pager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLeftViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AuthManager.instance().getUserSalesmanMobile())) {
                    ToastUtils.showTasty("您还没有绑定专员，请到淘不锈官网绑定专员", TastyToast.INFO, Toast.LENGTH_LONG);
                    return;
                }
                DialogUtils.showAlertDialog(getActivity(), "拨打专员电话:" + AuthManager.instance().getUserSalesmanMobile(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SystemHelper.call(AuthManager.instance().getUserSalesmanMobile());
                    }
                });
            }
        });

        QtPagerAdapter adapter = new QtPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);
        toolbar.getLeftImageLayout().setVisibility(View.GONE);
    }


    @OnClick(R.id.left_title)
    public void clickOnLeftTitle() {
        QtManager.instance().cantactSalesMan((BaseActivity) getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(OnMineTabEvent OnQtTabEvent) {
        System.out.println("-----------------OnQtTabEvent");
    }

    @OnClick(R.id.right_image)
    public void onRightImageClick() {
        WebViewActivity.start((BaseActivity) getActivity(), "file:///android_asset/qt_info.html", "上门质检服务说明");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    @Subscribe
    public void onQtListEvent(QtListEvent event) {
        switch (event.qtStatus) {
            case QT_WAITING:
                tab.getTabAt(0).setText("等待质检(" +QtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
            case QT_DOING:
                tab.getTabAt(1).setText("质检中(" +QtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
            case QT_DONE:
                tab.getTabAt(2).setText("质检完成(" +QtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
            case QT_CANCEL:
                tab.getTabAt(3).setText("质检取消(" +QtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
        };
    }

}


