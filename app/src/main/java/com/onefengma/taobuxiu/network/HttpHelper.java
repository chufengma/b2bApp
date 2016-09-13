package com.onefengma.taobuxiu.network;

import android.accounts.NetworkErrorException;

import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.network.persistentcookiejar.ClearableCookieJar;
import com.onefengma.taobuxiu.network.persistentcookiejar.PersistentCookieJar;
import com.onefengma.taobuxiu.network.persistentcookiejar.cache.SetCookieCache;
import com.onefengma.taobuxiu.network.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.sales.SalesAuthManager;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
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
//    118.178.17.15
    public static final String BASE_URL = "http://118.178.17.15:9090/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            ClearableCookieJar cookieJar =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MainApplication.getContext()));

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            CookieInterceptor cookieInterceptor = new CookieInterceptor();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(cookieInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    static final class CookieInterceptor implements Interceptor {
        private volatile String cookie;

        public void setSessionCookie(String cookie) {
            this.cookie = cookie;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            request = request.newBuilder()
                    .addHeader("User-Agent", "App")
                    .addHeader("X-Mobile-Flag", "Android")
                    .build();

            return chain.proceed(request);
        }
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

    public abstract static class NetworkSubscriber<T extends BaseResponse> extends Subscriber<T> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.errorMsg = "网络错误，请重试";
            baseResponse.status = 1;
            onFailed(baseResponse, e);
        }

        @Override
        public void onNext(T t) {
            if (t.status == 0) {
                onSuccess(t);
            } else if (t.status == 2) {
                if (MainApplication.IS_SALES_APP) {
                    SalesAuthManager.startLoginActivity();
                    SPHelper.top().save(Constant.StorageKeys.SALES_PROFILE, "");
                } else {
                    AuthManager.startLoginActivity();
                    SPHelper.top().save(Constant.StorageKeys.USER_PROFILE, "");
                }
            } else {
                onFailed(t, new NetworkErrorException(t.errorMsg));
            }
        }

        public abstract void onFailed(BaseResponse t, Throwable e);

        public abstract void onSuccess(T t);
    }

    public abstract static class SimpleNetworkSubscriber<T extends BaseResponse> extends NetworkSubscriber<T> {
        @Override
        public void onFailed(BaseResponse baseResponse, Throwable e) {
            Logger.e(e, e.getMessage());
            ToastUtils.showErrorTasty(baseResponse.errorMsg);
        }
    }

}
