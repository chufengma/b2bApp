package com.onefengma.taobuxiu.manager;


import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.entities.MyIronsResponse;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.GetBuyNumbersEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.MyIronsEventDone;
import com.onefengma.taobuxiu.model.events.MyIronsEventOutOfDate;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.network.HttpHelper.SimpleNetworkSubscriber;
import com.onefengma.taobuxiu.utils.SPHelper;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_DOING;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_DOING_NUMBERS;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_DONE;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_DONE_NUMBERS;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_OUT_OF_DATE;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.BUY_OUT_OF_DATE_NUMBERS;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuyManager {

    private static BuyManager instance;

    public MyIronsResponse myIronsResponseForDoing;
    public MyIronsResponse myIronsResponseForDone;
    public MyIronsResponse myIronsResponseForOutOfDate;

    public enum BuyStatus {
        DOING,
        DONE,
        OUT_OF_DATE
    }

    public BuyManager() {
        this.myIronsResponseForDoing = new MyIronsResponse();
        this.myIronsResponseForDoing.currentPage = 0;
        this.myIronsResponseForDoing.pageCount = 15;

        this.myIronsResponseForDone = new MyIronsResponse();
        this.myIronsResponseForDone.currentPage = 0;
        this.myIronsResponseForDone.pageCount = 15;

        this.myIronsResponseForOutOfDate = new MyIronsResponse();
        this.myIronsResponseForOutOfDate.currentPage = 0;
        this.myIronsResponseForOutOfDate.pageCount = 15;
    }

    public static BuyManager instance() {
        if (instance == null) {
            instance = new BuyManager();
        }
        return instance;
    }

    public void reloadMyIronBuysForDoing() {
        readFromDB(BuyStatus.DOING);
        EventBusHelper.post(new MyIronsEventDoing(BaseStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
        myIronsResponseForDoing.currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForDoing.currentPage, myIronsResponseForDoing.pageCount, BuyStatus.DOING.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForDoing = myIronsResponse;

                // cache
                SPHelper.buy().save(BUY_DOING, myIronsResponse);
                SPHelper.buy().save(BUY_DOING_NUMBERS, myIronsResponse.maxCount);

                // event
                EventBusHelper.post(new MyIronsEventDoing(BaseStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronsEventDoing(BaseStatusEvent.FAILED, MyIronsEventDoing.RELOAD));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void lodeMoreMyIronBuysForDoing() {
        EventBusHelper.post(new MyIronsEventDoing(BaseStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForDoing.currentPage + 1, myIronsResponseForDoing.pageCount, BuyStatus.DOING.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForDoing.currentPage = myIronsResponse.currentPage;
                myIronsResponseForDoing.pageCount = myIronsResponse.pageCount;
                myIronsResponseForDoing.buys.addAll(myIronsResponse.buys);

                // cache
                SPHelper.buy().save(BUY_DOING_NUMBERS, myIronsResponse.maxCount);

                EventBusHelper.post(new MyIronsEventDoing(BaseStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronsEventDoing(BaseStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE));
            }
        });
    }

    public void reloadMyIronBuysForDone() {
        readFromDB(BuyStatus.DONE);
        EventBusHelper.post(new MyIronsEventDone(BaseStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
        myIronsResponseForDone.currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForDone.currentPage, myIronsResponseForDone.pageCount, BuyStatus.DONE.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForDone = myIronsResponse;

                // cache
                SPHelper.buy().save(BUY_DONE, myIronsResponse);
                SPHelper.buy().save(BUY_DONE_NUMBERS, myIronsResponse.maxCount);

                // event
                EventBusHelper.post(new MyIronsEventDone(BaseStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronsEventDone(BaseStatusEvent.FAILED, MyIronsEventDoing.RELOAD));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void lodeMoreMyIronBuysForDone() {
        EventBusHelper.post(new MyIronsEventDone(BaseStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForDone.currentPage + 1, myIronsResponseForDone.pageCount, BuyStatus.DONE.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForDone.currentPage = myIronsResponse.currentPage;
                myIronsResponseForDone.pageCount = myIronsResponse.pageCount;
                myIronsResponseForDone.buys.addAll(myIronsResponse.buys);

                // cache
                SPHelper.buy().save(BUY_DOING_NUMBERS, myIronsResponse.maxCount);

                EventBusHelper.post(new MyIronsEventDone(BaseStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronsEventDone(BaseStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE));
            }
        });
    }

    public void reloadMyIronBuysForOutForDate() {
        readFromDB(BuyStatus.OUT_OF_DATE);
        EventBusHelper.post(new MyIronsEventOutOfDate(BaseStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
        myIronsResponseForOutOfDate.currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForOutOfDate.currentPage, myIronsResponseForOutOfDate.pageCount, BuyStatus.OUT_OF_DATE.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForOutOfDate = myIronsResponse;

                // cache
                SPHelper.buy().save(BUY_OUT_OF_DATE, myIronsResponse);
                SPHelper.buy().save(BUY_OUT_OF_DATE_NUMBERS, myIronsResponse.maxCount);

                // event
                EventBusHelper.post(new MyIronsEventOutOfDate(BaseStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronsEventOutOfDate(BaseStatusEvent.FAILED, MyIronsEventDoing.RELOAD));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void lodeMoreMyIronBuysForOutForDate() {
        EventBusHelper.post(new MyIronsEventOutOfDate(BaseStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForOutOfDate.currentPage + 1, myIronsResponseForOutOfDate.pageCount, BuyStatus.OUT_OF_DATE.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForOutOfDate.currentPage = myIronsResponse.currentPage;
                myIronsResponseForOutOfDate.pageCount = myIronsResponse.pageCount;
                myIronsResponseForOutOfDate.buys.addAll(myIronsResponse.buys);

                // cache
                SPHelper.buy().save(BUY_OUT_OF_DATE, myIronsResponse.maxCount);

                EventBusHelper.post(new MyIronsEventOutOfDate(BaseStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronsEventOutOfDate(BaseStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE));
            }
        });
    }

    public interface BuyService {
        @GET("iron/myBuy")
        Observable<BaseResponse> myIronBuy(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount, @Query("status") int status);
    }

    public void getBuyNumbers() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                int numbersForDoing = SPHelper.buy().sp().getInt(BUY_DOING_NUMBERS, 0);
                int numbersForDone = SPHelper.buy().sp().getInt(BUY_DOING_NUMBERS, 0);
                int numbersForOutOfDate = SPHelper.buy().sp().getInt(BUY_DOING_NUMBERS, 0);
                EventBusHelper.post(new GetBuyNumbersEvent(numbersForDoing, numbersForDone, numbersForOutOfDate));
            }
        }).subscribe((Observer<? super Object>) Schedulers.io());
    }

    private void readFromDB(BuyStatus status) {
        if(myIronsResponseForDoing.buys == null && status == BuyStatus.DOING) {
            MyIronsResponse cache = SPHelper.buy().get(BUY_DOING, MyIronsResponse.class);
            if (cache != null) {
                myIronsResponseForDoing.buys = cache.buys;
                myIronsResponseForDoing.maxCount = cache.maxCount;
                myIronsResponseForDoing.newSupplyNums = cache.newSupplyNums;
            }
        }

        if(myIronsResponseForDone.buys == null && status == BuyStatus.DONE) {
            MyIronsResponse cache = SPHelper.buy().get(BUY_DONE, MyIronsResponse.class);
            if (cache != null) {
                myIronsResponseForDone.buys = cache.buys;
                myIronsResponseForDone.maxCount = cache.maxCount;
                myIronsResponseForDone.newSupplyNums = cache.newSupplyNums;
            }
        }

        if(myIronsResponseForOutOfDate.buys == null && status == BuyStatus.OUT_OF_DATE) {
            MyIronsResponse cache = SPHelper.buy().get(BUY_OUT_OF_DATE, MyIronsResponse.class);
            if (cache != null) {
                myIronsResponseForOutOfDate.buys = cache.buys;
                myIronsResponseForOutOfDate.maxCount = cache.maxCount;
                myIronsResponseForOutOfDate.newSupplyNums = cache.newSupplyNums;
            }
        }
    }

}
