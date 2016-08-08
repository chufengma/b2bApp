package com.onefengma.taobuxiu.model;

import android.accounts.NetworkErrorException;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chufengma on 16/8/7.
 */
public class HttpHelper {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.32.24.114:5389/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <T> T create(final Class<T> service) {
        return getRetrofit().create(service);
    }

    public static <T> Observable<T> wrap(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static MultipartBody.Part preparePart(String name, Object object) {
        String value = object == null ? "" : object.toString();

        if (object instanceof File) {
            File file = (File) object;

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(name, System.currentTimeMillis() + "--" + file.getName(), requestFile);
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), value);
        return MultipartBody.Part.createFormData(name, System.currentTimeMillis() + "--" + name, requestBody);
    }

    public abstract static class NetworkSubscriber<T extends BaseHttpResponse> extends Subscriber<T> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            onFailed(null, e);
        }

        @Override
        public void onNext(T t) {
            if (t.status == 0) {
                onSuccess(t);
            } else {
                onFailed(t, new NetworkErrorException(t.errorMsg));
            }
        }

        public abstract void onFailed(T t, Throwable e);
        public abstract void onSuccess(T t);
    }

    public static class BaseHttpResponse {
        public String errorMsg;
        public int status;
        public JsonNode data;
    }

}
