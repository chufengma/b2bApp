package com.onefengma.taobuxiu.manager;

import android.content.Intent;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.network.HttpHelper.SimpleNetworkSubscriber;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.manager.helpers.VerifyHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.model.events.BaseStatusEvent;
import com.onefengma.taobuxiu.model.events.LoginEvent;
import com.onefengma.taobuxiu.model.events.OnGetMsgCodeEvent;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.auth.LoginMainActivity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class AuthManager {
    
    private static AuthManager instance;
    
    public static AuthManager instance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public static void startLoginActivity() {
        Intent intent = new Intent(MainApplication.getContext().getCurrentActivity(), LoginMainActivity.class);
        MainApplication.getContext().finishActivities();
        MainApplication.getContext().getCurrentActivity().startActivity(intent);
    }

    public void doLogin(String mobile, String password) {
        if(!VerifyHelper.checkMobileAndPassword(mobile, password)) {
            return;
        }

        EventBusHelper.post(new LoginEvent());
        HttpHelper.wrap(HttpHelper.create(AuthService.class).login(mobile, password)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                UserProfile userProfile = JSONHelper.parse(baseResponse.data.toString(), UserProfile.class);
                SPHelper.common().save(Constant.StorageKeys.USER_PROFILE, userProfile);
                EventBusHelper.post(new LoginEvent(BaseStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new LoginEvent(BaseStatusEvent.FAILED));
            }
        });
    }

    public void doRegister(String mobile, String password, String msgCode) {
        if (StringUtils.isEmpty(msgCode)) {
            ToastUtils.showInfoTasty("请输入短信验证码");
        }

        if(!VerifyHelper.checkMobileAndPassword(mobile, password)) {
            return;
        }

        HttpHelper.wrap(HttpHelper.create(AuthService.class).register(mobile, password, msgCode)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                ToastUtils.showSuccessTasty("注册成功");
            }
        });
    }

    public void doGetMsgCode(String mobile) {
        if(!VerifyHelper.checkMobile(mobile)) {
            return;
        }

        EventBusHelper.post(new OnGetMsgCodeEvent(BaseStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(AuthService.class).msgCode(mobile)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                ToastUtils.showSuccessTasty("获取短信验证码成功");
                EventBusHelper.post(new OnGetMsgCodeEvent(BaseStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                EventBusHelper.post(new OnGetMsgCodeEvent(BaseStatusEvent.FAILED));
            }
        });
    }


    public interface AuthService {
        @GET("msgCode")
        Observable<BaseResponse> msgCode(@Query(("mobile")) String mobile);

        @FormUrlEncoded
        @POST("member/login")
        Observable<BaseResponse> login(@Field(("mobile")) String mobile, @Field("password") String password);

        @FormUrlEncoded
        @POST("member/registerMobile")
        Observable<BaseResponse> register(@Field(("mobile")) String mobile, @Field("password") String password, @Field("msgCode") String msgCode);
    }

}
