package com.onefengma.taobuxiu.views.offers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.OfferManager;
import com.onefengma.taobuxiu.manager.OfferManager.OfferStatus;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.MyOffersResponse;
import com.onefengma.taobuxiu.model.events.MyOffersEvent;
import com.onefengma.taobuxiu.model.events.OnOfferTabEvent;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.TabItem;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/7.
 */
public class OffersFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.offer_tab)
    TabLayout offerTab;
    @BindView(R.id.offer_view_pager)
    ViewPager offerViewPager;

    TabLayout.Tab tabDoing;
    TabLayout.Tab tabWaiting;
    TabLayout.Tab tabWin;
    TabLayout.Tab tabLose;

    TabItem tabItemDoing;
    TabItem tabItemWaiting;
    TabItem tabItemWin;
    TabItem tabItemLose;

    @OnClick(R.id.right_image)
    public void clickOnRightImage() {
        MySubscribeActivity.start((BaseActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        OfferFragmentViewPagerAdapter adapter = new OfferFragmentViewPagerAdapter(getFragmentManager());
        offerViewPager.setOffscreenPageLimit(4);
        offerViewPager.setAdapter(adapter);

        offerViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(offerTab));
        offerTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(offerViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab == tabDoing) {
                    offerTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_yellow));
                } else if (tab == tabWaiting) {
                    offerTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
                } else if (tab == tabWin) {
                    offerTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_green));
                } else if (tab == tabLose) {
                    offerTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_red));
                }
            }
        });

        setupTabs();

        setLeftViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOfferHistoryActivity.start((BaseActivity) getActivity());
            }
        });
    }

    private void setupTabs() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels / 4.5);

        tabDoing = offerTab.newTab();
        tabItemDoing = new TabItem(getContext());
        tabItemDoing.setTitle(getString(R.string.offer_doing_tab, 0));
        tabItemDoing.setIcon(R.drawable.offer_tab_doing);
        tabItemDoing.titleView.setTextColor(getResources().getColorStateList(R.color.buy_doing_indicator_text_colors));
        tabItemDoing.setWidth(width);
        tabDoing.setCustomView(tabItemDoing);
        offerTab.addTab(tabDoing);

        tabWaiting = offerTab.newTab();
        tabItemWaiting = new TabItem(getContext());
        tabItemWaiting.setIcon(R.drawable.offer_tab_done);
        tabItemWaiting.setTitle(getString(R.string.offer_waiting_tab, 0));
        tabItemWaiting.titleView.setTextColor(getResources().getColorStateList(R.color.offer_waiting_indicator_text_colors));
        tabItemWaiting.setWidth(width);
        tabWaiting.setCustomView(tabItemWaiting);
        offerTab.addTab(tabWaiting);

        tabWin = offerTab.newTab();
        tabItemWin = new TabItem(getContext());
        tabItemWin.setIcon(R.drawable.offer_tab_win);
        tabItemWin.setTitle(getString(R.string.offer_win_tab, 0));
        tabItemWin.titleView.setTextColor(getResources().getColorStateList(R.color.buy_done_indicator_text_colors));
        tabItemWin.setWidth(width);
        tabWin.setCustomView(tabItemWin);
        offerTab.addTab(tabWin);


        tabLose = offerTab.newTab();
        tabItemLose = new TabItem(getContext());
        tabItemLose.setIcon(R.drawable.buy_tab_out_of_date);
        tabItemLose.setTitle(getString(R.string.offer_lose_tab, 0));
        tabItemLose.titleView.setTextColor(getResources().getColorStateList(R.color.buy_outofdate_indicator_text_colors));
        tabItemLose.setWidth(width);
        tabLose.setCustomView(tabItemLose);
        offerTab.addTab(tabLose);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadNumbers(MyOffersEvent myOffersEvent) {
        MyOffersResponse myOffersResponse = OfferManager.instance().myOffersResponse[myOffersEvent.offerStatus.ordinal()];
        getStrByStatus(myOffersEvent.offerStatus, myOffersResponse.maxCount);
    }

    public void getStrByStatus(OfferStatus offerStatus, long count) {
        switch (offerStatus) {
            case DOING:
                tabItemDoing.setTitle(getString(R.string.offer_doing_tab, count));
                break;
            case WAITING:
                tabItemWaiting.setTitle(getString(R.string.offer_waiting_tab, count));
                break;
            case WIN:
                tabItemWin.setTitle(getString(R.string.offer_win_tab, count));
                break;
            default:
                tabItemLose.setTitle(getString(R.string.offer_lose_tab, count));
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(OnOfferTabEvent onOfferTabEvent) {
        System.out.println("-----------------onOfferTabEvent");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

}
