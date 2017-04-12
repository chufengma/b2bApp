package com.taobuxiu.driver.views.widgets;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.taobuxiu.driver.model.BaseResponse;
import com.taobuxiu.driver.model.events.BaseStatusEvent;
import com.taobuxiu.driver.model.events.ChoosePhotoEvent;
import com.taobuxiu.driver.network.HttpHelper;
import com.taobuxiu.driver.utils.helpers.EventBusHelper;

import java.io.File;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public class ChoosePhotoActivity extends TakePhotoActivity {

    public static void start(Activity activity, String id) {
        Intent intent = new Intent(activity, ChoosePhotoActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File file=new File(Environment.getExternalStorageDirectory(), "/taobuxiu_driver/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();

        Uri imageUri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(50*1024).enableReserveRaw(true).setMaxPixel(800).create(), true);
        takePhoto.onPickFromGalleryWithCrop(imageUri, new CropOptions.Builder().create());
        EventBusHelper.post(new ChoosePhotoEvent(getIntent().getStringExtra("id"), "", BaseStatusEvent.STARTED));
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        EventBusHelper.post(new ChoosePhotoEvent(getIntent().getStringExtra("id"), "", BaseStatusEvent.FAILED));
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        EventBusHelper.post(new ChoosePhotoEvent(getIntent().getStringExtra("id"), "", BaseStatusEvent.FAILED));
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        EventBusHelper.post(new ChoosePhotoEvent(getIntent().getStringExtra("id"), result.getImage().getCompressPath(), BaseStatusEvent.FAILED));
    }

}
