package com.onefengma.taobuxiu.views.offers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.OfferManager;
import com.onefengma.taobuxiu.manager.OfferManager.OfferStatus;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.entities.MyOffersResponse;
import com.onefengma.taobuxiu.model.events.MyOffersEvent;
import com.onefengma.taobuxiu.views.buys.BuyListAdapter;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chufengma on 16/8/27.
 */
public class BaseOfferStatusFragment extends BaseFragment {

    public static final String OFFER_STATUS = "offer_status";

    OfferListAdapter customAdapter;

    @BindView(R.id.recycler_view)
    XListView recyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;

    private OfferStatus offerStatus;

    @Override
    public void onResume() {
        super.onResume();
        EventBusHelper.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusHelper.unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offerStatus = OfferStatus.values()[getArguments().getInt(OFFER_STATUS)];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_sub, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (AuthManager.instance().sellerCheck()) {
            recyclerView.fakePullRefresh();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customAdapter = new OfferListAdapter();
        recyclerView.setAdapter(customAdapter);

        recyclerView.setOnRefreshListener(new XListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OfferManager.instance().reloadMyOffers(offerStatus);
            }
        });

        recyclerView.setOnLoadMoreListener(new XListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                OfferManager.instance().loadMoreOffers(offerStatus);
            }
        });

        recyclerView.enableLoadMore(true);

        recyclerView.setEmptyView(emptyView);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @OnClick(R.id.emptyView)
    public void clickOnEmptyView() {
        if (AuthManager.instance().sellerCheck()) {
            recyclerView.fakePullRefresh();
        }
    }

    @Subscribe
    public void onLoadMyOffers(MyOffersEvent offersEvent) {
        if (offersEvent.offerStatus != offerStatus) {
            return;
        }

        if (offersEvent.isRefreshComplete()) {
            recyclerView.onRefreshComplete(false);
        }

        if (offersEvent.isLoadComplete()) {
            recyclerView.onLoadMoreComplete();
        }

        MyOffersResponse myOffersResponse = OfferManager.instance().myOffersResponse[offerStatus.ordinal()];

        recyclerView.enableLoadMore(myOffersResponse.buys != null
                && myOffersResponse.buys.size() > 0
                && myOffersResponse.buys.size() % 15 == 0);

        customAdapter.setMyBuys(myOffersResponse.buys);
    }

}
