package com.onefengma.taobuxiu.manager;


import android.app.Activity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jaydenxiao.guider.HighLightGuideView;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.IronBuyPush;
import com.onefengma.taobuxiu.model.entities.MyBuyHistoryInfo;
import com.onefengma.taobuxiu.model.entities.MyIronBuyDetail;
import com.onefengma.taobuxiu.model.entities.MyIronBuysNewNums;
import com.onefengma.taobuxiu.model.entities.MyIronsResponse;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.DeleteIronBuyEvent;
import com.onefengma.taobuxiu.model.events.GetBuyNumbersEvent;
import com.onefengma.taobuxiu.model.events.IronBuyPushEvent;
import com.onefengma.taobuxiu.model.events.MyIronBuyHistoryEvent;
import com.onefengma.taobuxiu.model.events.MyIronDetailEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.MyIronsEventDone;
import com.onefengma.taobuxiu.model.events.MyIronsEventOutOfDate;
import com.onefengma.taobuxiu.model.events.QtIronBuyEvent;
import com.onefengma.taobuxiu.model.events.SelectSupplyEvent;
import com.onefengma.taobuxiu.model.push.BuyPushData;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.network.HttpHelper.SimpleNetworkSubscriber;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    private boolean isBuyRefresing = false;

    public List<IronBuyPush> ironBuyPushList = new ArrayList<>();

    public enum BuyStatus {
        DOING(0),
        DONE(1),
        OUT_OF_DATE(2);
        int status;
        BuyStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
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

        EventBusHelper.register(this);
    }

    public static BuyManager instance() {
        if (instance == null) {
            instance = new BuyManager();
        }
        return instance;
    }

    public void reloadAllStatusBuys() {
        if (isBuyRefresing) {
            return;
        }
        reloadMyIronBuysForDoing();
        reloadMyIronBuysForDone();
        reloadMyIronBuysForOutForDate();
    }

    public void reloadMyIronBuysForDoing() {
        readFromDB(BuyStatus.DOING);
        EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
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
                EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void lodeMoreMyIronBuysForDoing() {
        EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForDoing.currentPage + 1, myIronsResponseForDoing.pageCount, BuyStatus.DOING.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForDoing.currentPage = myIronsResponse.currentPage;
                myIronsResponseForDoing.pageCount = myIronsResponse.pageCount;
                if (myIronsResponse.buys != null) {
                    myIronsResponseForDoing.buys.addAll(myIronsResponse.buys);
                }


                // cache
                SPHelper.buy().save(BUY_DOING_NUMBERS, myIronsResponse.maxCount);

                EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE));
            }
        });
    }

    public void reloadMyIronBuysForDone() {
        readFromDB(BuyStatus.DONE);
        EventBusHelper.post(new MyIronsEventDone(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
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
                EventBusHelper.post(new MyIronsEventDone(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronsEventDone(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void lodeMoreMyIronBuysForDone() {
        EventBusHelper.post(new MyIronsEventDone(BaseListStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForDone.currentPage + 1, myIronsResponseForDone.pageCount, BuyStatus.DONE.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForDone.currentPage = myIronsResponse.currentPage;
                myIronsResponseForDone.pageCount = myIronsResponse.pageCount;
                if (myIronsResponse.buys != null) {
                    myIronsResponseForDone.buys.addAll(myIronsResponse.buys);
                }

                // cache
                SPHelper.buy().save(BUY_DOING_NUMBERS, myIronsResponse.maxCount);

                EventBusHelper.post(new MyIronsEventDone(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronsEventDone(BaseListStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE));
            }
        });
    }

    public void reloadMyIronBuysForOutForDate() {
        readFromDB(BuyStatus.OUT_OF_DATE);
        EventBusHelper.post(new MyIronsEventOutOfDate(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
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
                EventBusHelper.post(new MyIronsEventOutOfDate(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD));
                isBuyRefresing = false;
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronsEventOutOfDate(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD));
                isBuyRefresing = false;
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void lodeMoreMyIronBuysForOutForDate() {
        EventBusHelper.post(new MyIronsEventOutOfDate(BaseListStatusEvent.STARTED, MyIronsEventDoing.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(myIronsResponseForOutOfDate.currentPage + 1, myIronsResponseForOutOfDate.pageCount, BuyStatus.OUT_OF_DATE.ordinal())).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                myIronsResponseForOutOfDate.currentPage = myIronsResponse.currentPage;
                myIronsResponseForOutOfDate.pageCount = myIronsResponse.pageCount;
                if (myIronsResponse.buys != null) {
                    myIronsResponseForOutOfDate.buys.addAll(myIronsResponse.buys);
                }

                // cache
                SPHelper.buy().save(BUY_OUT_OF_DATE, myIronsResponse.maxCount);

                EventBusHelper.post(new MyIronsEventOutOfDate(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronsEventOutOfDate(BaseListStatusEvent.FAILED, MyIronsEventDoing.LOAD_MORE));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuyPushEvent(BuyPushData pushData) {
        myIronsResponseForDoing.newSupplyNums = pushData.newSupplyNums;
        if (myIronsResponseForDoing.buys != null) {
            for (IronBuyBrief ironBuyBrief : myIronsResponseForDoing.buys) {
                if (StringUtils.equals(ironBuyBrief.id, pushData.ironBuyBrief.id)) {
                    ironBuyBrief.newSupplyNum = pushData.ironBuyBrief.newSupplyNum;
                    ironBuyBrief.supplyCount = pushData.ironBuyBrief.supplyCount;
                }
            }
        }
        EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
    }

    public void loadBuyNewNums() {
        HttpHelper.wrap(HttpHelper.create(BuyService.class).buyNewNums()).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronBuysNewNums newSupplyNums = JSON.parseObject(data.data.toString(), MyIronBuysNewNums.class);
                myIronsResponseForDoing.newSupplyNums = newSupplyNums.newSupplyNums;
                EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
            }
        });
    }

    public void loadIronBuyDetail(String ironId) {
        EventBusHelper.post(new MyIronDetailEvent(BaseStatusEvent.STARTED, null));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronDetail(ironId)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronBuyDetail myIronBuyDetail = JSON.parseObject(data.data.toString(), MyIronBuyDetail.class);
                EventBusHelper.post(new MyIronDetailEvent(BaseStatusEvent.SUCCESS, myIronBuyDetail));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronDetailEvent(BaseStatusEvent.FAILED, null));
            }
        });
    }

    public void selectSupply(final String ironId, String supplyId, final float totalMoney) {
        EventBusHelper.post(new SelectSupplyEvent(BaseStatusEvent.STARTED, totalMoney, ironId));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).selectSupply(supplyId, ironId)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                EventBusHelper.post(new SelectSupplyEvent(BaseStatusEvent.SUCCESS, totalMoney, ironId));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new SelectSupplyEvent(BaseStatusEvent.FAILED, totalMoney, ironId));
            }
        });
    }

    public void deleteIronBuy(String ironId) {
        EventBusHelper.post(new DeleteIronBuyEvent(BaseStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).deleteIronBuy(ironId)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                EventBusHelper.post(new DeleteIronBuyEvent(BaseStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new DeleteIronBuyEvent(BaseStatusEvent.FAILED));
            }
        });
    }

    public void qtIronBuy(String ironId) {
        EventBusHelper.post(new QtIronBuyEvent(BaseStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).qtIronBuy(ironId)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                EventBusHelper.post(new QtIronBuyEvent(BaseStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new QtIronBuyEvent(BaseStatusEvent.FAILED));
            }
        });
    }

    public void doPushAllIronBuy() {
        List<IronBuyPush> pushs = getCachedIronBuys();
        IronBuyPush[] pushArray = new IronBuyPush[pushs.size()];
        for (int i = 0; i < pushs.size(); i++) {
            pushArray[i] = pushs.get(i);
        }
        for (IronBuyPush push : pushArray) {
            doPushIronBuy(push);
        }
    }

    public void doRePushIronBuy(IronBuyPush push) {
        if (push.timeLimit <= 0) {
            ToastUtils.showInfoTasty("时间期限必须大于0");
            return;
        }
        EventBusHelper.post(new IronBuyPushEvent(BaseStatusEvent.STARTED, true));

        HttpHelper.wrap(HttpHelper.create(BuyService.class)
                .reBuyPush(push.ironId, push.ironType, push.material, push.surface, push.proPlace, push.locationCityId, push.message, push.length, push.width, push.height, push.toleranceFrom, push.toleranceTo, push.numbers, push.timeLimit, push.unit))
                .subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        EventBusHelper.post(new IronBuyPushEvent(BaseStatusEvent.SUCCESS, true));
                    }

                    @Override
                    public void onFailed(BaseResponse baseResponse, Throwable e) {
                        super.onFailed(baseResponse, e);
                        EventBusHelper.post(new IronBuyPushEvent(BaseStatusEvent.FAILED, true));
                    }
                });
    }

    public void doPushIronBuy(IronBuyPush push) {
        deleteIronBuy(push);

        if (push.timeLimit <= 0) {
            ToastUtils.showInfoTasty("时间期限必须大于0");
            return;
        }
        EventBusHelper.post(new IronBuyPushEvent(BaseStatusEvent.STARTED));

        HttpHelper.wrap(HttpHelper.create(BuyService.class)
                .ironBuyPush(push.ironType, push.material, push.surface, push.proPlace, push.locationCityId, push.message, push.length, push.width, push.height, push.toleranceFrom, push.toleranceTo, push.numbers, push.timeLimit, push.unit))
                .subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        EventBusHelper.post(new IronBuyPushEvent(BaseStatusEvent.SUCCESS));
                    }

                    @Override
                    public void onFailed(BaseResponse baseResponse, Throwable e) {
                        super.onFailed(baseResponse, e);
                        EventBusHelper.post(new IronBuyPushEvent(BaseStatusEvent.FAILED));
                    }
                });
    }

    public List<IronBuyPush> getCachedIronBuys() {
        List<IronBuyPush> pushs = SPHelper.buy().get(Constant.StorageBuyKeys.CACHED_IRON_PUSH, new TypeReference<List<IronBuyPush>>() {
        });
        if (pushs != null) {
            ironBuyPushList.clear();
            ironBuyPushList.addAll(pushs);
        }
        return ironBuyPushList;
    }

    public void getIronBuyHistroy() {
        EventBusHelper.post(new MyIronBuyHistoryEvent(BaseStatusEvent.STARTED, null));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).getMyIronBuyHistory()).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyBuyHistoryInfo myBuyHistoryInfo = JSON.parseObject(data.data.toString(), MyBuyHistoryInfo.class);
                EventBusHelper.post(new MyIronBuyHistoryEvent(BaseStatusEvent.SUCCESS, myBuyHistoryInfo));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronBuyHistoryEvent(BaseStatusEvent.FAILED, null));
            }
        });
    }

    public void saveIronBuy(IronBuyPush push) {
        deleteIronBuy(push);
        ironBuyPushList.add(0, push);
        SPHelper.buy().save(Constant.StorageBuyKeys.CACHED_IRON_PUSH, ironBuyPushList);
    }

    public void deleteIronBuy(IronBuyPush push) {
        int index = -1;
        for (int i = 0; i < ironBuyPushList.size(); i++) {
            IronBuyPush item = ironBuyPushList.get(i);
            if (item.id == push.id) {
                index = i;
            }
        }
        if (index != -1) {
            ironBuyPushList.remove(index);
        }
        SPHelper.buy().save(Constant.StorageBuyKeys.CACHED_IRON_PUSH, ironBuyPushList);
    }

    public interface BuyService {
        @GET("iron/myBuy")
        Observable<BaseResponse> myIronBuy(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount, @Query("status") int status);

        @GET("iron/newBuyNums")
        Observable<BaseResponse> buyNewNums();

        @GET("iron/myBuyDetail")
        Observable<BaseResponse> myIronDetail(@Query(("ironId")) String ironId);

        @GET("iron/myIronBuyHistory")
        Observable<BaseResponse> getMyIronBuyHistory();

        @FormUrlEncoded
        @POST("iron/selectSupply")
        Observable<BaseResponse> selectSupply(@Field(("supplyId")) String supplyId, @Field(("ironBuyId")) String ironBuyId);

        @FormUrlEncoded
        @POST("iron/deleteIronBuy")
        Observable<BaseResponse> deleteIronBuy(@Field(("ironId")) String ironId);

        @FormUrlEncoded
        @POST("iron/qt")
        Observable<BaseResponse> qtIronBuy(@Field(("ironId")) String ironId);

        @FormUrlEncoded
        @POST("iron/buy")
        Observable<BaseResponse> ironBuyPush(@Field(("ironType")) String ironType,
                                             @Field(("material")) String material,
                                             @Field(("surface")) String surface,
                                             @Field(("proPlace")) String proPlace,
                                             @Field(("locationCityId")) String locationCityId,
                                             @Field(("message")) String message,
                                             @Field(("length")) float length,
                                             @Field(("width")) float width,
                                             @Field(("height")) float height,
                                             @Field(("toleranceFrom")) float toleranceFrom,
                                             @Field(("toleranceTo")) float toleranceTo,
                                             @Field(("numbers")) float numbers,
                                             @Field(("timeLimit")) long timeLimit,
                                             @Field(("unit")) String unit);

        @FormUrlEncoded
        @POST("iron/editBuy")
        Observable<BaseResponse> reBuyPush(@Field(("ironId")) String ironId, @Field(("ironType")) String ironType,
                                             @Field(("material")) String material,
                                             @Field(("surface")) String surface,
                                             @Field(("proPlace")) String proPlace,
                                             @Field(("locationCityId")) String locationCityId,
                                             @Field(("message")) String message,
                                             @Field(("length")) float length,
                                             @Field(("width")) float width,
                                             @Field(("height")) float height,
                                             @Field(("toleranceFrom")) float toleranceFrom,
                                             @Field(("toleranceTo")) float toleranceTo,
                                             @Field(("numbers")) float numbers,
                                             @Field(("timeLimit")) long timeLimit,
                                             @Field(("unit")) String unit);
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
        if (myIronsResponseForDoing.buys == null && status == BuyStatus.DOING) {
            MyIronsResponse cache = SPHelper.buy().get(BUY_DOING, MyIronsResponse.class);
            if (cache != null) {
                myIronsResponseForDoing.buys = cache.buys;
                myIronsResponseForDoing.maxCount = cache.maxCount;
                myIronsResponseForDoing.newSupplyNums = cache.newSupplyNums;
            }
        }

        if (myIronsResponseForDone.buys == null && status == BuyStatus.DONE) {
            MyIronsResponse cache = SPHelper.buy().get(BUY_DONE, MyIronsResponse.class);
            if (cache != null) {
                myIronsResponseForDone.buys = cache.buys;
                myIronsResponseForDone.maxCount = cache.maxCount;
                myIronsResponseForDone.newSupplyNums = cache.newSupplyNums;
            }
        }

        if (myIronsResponseForOutOfDate.buys == null && status == BuyStatus.OUT_OF_DATE) {
            MyIronsResponse cache = SPHelper.buy().get(BUY_OUT_OF_DATE, MyIronsResponse.class);
            if (cache != null) {
                myIronsResponseForOutOfDate.buys = cache.buys;
                myIronsResponseForOutOfDate.maxCount = cache.maxCount;
                myIronsResponseForOutOfDate.newSupplyNums = cache.newSupplyNums;
            }
        }
    }

    public void showBuyGuidance(Activity activity, View view) {
        if (!SPHelper.top().sp().getBoolean(Constant.StorageKeys.SETTING_BUY_GUIDANCE, false)) {
            HighLightGuideView.builder(activity)
                    .addHighLightGuidView(view, R.drawable.ic_launcher)
                    .setHighLightStyle(HighLightGuideView.VIEWSTYLE_CIRCLE)
                    .show();
            SPHelper.top().sp().edit().putBoolean(Constant.StorageKeys.SETTING_BUY_GUIDANCE, true).commit();
        }
    }


    public void showBuyDetailGuidance(Activity activity) {
        if (!SPHelper.top().sp().getBoolean(Constant.StorageKeys.SETTING_BUY_DETAIL_GUIDANCE, false)) {
            HighLightGuideView.builder(activity)
                    .addNoHighLightGuidView(R.drawable.ic_mine_pressed)
                    .show();
            SPHelper.top().sp().edit().putBoolean(Constant.StorageKeys.SETTING_BUY_DETAIL_GUIDANCE, true).commit();
        }
    }

}
