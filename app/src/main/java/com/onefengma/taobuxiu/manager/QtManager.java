package com.onefengma.taobuxiu.manager;

import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.Constant.StorageBuyKeys;
import com.onefengma.taobuxiu.model.entities.MyIronsResponse;
import com.onefengma.taobuxiu.model.entities.QtListResponse;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.QtListEvent;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_DOING;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_DOING_NUMBERS;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.QT_LIST;

/**
 * Created by chufengma on 16/8/21.
 */
public class QtManager {
    private static QtManager instance;

    public QtListResponse qtListResponse;

    public QtManager() {
        qtListResponse = new QtListResponse();
        qtListResponse.currentPage = 0;
        qtListResponse.pageCount = 15;
    }

    public static QtManager instance() {
        if (instance == null) {
            instance = new QtManager();
        }
        return instance;
    }

    public void refreshQtList() {
        readFromDB();
        EventBusHelper.post(new QtListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
        qtListResponse.currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(QtService.class).qtList(qtListResponse.currentPage, qtListResponse.pageCount)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                QtListResponse list = JSONHelper.parse(data.data.toString(), QtListResponse.class);
                qtListResponse = list;

                // cache
                SPHelper.buy().save(QT_LIST, qtListResponse);

                // event
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void loadMoreQtList() {
        EventBusHelper.post(new QtListEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE));

        HttpHelper.wrap(HttpHelper.create(QtService.class).qtList(qtListResponse.currentPage, qtListResponse.pageCount)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                QtListResponse list = JSONHelper.parse(data.data.toString(), QtListResponse.class);
                if (list.qts != null) {
                    qtListResponse.qts.addAll(list.qts);
                    qtListResponse.maxCount = list.maxCount;
                }
                // event
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new QtListEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE));
                super.onFailed(baseResponse, e);
            }
        });
    }

    private void readFromDB() {
        if(qtListResponse.qts == null) {
            QtListResponse cache = SPHelper.buy().get(QT_LIST, QtListResponse.class);
            if (cache != null) {
                qtListResponse.qts = cache.qts;
                qtListResponse.maxCount = cache.maxCount;
            }
        }
    }

    public interface QtService {
        @GET("iron/qt")
        Observable<BaseResponse> qtList(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount);
    }
}
