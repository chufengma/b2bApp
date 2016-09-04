package com.onefengma.taobuxiu.manager;

import android.content.Intent;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.events.OnResetPasswordEvent;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.network.HttpHelper.SimpleNetworkSubscriber;
import com.onefengma.taobuxiu.manager.helpers.JSONHelper;
import com.onefengma.taobuxiu.manager.helpers.VerifyHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.LoginEvent;
import com.onefengma.taobuxiu.model.events.OnGetMsgCodeEvent;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.auth.LoginMainActivity;
import com.onefengma.taobuxiu.views.buys.PushNewBuyActivity;
import com.onefengma.taobuxiu.views.core.BaseActivity;

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
    private static UserProfile userProfile;

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

    public static void quit() {
        userProfile = null;
        SPHelper.top().save(Constant.StorageKeys.USER_PROFILE, "");
        startLoginActivity();
    }

    public UserProfile getUserProfile() {
        if (userProfile == null) {
            userProfile = SPHelper.top().get(Constant.StorageKeys.USER_PROFILE, UserProfile.class);
        }
        return userProfile;
    }

    public String getUserSalesmanMobile() {
        if (getUserProfile() == null || getUserProfile().salesMan == null) {
            return null;
        } else {
            return getUserProfile().salesMan.tel;
        }
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
                SPHelper.top().save(Constant.StorageKeys.USER_PROFILE, userProfile);
                SPHelper.top().sp().edit().putString(Constant.StorageKeys.USER_TEL, userProfile.mobile).commit();
                EventBusHelper.post(new LoginEvent(BaseListStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new LoginEvent(BaseListStatusEvent.FAILED));
            }
        });
    }

    public boolean sellerCheck() {
        return sellerCheck(true);
    }

    public boolean sellerCheck(boolean showToast) {
        UserProfile userProfile = AuthManager.instance().getUserProfile();
        if (userProfile.seller == null) {
            if (showToast) {
                ToastUtils.showErrorTasty("您还不是商家，请前往电脑端申请商家身份");
            }
            return false;
        } else {
            return true;
        }
    }

    public void doRegister(final String mobile, final String password, String msgCode) {
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
                doLogin(mobile, password);
            }
        });
    }

    public void doGetMsgCode(String mobile) {
        if(!VerifyHelper.checkMobile(mobile)) {
            return;
        }

        EventBusHelper.post(new OnGetMsgCodeEvent(BaseListStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(AuthService.class).msgCode(mobile)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
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

    public void doResetPassword(String mobile, String msgCode, String password) {
        if(!VerifyHelper.checkMobileAndPassword(mobile, password)) {
            return;
        }

        EventBusHelper.post(new OnResetPasswordEvent(BaseListStatusEvent.STARTED));
        HttpHelper.wrap(HttpHelper.create(AuthService.class).resetPassword(mobile, msgCode, password, password)).subscribe(new SimpleNetworkSubscriber<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                EventBusHelper.post(new OnResetPasswordEvent(BaseListStatusEvent.SUCCESS));
            }

            @Override
            public void onFailed(BaseResponse baseResponse, Throwable e) {
                super.onFailed(baseResponse, e);
                EventBusHelper.post(new OnResetPasswordEvent(BaseListStatusEvent.FAILED));
            }
        });
    }

    public interface AuthService {
        @GET("msgCode")
        Observable<BaseResponse> msgCode(@Query(("mobile")) String mobile);

        @FormUrlEncoded
        @POST("member/login")
        Observable<BaseResponse> login(@Field(("mobile")) String mobile,
                                       @Field("password") String password);

        @FormUrlEncoded
        @POST("member/registerMobile")
        Observable<BaseResponse> register(@Field(("mobile")) String mobile,
                                          @Field("password") String password,
                                          @Field("msgCode") String msgCode);

        @FormUrlEncoded
        @POST("member/forgetPassword")
        Observable<BaseResponse> resetPassword(@Field("mobile") String mobile,
                                               @Field("msgCode") String msgCode,
                                               @Field(("newPassword")) String newPassword,
                                               @Field("newPasswordConfirm") String newPasswordConfirm);
    }

}
