package com.onefengma.taobuxiu.views.sales;

import android.content.Intent;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.manager.helpers.VerifyHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.LoginEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesLoginEvent;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author yfchu
 * @date 2016/9/1
 */
public class SalesAuthManager {

    private static SalesAuthManager instance;

    public static SalesAuthManager instance() {
        if (instance == null) {
            instance = new SalesAuthManager();
        }
        return instance;
    }

    public static void startLoginActivity() {
        Intent intent = new Intent(MainApplication.getContext().getCurrentActivity(), SalesLoginActivity.class);
        MainApplication.getContext().finishActivities();
        MainApplication.getContext().getCurrentActivity().startActivity(intent);
    }

    public static void quit() {
        SPHelper.common().save(Constant.StorageKeys.SALES_PROFILE, "");
        startLoginActivity();
    }

    public void login(String mobile, String password) {
        if(!VerifyHelper.checkMobileAndPassword(mobile, password)) {
            return;
        }

        EventBusHelper.post(new LoginEvent(BaseStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(SalesAuthService.class).login(mobile, password)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                SalesManDetail userProfile = JSONHelper.parse(baseResponse.data.toString(), SalesManDetail.class);
                SPHelper.common().save(Constant.StorageKeys.SALES_PROFILE, userProfile);
                EventBusHelper.post(new SalesLoginEvent(BaseListStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new SalesLoginEvent(BaseListStatusEvent.FAILED));
            }
        });
    }

    public interface SalesAuthService {

        @FormUrlEncoded
        @POST("sales/login")
        Observable<BaseResponse> login(@Field(("mobile")) String mobile,
                                       @Field("password") String password);

    }


}
