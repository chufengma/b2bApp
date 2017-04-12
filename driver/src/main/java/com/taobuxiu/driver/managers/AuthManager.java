package com.taobuxiu.driver.managers;

import android.app.Activity;
import android.content.Intent;

import com.taobuxiu.driver.model.BaseResponse;
import com.taobuxiu.driver.model.Constant;
import com.taobuxiu.driver.model.Driver;
import com.taobuxiu.driver.model.events.BaseListStatusEvent;
import com.taobuxiu.driver.model.events.OnGetMsgCodeEvent;
import com.taobuxiu.driver.network.HttpHelper;
import com.taobuxiu.driver.utils.SPHelper;
import com.taobuxiu.driver.utils.StringUtils;
import com.taobuxiu.driver.utils.ToastUtils;
import com.taobuxiu.driver.utils.helpers.EventBusHelper;
import com.taobuxiu.driver.utils.helpers.VerifyHelper;
import com.taobuxiu.driver.views.auth.FillCerActivity;
import com.taobuxiu.driver.views.auth.FillCompanyNameActivity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dev on 2017/2/27.
 */

public class AuthManager {

    private static AuthManager instance;
    
    public static AuthManager ins() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }
    
    
    public void doGetMsgCode(String mobile) {
        if(!VerifyHelper.checkMobile(mobile)) {
            return;
        }
        EventBusHelper.post(new OnGetMsgCodeEvent(BaseListStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(AuthService.class).msgCode(mobile)).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                ToastUtils.showSuccessTasty("获取短信验证码成功");
                EventBusHelper.post(new OnGetMsgCodeEvent(BaseListStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new OnGetMsgCodeEvent(BaseListStatusEvent.FAILED));
            }
        });
    }

    public boolean checkToGoto(Activity activity) {
        Driver userProfile =  SPHelper.top().get(Constant.StorageKeys.DRIVER_PROFILE, Driver.class);
        if (userProfile == null) {
            return false;
        }
        if (StringUtils.isEmpty(userProfile.companyName)) {
            FillCompanyNameActivity.start(activity);
            return false;
        } else if (StringUtils.isEmpty(userProfile.license)) {
            FillCerActivity.start(activity);
            return false;
        }
        return true;
    }

    public interface AuthService {
        @GET("msgCode")
        Observable<BaseResponse> msgCode(@Query(("mobile")) String mobile);

        @FormUrlEncoded
        @POST("logistics/driverRegister")
        Observable<BaseResponse> register(@Field(("mobile")) String mobile, @Field(("password")) String password, @Field(("code")) String code);

        @FormUrlEncoded
        @POST("logistics/driverLogin")
        Observable<BaseResponse> login(@Field(("mobile")) String mobile, @Field(("password")) String password);

        @FormUrlEncoded
        @POST("logistics/driverChangePassword")
        Observable<BaseResponse> changePassword(@Field(("mobile")) String mobile, @Field(("password")) String password, @Field(("code")) String code);

        @FormUrlEncoded
        @POST("logistics/driverFillCompany")
        Observable<BaseResponse> fillCompany(@Field(("id")) String id, @Field(("companyName")) String companyName);
    }

}
