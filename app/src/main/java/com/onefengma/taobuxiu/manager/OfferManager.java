package com.onefengma.taobuxiu.manager;

import com.alibaba.fastjson.JSON;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.entities.MyOfferHistoryInfo;
import com.onefengma.taobuxiu.model.entities.MyOffersResponse;
import com.onefengma.taobuxiu.model.entities.OfferDetail;
import com.onefengma.taobuxiu.model.entities.SubscribeInfo;
import com.onefengma.taobuxiu.model.events.ActionSupplyEvent;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.GetSubscribeInfoEvent;
import com.onefengma.taobuxiu.model.events.MyIronDetailEvent;
import com.onefengma.taobuxiu.model.events.MyIronOfferHistoryEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEventDoing;
import com.onefengma.taobuxiu.model.events.MyOfferDetailEvent;
import com.onefengma.taobuxiu.model.events.MyOffersEvent;
import com.onefengma.taobuxiu.model.events.UpdateSubscribeInfoEvent;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.views.buys.PushNewBuyActivity;
import com.onefengma.taobuxiu.views.core.BaseActivity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_DOING;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_DOING_NUMBERS;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_LOSE;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_LOSE_NUMBERS;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_WAITING;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_WAITING_NUMBERS;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_WIN;
import static com.onefengma.taobuxiu.model.Constant.StorageBuyKeys.OFFER_WIN_NUMBERS;

/**
 * Created by chufengma on 16/8/27.
 */
public class OfferManager {
    private static OfferManager instance;

    public MyOffersResponse myOffersResponse[] = new MyOffersResponse[4];

    public enum OfferStatus {
        DOING(0),
        WAITING(3),
        WIN(4),
        LOSE(6);

        int status;

        OfferStatus(int status) {
            this.status = status;
        }
    }

    public static OfferManager instance() {
        if (instance == null) {
            instance = new OfferManager();
        }
        return instance;
    }

    public OfferManager() {
        for (int i = 0; i < myOffersResponse.length; i++) {
            myOffersResponse[i] = new MyOffersResponse();
            myOffersResponse[i].currentPage = 0;
            myOffersResponse[i].pageCount = 15;
        }
    }

    public void reloadMyOffers(final OfferStatus offerStatus) {
        if (!AuthManager.instance().sellerCheck()) {
            return;
        }
        readFromDB(offerStatus);
        MyOffersResponse myOffersResponseDoing = myOffersResponse[offerStatus.ordinal()];

        EventBusHelper.post(new MyIronsEventDoing(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD));
        myOffersResponseDoing.currentPage = 0;

        HttpHelper.wrap(HttpHelper.create(OfferService.class).myIronOffers(myOffersResponseDoing.currentPage, myOffersResponseDoing.pageCount, offerStatus.status)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyOffersResponse offersResponse = JSONHelper.parse(data.data.toString(), MyOffersResponse.class);
                myOffersResponse[offerStatus.ordinal()] = offersResponse;

                // cache
                DataKeyItem dataKeyItem = getDataKey(offerStatus);
                SPHelper.buy().save(dataKeyItem.dataKey, offersResponse);
                SPHelper.buy().save(dataKeyItem.dataNumKey, offersResponse.maxCount);

                // event
                EventBusHelper.post(new MyOffersEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD, offerStatus));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyOffersEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD, offerStatus));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void loadMoreOffers(final OfferStatus offerStatus) {
        if (!AuthManager.instance().sellerCheck()) {
            return;
        }
        MyOffersResponse myOffersResponseDoing = myOffersResponse[offerStatus.ordinal()];
        EventBusHelper.post(new MyOffersEvent(BaseListStatusEvent.STARTED, MyIronsEventDoing.RELOAD, offerStatus));
        HttpHelper.wrap(HttpHelper.create(OfferService.class).myIronOffers(myOffersResponseDoing.currentPage, myOffersResponseDoing.pageCount, offerStatus.status)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyOffersResponse offersResponse = JSONHelper.parse(data.data.toString(), MyOffersResponse.class);
                MyOffersResponse cachedOffer = OfferManager.this.myOffersResponse[offerStatus.ordinal()];

                cachedOffer.currentPage = offersResponse.currentPage;
                cachedOffer.pageCount = offersResponse.pageCount;
                cachedOffer.maxCount = offersResponse.maxCount;
                if (offersResponse.buys != null) {
                    cachedOffer.buys.addAll(offersResponse.buys);
                }

                // cache
                DataKeyItem dataKeyItem = getDataKey(offerStatus);
                SPHelper.buy().save(dataKeyItem.dataKey, offersResponse);
                SPHelper.buy().save(dataKeyItem.dataNumKey, offersResponse.maxCount);

                // event
                EventBusHelper.post(new MyOffersEvent(BaseListStatusEvent.SUCCESS, MyIronsEventDoing.RELOAD, offerStatus));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyOffersEvent(BaseListStatusEvent.FAILED, MyIronsEventDoing.RELOAD, offerStatus));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void loadIronOfferDetail(String ironId) {
        EventBusHelper.post(new MyIronDetailEvent(BaseStatusEvent.STARTED, null));
        HttpHelper.wrap(HttpHelper.create(OfferService.class).myIronOfferDetails(ironId)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                OfferDetail offerDetail = JSON.parseObject(data.data.toString(), OfferDetail.class);
                EventBusHelper.post(new MyOfferDetailEvent(BaseStatusEvent.SUCCESS, offerDetail));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyOfferDetailEvent(BaseStatusEvent.FAILED, null));
            }
        });
    }

    public void offerIronBuy(final String ironId, float price, String message, String unit) {
        EventBusHelper.post(new ActionSupplyEvent(BaseStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(OfferService.class).offerIronBuy(ironId, price, message, unit)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                EventBusHelper.post(new ActionSupplyEvent(BaseStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new ActionSupplyEvent(BaseStatusEvent.FAILED));
            }
        });
    }

    public void getMySubscribeInfo() {
        EventBusHelper.post(new GetSubscribeInfoEvent(BaseStatusEvent.STARTED, null));
        HttpHelper.wrap(HttpHelper.create(OfferService.class).mySubscribeInfo()).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                SubscribeInfo subscribeInfo = JSON.parseObject(data.data.toString(), SubscribeInfo.class);
                EventBusHelper.post(new GetSubscribeInfoEvent(BaseStatusEvent.SUCCESS, subscribeInfo));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new GetSubscribeInfoEvent(BaseStatusEvent.FAILED, null));
            }
        });
    }

    public void updateMySubscribeInfo(SubscribeInfo subscribeInfo) {
        EventBusHelper.post(new UpdateSubscribeInfoEvent(BaseStatusEvent.STARTED));

        String types = "";
        String surfaces = "";
        String materials = "";
        String proPlaces = "";

        try {
            types = JSON.toJSONString(subscribeInfo.types);
            surfaces = JSON.toJSONString(subscribeInfo.surfaces);
            materials = JSON.toJSONString(subscribeInfo.materials);
            proPlaces = JSON.toJSONString(subscribeInfo.proPlaces);
        } catch (Exception e) {}

        HttpHelper.wrap(HttpHelper.create(OfferService.class).updateSubscribeInfo(types, surfaces, materials, proPlaces)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                EventBusHelper.post(new UpdateSubscribeInfoEvent(BaseStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new UpdateSubscribeInfoEvent(BaseStatusEvent.FAILED));
            }
        });
    }

    private void readFromDB(OfferStatus status) {
        MyOffersResponse cache = SPHelper.buy().get(getDataKey(status).dataKey, MyOffersResponse.class);
        if (cache != null) {
            myOffersResponse[status.ordinal()].buys = cache.buys;
            myOffersResponse[status.ordinal()].newWinNums = cache.newWinNums;
            myOffersResponse[status.ordinal()].maxCount = cache.maxCount;
        }
    }

    public DataKeyItem getDataKey(OfferStatus offerStatus) {
        String dataKey;
        String dataNumKey;
        switch (offerStatus) {
            case DOING:
                dataKey = OFFER_DOING;
                dataNumKey = OFFER_DOING_NUMBERS;
                break;
            case WAITING:
                dataKey = OFFER_WAITING;
                dataNumKey = OFFER_WAITING_NUMBERS;
                break;
            case WIN:
                dataKey = OFFER_WIN;
                dataNumKey = OFFER_WIN_NUMBERS;
                break;
            default:
                dataKey = OFFER_LOSE;
                dataNumKey = OFFER_LOSE_NUMBERS;
                break;
        }
        return new DataKeyItem(dataKey, dataNumKey);
    }


    public void getIronOfferHistroy() {
        EventBusHelper.post(new MyIronOfferHistoryEvent(BaseStatusEvent.STARTED, null));
        HttpHelper.wrap(HttpHelper.create(OfferService.class).getMyIronOfferHistory()).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyOfferHistoryInfo myOfferHistoryInfo = JSON.parseObject(data.data.toString(), MyOfferHistoryInfo.class);
                EventBusHelper.post(new MyIronOfferHistoryEvent(BaseStatusEvent.SUCCESS, myOfferHistoryInfo));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronOfferHistoryEvent(BaseStatusEvent.FAILED, null));
            }
        });
    }

    public interface OfferService {
        @GET("seller/myIronBuys")
        Observable<BaseResponse> myIronOffers(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount, @Query("status") int status);

        @GET("seller/myIronBuyDetail")
        Observable<BaseResponse> myIronOfferDetails(@Query("ironId") String ironId);


        @FormUrlEncoded
        @POST("seller/offerIronBuy")
        Observable<BaseResponse> offerIronBuy(@Field("ironId") String ironId, @Field("price") float price, @Field("msg") String message, @Field("unit") String unit);

        @FormUrlEncoded
        @POST("member/ironSubscribe")
        Observable<BaseResponse> updateSubscribeInfo(@Field("types") String types,
                                                     @Field("surfaces") String surfaces,
                                                     @Field("materials") String materials,
                                                     @Field("proPlaces") String proPlaces);

        @GET("member/ironSubscribe")
        Observable<BaseResponse> mySubscribeInfo();


        @GET("iron/myIronBuyHistory")
        Observable<BaseResponse> getMyIronOfferHistory();
    }

    public class DataKeyItem {
        public String dataKey;
        public String dataNumKey;

        public DataKeyItem(String dataKey, String dataNumKey) {
            this.dataKey = dataKey;
            this.dataNumKey = dataNumKey;
        }
    }

}
