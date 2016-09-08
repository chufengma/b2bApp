package com.onefengma.taobuxiu.views.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.QtManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.sales.SalesQtListEvent;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/9/1
 */
public class SalesQtFragment extends BaseFragment {

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.sales_qt_view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    ToolBar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slaes_qt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SalesQtPagerAdapter adapter = new SalesQtPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);
        toolbar.getLeftImageLayout().setVisibility(View.GONE);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
    @Subscribe
    public void onQtListEvent(SalesQtListEvent event) {
        switch (event.qtStatus) {
            case QT_WAITING:
                tab.getTabAt(0).setText("等待质检(" + SalesQtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
            case QT_DOING:
                tab.getTabAt(1).setText("质检中(" +SalesQtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
            case QT_DONE:
                tab.getTabAt(2).setText("质检完成(" +SalesQtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
            case QT_CANCEL:
                tab.getTabAt(3).setText("质检取消(" +SalesQtManager.instance().qtListResponses[event.qtStatus.ordinal()].maxCount  + ")");
                break;
        };
    }

}
