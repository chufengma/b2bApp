package com.onefengma.taobuxiu.manager;

import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.onefengma.taobuxiu.model.HttpHelper;
import com.onefengma.taobuxiu.utils.ToastUtils;

import org.json.JSONArray;

import retrofit2.http.GET;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chufengma on 16/8/7.
 */
public class BuyManager {

    public static void demo() {
        HttpHelper.getRetrofit()
                .create(DemoService.class)
                .shopRecommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Data>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show("Error");
            }

            @Override
            public void onNext(Data s) {
                ToastUtils.show(s.data.toString());
            }
        });
    }

    public interface DemoService {

        @GET("iron/shopRecommend")
        Observable<Data> shopRecommend();

    }

    public static class Data {
        public ArrayNode data;
        public int status;
        public String errorMsg;
    }

}
