package com.onefengma.taobuxiu.views.sales;

import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.BuyManager.BuyStatus;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.QtListResponse;
import com.onefengma.taobuxiu.model.entities.SalesIronsBuyResponse;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.sales.SalesBuyListEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesQtListEvent;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chufengma on 16/9/3.
 */
public class SalesBuyManager {

    private static SalesBuyManager instance;

    public SalesIronsBuyResponse salesIronsBuyResponses[] = new SalesIronsBuyResponse[3];

    public SalesBuyManager() {
        for (int i = 0; i < salesIronsBuyResponses.length; i++) {
            salesIronsBuyResponses[i] = new SalesIronsBuyResponse();
            salesIronsBuyResponses[i].currentPage = 0;
            salesIronsBuyResponses[i].pageCount = 15;
        }
    }

    public static SalesBuyManager instance() {
        if (instance == null) {
            instance = new SalesBuyManager();
        }
        return instance;
    }

    public void refreshQtList(final BuyStatus status) {
        readFromDB(status);
        EventBusHelper.post(new SalesBuyListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD, status));
        SalesIronsBuyResponse salesIronsBuyResponse = salesIronsBuyResponses[status.ordinal()];
        salesIronsBuyResponses[status.ordinal()].currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(SalesBuyService.class).getBuyList(salesIronsBuyResponse.currentPage, salesIronsBuyResponse.pageCount, status.getStatus())).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                SalesIronsBuyResponse list = JSONHelper.parse(data.data.toString(), SalesIronsBuyResponse.class);
                salesIronsBuyResponses[status.ordinal()] = list;

                // cache
                SPHelper.buy().save(getSPKey(status), list);

                // event
                EventBusHelper.post(new SalesBuyListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD, status));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new SalesBuyListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD, status));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void loadMoreQtList(final BuyStatus status) {
        EventBusHelper.post(new SalesBuyListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE, status));
        final SalesIronsBuyResponse salesIronsBuyResponse = salesIronsBuyResponses[status.ordinal()];

        HttpHelper.wrap(HttpHelper.create(SalesBuyService.class).getBuyList(salesIronsBuyResponse.currentPage, salesIronsBuyResponse.pageCount, status.getStatus())).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                SalesIronsBuyResponse list = JSONHelper.parse(data.data.toString(), SalesIronsBuyResponse.class);
                salesIronsBuyResponse.maxCount = list.maxCount;
                if (salesIronsBuyResponse.buys == null) {
                    salesIronsBuyResponse.buys = list.buys;
                } else  if (list.buys != null) {
                    salesIronsBuyResponse.buys.addAll(list.buys);
                }
                // event
                EventBusHelper.post(new SalesBuyListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE, status));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new SalesBuyListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE, status));
                super.onFailed(baseResponse, e);
            }
        });
    }


    public String getSPKey(BuyStatus status) {
        switch (status) {
            case DOING:return Constant.StorageKeys.SALES_BUY_DOING;
            case DONE:return Constant.StorageKeys.SALES_BUY_DONE;
            default :return Constant.StorageKeys.SALES_BUY_OUT_OF_DATE;
        }
    }

    private void readFromDB(BuyStatus status) {
        SalesIronsBuyResponse cache = SPHelper.buy().get(getSPKey(status), SalesIronsBuyResponse.class);
        if (cache != null) {
            salesIronsBuyResponses[status.ordinal()].buys = cache.buys;
            salesIronsBuyResponses[status.ordinal()].maxCount = cache.maxCount;
        }
    }

    public interface SalesBuyService {
        @GET("sales/buyList")
        Observable<BaseResponse> getBuyList(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount, @Query("status") int status);
    }

}