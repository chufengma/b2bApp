package com.onefengma.taobuxiu.views.sales;

import com.alibaba.fastjson.JSON;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.QtListResponse;
import com.onefengma.taobuxiu.model.entities.SalesIronBuyDetail;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.sales.SalesActionQtEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesIronDetailEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesQtListEvent;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chufengma on 16/9/3.
 */
public class SalesQtManager {

    private static SalesQtManager instance;

    public QtListResponse qtListResponses[] = new QtListResponse[4];

    public enum SalesQtStatus {
        QT_WAITING(0),
        QT_DONE(1),
        QT_CANCEL(2),
        QT_DOING(3);

        public int status;

        SalesQtStatus(int status) {
            this.status = status;
        }
    }

    public static SalesQtManager instance() {
        if (instance == null) {
            instance = new SalesQtManager();
        }
        return instance;
    }

    public SalesQtManager() {
        for (int i = 0; i < qtListResponses.length; i++) {
            qtListResponses[i] = new QtListResponse();
            qtListResponses[i].currentPage = 0;
            qtListResponses[i].pageCount = 15;
        }
    }

    public void refreshQtList(final SalesQtStatus status) {
        readFromDB(status);
        EventBusHelper.post(new SalesQtListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD, status));
        QtListResponse qtListResponse = qtListResponses[status.ordinal()];
        qtListResponses[status.ordinal()].currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(SalesQtService.class).qtList(qtListResponse.currentPage, qtListResponse.pageCount, status.status)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                QtListResponse list = JSONHelper.parse(data.data.toString(), QtListResponse.class);
                qtListResponses[status.ordinal()] = list;

                // cache
                SPHelper.buy().save(getSPKey(status), list);

                // event
                EventBusHelper.post(new SalesQtListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD, status));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new SalesQtListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD, status));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void loadMoreQtList(final SalesQtStatus status) {
        EventBusHelper.post(new SalesQtListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE, status));
        final QtListResponse qtListResponse = qtListResponses[status.ordinal()];

        HttpHelper.wrap(HttpHelper.create(SalesQtService.class).qtList(qtListResponse.currentPage, qtListResponse.pageCount, status.status)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                QtListResponse list = JSONHelper.parse(data.data.toString(), QtListResponse.class);
                qtListResponse.maxCount = list.maxCount;
                if (qtListResponse.qts == null) {
                    qtListResponse.qts = list.qts;
                } else  if (list.qts != null) {
                    qtListResponse.qts.addAll(list.qts);
                }
                // event
                EventBusHelper.post(new SalesQtListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE, status));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new SalesQtListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE, status));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void loadIronBuyDetail(String ironId) {
        EventBusHelper.post(new SalesIronDetailEvent(BaseStatusEvent.STARTED, null));
        HttpHelper.wrap(HttpHelper.create(SalesQtService.class).getIronDetail(ironId)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                SalesIronBuyDetail myIronBuyDetail = JSON.parseObject(data.data.toString(), SalesIronBuyDetail.class);
                EventBusHelper.post(new SalesIronDetailEvent(BaseStatusEvent.SUCCESS, myIronBuyDetail));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new SalesIronDetailEvent(BaseStatusEvent.FAILED, null));
            }
        });
    }

    public void doneQt(String qtId) {
        actionQt(qtId, 1);
    }

    public void cancelQt(String qtId) {
        actionQt(qtId, 2);
    }

    public void startQt(String qtId) {
        actionQt(qtId, 3);
    }

    private void actionQt(String qtId, final int status) {
        EventBusHelper.post(new SalesActionQtEvent(BaseStatusEvent.STARTED, status));
        HttpHelper.wrap(HttpHelper.create(SalesQtService.class).updateQtStatus(qtId, status)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                EventBusHelper.post(new SalesActionQtEvent(BaseStatusEvent.SUCCESS, status));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new SalesActionQtEvent(BaseStatusEvent.FAILED, status));
            }
        });
    }

    public String getSPKey(SalesQtStatus status) {
        switch (status) {
            case QT_WAITING:return Constant.StorageKeys.SALES_QT_WAITING;
            case QT_DONE:return Constant.StorageKeys.SALES_QT_DONE;
            case QT_DOING:return Constant.StorageKeys.SALES_QT_DOING;
            default: QT_CANCEL:return Constant.StorageKeys.SALES_QT_CANCEL;
        }
    }

    private void readFromDB(SalesQtStatus status) {
        QtListResponse cache = SPHelper.buy().get(getSPKey(status), QtListResponse.class);
        if (cache != null) {
            qtListResponses[status.ordinal()].qts = cache.qts;
            qtListResponses[status.ordinal()].maxCount = cache.maxCount;
        }
    }


    public interface SalesQtService {

        @FormUrlEncoded
        @POST("sales/updateQtStatus")
        Observable<BaseResponse> updateQtStatus(@Field(("qtId")) String qtId, @Field(("status"))int status);

        @GET("sales/ironDetail")
        Observable<BaseResponse> getIronDetail(@Query(("ironId")) String ironId);

        @GET("sales/qtList")
        Observable<BaseResponse> qtList(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount, @Query("status")  int status);

    }

}
