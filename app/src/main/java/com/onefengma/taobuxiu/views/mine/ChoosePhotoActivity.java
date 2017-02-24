package com.onefengma.taobuxiu.views.mine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.BaseResponse;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.model.events.BaseListStatusEvent;
import com.onefengma.taobuxiu.model.events.sales.SalesGetSellersEvent;
import com.onefengma.taobuxiu.model.sales.SalesBindSellerResponse;
import com.onefengma.taobuxiu.network.HttpHelper;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.views.sales.SalesUserManager;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public class ChoosePhotoActivity extends TakePhotoActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ChoosePhotoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File file=new File(Environment.getExternalStorageDirectory(), "/fengma/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);

        TakePhoto takePhoto = getTakePhoto();
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(50*1024).enableReserveRaw(true).setMaxPixel(800).create(), true);
        takePhoto.onPickFromGalleryWithCrop(imageUri, new CropOptions.Builder().create());
    }

    public void onClick(View view) {
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        for(TImage image : images) {
            System.out.println("-------" + image.getCompressPath());
            HttpHelper.wrap(HttpHelper.create(ImageService.class).getBindUsers(HttpHelper.imageBody(image.getCompressPath()))).subscribe(new HttpHelper.SimpleNetworkSubscriber<BaseResponse>() {
                @Override
                public void onSuccess(BaseResponse baseResponse) {
                    System.out.println("--------success");
                }

                @Override
                public void onFailed(BaseResponse baseResponse, Throwable e) {
                    System.out.println("--------failed");
                }
            });
        }

    }

    public interface ImageService {

        @Multipart
        @POST("imageUpload")
        Observable<BaseResponse> getBindUsers(@Part("image\"; filename=\"pp.png\" ") RequestBody file);

    }
}
