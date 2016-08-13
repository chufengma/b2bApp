package com.onefengma.taobuxiu.manager;


import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.network.HttpHelper.SimpleNetworkSubscriber;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.entities.IronBuyBrief;
import com.onefengma.taobuxiu.model.entities.MyIronsResponse;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.MyIronsEvent;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuyManager {

    private int currentPage = 0;
    private int pageCount = 15;

    private static BuyManager instance;

    public List<IronBuyBrief> ironBuys;

    public static BuyManager instance() {
        if (instance == null) {
            instance = new BuyManager();
        }
        return instance;
    }

    public void reloadMyIronBuys() {
        currentPage = 0;
        EventBusHelper.post(new MyIronsEvent(BaseStatusEvent.STARTED, MyIronsEvent.RELOAD));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(currentPage, pageCount)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                ironBuys = myIronsResponse.buys;
                EventBusHelper.post(new MyIronsEvent(BaseStatusEvent.SUCCESS, MyIronsEvent.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new MyIronsEvent(BaseStatusEvent.FAILED, MyIronsEvent.RELOAD));
                super.onFailed(baseResponse, e);
            }
        });
    }

    public void lodeMoreMyIronBuys() {
        EventBusHelper.post(new MyIronsEvent(BaseStatusEvent.STARTED, MyIronsEvent.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(BuyService.class).myIronBuy(currentPage + 1, pageCount)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                MyIronsResponse myIronsResponse = JSONHelper.parse(data.data.toString(), MyIronsResponse.class);
                BuyManager.this.currentPage = myIronsResponse.currentPage;
                BuyManager.this.pageCount = myIronsResponse.pageCount;
                ironBuys.addAll(myIronsResponse.buys);
                EventBusHelper.post(new MyIronsEvent(BaseStatusEvent.SUCCESS, MyIronsEvent.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new MyIronsEvent(BaseStatusEvent.FAILED, MyIronsEvent.LOAD_MORE));
            }
        });
    }

    public interface BuyService {
        @GET("iron/myBuy")
        Observable<BaseResponse> myIronBuy(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount);

        @GET("iron/shopRecommend")
        Observable<Data> shopRecommend();

        @GET("iron/irons")
        Observable<Data> irons(@Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount);

        @FormUrlEncoded
        @POST("product/postTest")
        Observable<Data> postDemo(@Field(("proId")) String proId, @Field("productType") int productType);

        @Multipart
        @POST("member/upload")
        Observable<Data> fileDemo(@Part MultipartBody.Part test, @Part MultipartBody.Part file);
    }

    public static class Data extends BaseResponse {

    }

}
