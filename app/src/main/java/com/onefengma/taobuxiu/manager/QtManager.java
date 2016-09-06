package com.onefengma.taobuxiu.manager;

import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.QtListResponse;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.QtListEvent;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.views.sales.SalesQtManager.SalesQtStatus;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chufengma on 16/8/21.
 */
public class QtManager {
    private static QtManager instance;

    public QtListResponse qtListResponses[] = new QtListResponse[4];

    public QtManager() {
        for (int i = 0; i < qtListResponses.length; i++) {
            qtListResponses[i] = new QtListResponse();
            qtListResponses[i].currentPage = 0;
            qtListResponses[i].pageCount = 15;
        }
    }

    public static QtManager instance() {
        if (instance == null) {
            instance = new QtManager();
        }
        return instance;
    }

    public void refreshQtList(final SalesQtStatus status) {
        readFromDB(status);
        EventBusHelper.post(new QtListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD, status));
        QtListResponse qtListResponse = qtListResponses[status.ordinal()];
        qtListResponses[status.ordinal()].currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(QtService.class).qtList(qtListResponse.currentPage, qtListResponse.pageCount, status.status)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                QtListResponse list = JSONHelper.parse(data.data.toString(), QtListResponse.class);
                qtListResponses[status.ordinal()] = list;

                // cache
                SPHelper.buy().save(getSPKey(status), list);

                // event
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD, status));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD, status));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void loadMoreQtList(final SalesQtStatus status) {
        EventBusHelper.post(new QtListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE, status));
        final QtListResponse qtListResponse = qtListResponses[status.ordinal()];

        HttpHelper.wrap(HttpHelper.create(QtService.class).qtList(qtListResponse.currentPage, qtListResponse.pageCount, status.status)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
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
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE, status));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE, status));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public String getSPKey(SalesQtStatus status) {
        switch (status) {
            case QT_WAITING:return Constant.StorageKeys.QT_WAITING;
            case QT_DONE:return Constant.StorageKeys.QT_DONE;
            case QT_DOING:return Constant.StorageKeys.QT_DOING;
            default: QT_CANCEL:return Constant.StorageKeys.QT_CANCEL;
        }
    }

    private void readFromDB(SalesQtStatus status) {
        QtListResponse cache = SPHelper.buy().get(getSPKey(status), QtListResponse.class);
        if (cache != null) {
            qtListResponses[status.ordinal()].qts = cache.qts;
            qtListResponses[status.ordinal()].maxCount = cache.maxCount;
        }
    }

    public interface QtService {
        @GET("iron/qt")
        Observable<BaseResponse> qtList(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount, @Query("status") int status);
    }
}
