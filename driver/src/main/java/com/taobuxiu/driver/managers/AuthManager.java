package com.taobuxiu.driver.managers;

import com.taobuxiu.driver.model.BaseResponse;
import com.taobuxiu.driver.model.events.BaseListStatusEvent;
import com.taobuxiu.driver.model.events.OnGetMsgCodeEvent;
import com.taobuxiu.driver.network.HttpHelper;
import com.taobuxiu.driver.utils.ToastUtils;
import com.taobuxiu.driver.utils.helpers.EventBusHelper;
import com.taobuxiu.driver.utils.helpers.VerifyHelper;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dev on 2017/2/27.
 */

public class AuthManager {

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

    public interface AuthService {
        @GET("msgCode")
        Observable<BaseResponse> msgCode(@Query(("mobile")) String mobile);

    }

}
