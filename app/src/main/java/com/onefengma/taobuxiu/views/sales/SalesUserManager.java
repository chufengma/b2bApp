package com.onefengma.taobuxiu.views.sales;

import com.alibaba.fastjson.JSON;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.MyIronsResponse;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesGetSellersEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesGetUsers;
import com.onefengma.taobuxiu.model.sales.SalesBindSellerResponse;
import com.onefengma.taobuxiu.model.sales.SalesBindUserResponse;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chufengma on 16/9/2.
 */
public class SalesUserManager {
    private static SalesUserManager instance;

    public SalesBindUserResponse salesBindUserResponse;
    public SalesBindSellerResponse salesBindSellerResponse;

    public SalesUserManager() {
        salesBindUserResponse = new SalesBindUserResponse();
        salesBindUserResponse.currentPage = 0;
        salesBindUserResponse.pageCount = 15;

        salesBindSellerResponse = new SalesBindSellerResponse();
        salesBindSellerResponse.currentPage = 0;
        salesBindSellerResponse.pageCount = 15;
    }

    public static SalesUserManager instance() {
        if (instance == null) {
            instance = new SalesUserManager();
        }
        return instance;
    }

    public void reloadBindUsers(String mobile) {
        readFromDB();
        salesBindUserResponse.currentPage = 0;
        EventBusHelper.post(new SalesGetUsers(BaseStatusEvent.STARTED, BaseListStatusEvent.RELOAD));
        HttpHelper.wrap(HttpHelper.create(SalesUserService.class).getBindUsers(mobile, salesBindUserResponse.currentPage, salesBindUserResponse.pageCount)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                salesBindUserResponse = JSON.parseObject(baseResponse.data.toString(), SalesBindUserResponse.class);
                SPHelper.common().save(Constant.StorageKeys.SALES_USERS, salesBindUserResponse);
                EventBusHelper.post(new SalesGetUsers(BaseListStatusEvent.SUCCESS, BaseListStatusEvent.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new SalesGetUsers(BaseListStatusEvent.FAILED, BaseListStatusEvent.RELOAD));
            }
        });
    }

    public void loadMoreBindUsers(String mobile) {
        EventBusHelper.post(new SalesGetUsers(BaseStatusEvent.STARTED, BaseListStatusEvent.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(SalesUserService.class).getBindUsers(mobile, salesBindUserResponse.currentPage, salesBindUserResponse.pageCount)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                SalesBindUserResponse response = JSON.parseObject(baseResponse.data.toString(), SalesBindUserResponse.class);

                salesBindUserResponse.maxCount = response.maxCount;
                salesBindUserResponse.currentPage = response.currentPage;
                salesBindUserResponse.pageCount = response.pageCount;
                if (salesBindUserResponse.userInfos == null) {
                    salesBindUserResponse.userInfos = response.userInfos;
                } else if (response.userInfos != null) {
                    salesBindUserResponse.userInfos.addAll(response.userInfos);
                }

                SPHelper.common().save(Constant.StorageKeys.SALES_USERS, salesBindUserResponse);
                EventBusHelper.post(new SalesGetUsers(BaseListStatusEvent.SUCCESS, BaseListStatusEvent.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new SalesGetUsers(BaseListStatusEvent.FAILED, BaseListStatusEvent.LOAD_MORE));
            }
        });
    }


    public void reloadBindSellers(String mobile) {
        readFromDB();
        salesBindSellerResponse.currentPage = 0;
        EventBusHelper.post(new SalesGetSellersEvent(BaseStatusEvent.STARTED, BaseListStatusEvent.RELOAD));
        HttpHelper.wrap(HttpHelper.create(SalesUserService.class).getBindSellers(mobile, salesBindUserResponse.currentPage, salesBindUserResponse.pageCount)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                salesBindSellerResponse = JSON.parseObject(baseResponse.data.toString(), SalesBindSellerResponse.class);
                SPHelper.common().save(Constant.StorageKeys.SALES_SELLERS, salesBindSellerResponse);
                EventBusHelper.post(new SalesGetSellersEvent(BaseListStatusEvent.SUCCESS, BaseListStatusEvent.RELOAD));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new SalesGetSellersEvent(BaseListStatusEvent.FAILED, BaseListStatusEvent.RELOAD));
            }
        });
    }

    public void loadMoreBindSellers(String mobile) {
        EventBusHelper.post(new SalesGetUsers(BaseStatusEvent.STARTED, BaseListStatusEvent.LOAD_MORE));
        HttpHelper.wrap(HttpHelper.create(SalesUserService.class).getBindUsers(mobile, salesBindUserResponse.currentPage, salesBindUserResponse.pageCount)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                SalesBindSellerResponse response = JSON.parseObject(baseResponse.data.toString(), SalesBindSellerResponse.class);

                salesBindSellerResponse.maxCount = response.maxCount;
                salesBindSellerResponse.currentPage = response.currentPage;
                salesBindSellerResponse.pageCount = response.pageCount;
                if (salesBindSellerResponse.sellerInfos == null) {
                    salesBindSellerResponse.sellerInfos = response.sellerInfos;
                } else if (response.sellerInfos != null) {
                    salesBindSellerResponse.sellerInfos.addAll(response.sellerInfos);
                }

                SPHelper.common().save(Constant.StorageKeys.SALES_USERS, salesBindUserResponse);
                EventBusHelper.post(new SalesGetUsers(BaseListStatusEvent.SUCCESS, BaseListStatusEvent.LOAD_MORE));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new SalesGetUsers(BaseListStatusEvent.FAILED, BaseListStatusEvent.LOAD_MORE));
            }
        });
    }

    public void readFromDB() {
        if (salesBindSellerResponse.sellerInfos == null) {
            SalesBindSellerResponse cache = SPHelper.buy().get(Constant.StorageKeys.SALES_SELLERS, SalesBindSellerResponse.class);
            if (cache != null) {
                salesBindSellerResponse.sellerInfos = cache.sellerInfos;
                salesBindSellerResponse.maxCount = cache.maxCount;
            }
        }

        if (salesBindUserResponse.userInfos == null) {
            SalesBindUserResponse cache = SPHelper.buy().get(Constant.StorageKeys.SALES_USERS, SalesBindUserResponse.class);
            if (cache != null) {
                salesBindUserResponse.userInfos = cache.userInfos;
                salesBindUserResponse.maxCount = cache.maxCount;
            }
        }
    }

    public interface SalesUserService {

        @GET("sales/bindUsers")
        Observable<BaseResponse> getBindUsers(@Query(("mobile")) String mobile, @Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount);


        @GET("sales/bindSellers")
        Observable<BaseResponse> getBindSellers(@Query(("mobile")) String mobile, @Query(("currentPage")) int currentPage, @Query("pageCount") int pageCount);

    }

}
