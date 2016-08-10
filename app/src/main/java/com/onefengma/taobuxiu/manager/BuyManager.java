package com.onefengma.taobuxiu.manager;


import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.model.HttpHelper;
import com.onefengma.taobuxiu.model.HttpHelper.BaseHttpResponse;
import com.onefengma.taobuxiu.model.HttpHelper.NetworkSubscriber;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    public static void demoGet() {
        HttpHelper.wrap(HttpHelper.create(DemoService.class).shopRecommend()).subscribe(new NetworkSubscriber<Data>() {

            @Override
            public void onFailed(Data data, Throwable e) {
                e.printStackTrace();
                Logger.e("-------------------------" + data);
            }

            @Override
            public void onSuccess(Data data) {
                Logger.i("-------------------------" + data.data);
            }
        });
    }

    public static void demoGetWithParams() {
        HttpHelper.wrap(HttpHelper.create(DemoService.class).irons(2, 3)).subscribe(new NetworkSubscriber<Data>() {

            @Override
            public void onFailed(Data data, Throwable e) {
                e.printStackTrace();
                Logger.e("-------------------------" + data);
            }

            @Override
            public void onSuccess(Data data) {
                Logger.i("-------------------------" + data.data);
            }
        });
    }

    public static void demoPost() {
        HttpHelper.wrap(HttpHelper.create(DemoService.class).postDemo("4YtrTWjVbVUa", 0)).subscribe(new NetworkSubscriber<Data>() {

            @Override
            public void onFailed(Data data, Throwable e) {
                e.printStackTrace();
                Logger.e("-------------------------" + data);
            }

            @Override
            public void onSuccess(Data data) {
                Logger.i("-------------------------" + data.data);
            }
        });
    }


    public static void demoFile() {
        // use the FileUtils to get the actual file by uri
        File file = new File(MainApplication.getContext().getFilesDir() + "/demo.txt");
        try {
            file.createNewFile();
            new FileWriter(file).append("adfasdfasdf").flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHelper.wrap(HttpHelper.create(DemoService.class).
                fileDemo(HttpHelper.preparePart("test", "Fengma is Greate"), HttpHelper.preparePart("file", file)))
                .subscribe(new NetworkSubscriber<Data>() {

            @Override
            public void onFailed(Data data, Throwable e) {
                e.printStackTrace();
                Logger.e("-------------------------" + data);
            }

            @Override
            public void onSuccess(Data data) {
                Logger.i("-------------------------" + data.data);
            }
        });
    }

    public interface DemoService {
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

    public static class Data extends BaseHttpResponse{

    }

}
