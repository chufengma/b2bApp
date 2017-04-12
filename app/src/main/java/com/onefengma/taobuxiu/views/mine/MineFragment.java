package com.onefengma.taobuxiu.views.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.manager.QtManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.ImageHelper;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.model.events.OnMineTabEvent;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.auth.ResetPasswordActivity;
import com.onefengma.taobuxiu.views.auth.WebViewActivity;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.core.BaseFragment;
import com.onefengma.taobuxiu.views.offers.MySubscribeActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chufengma on 16/8/7.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.avator)
    CircleImageView avator;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.mine_subscribe)
    LinearLayout mineSubscribe;
    @BindView(R.id.mine_integral)
    LinearLayout mineIntegral;
    @BindView(R.id.mine_history)
    LinearLayout mineHistory;
    @BindView(R.id.mine_about)
    LinearLayout mineAbout;
    @BindView(R.id.mine_contact)
    LinearLayout mineContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserProfile userProfile = AuthManager.instance().getUserProfile();
        if (userProfile != null && userProfile.seller != null) {
            company.setText(userProfile.seller.companyName);
            company.setVisibility(View.VISIBLE);
        } else {
            company.setVisibility(View.GONE);
        }

        if (userProfile != null) {
            tel.setText(userProfile.mobile);
            tel.setVisibility(View.VISIBLE);
        } else {
            tel.setVisibility(View.GONE);
        }

        if (userProfile != null && !StringUtils.isEmpty(userProfile.avator)) {
            ImageLoader.getInstance().displayImage(ImageHelper.getImageUrl(userProfile.avator), avator);
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_detault_icon, avator);
        }

    }

    @OnClick(R.id.mine_integral)
    public void clickOnIntegral() {
        MyIntegralActivity.start((BaseActivity) getActivity());
    }

    @OnClick(R.id.mine_subscribe)
    public void clickOnSubscribe() {
        MySubscribeActivity.start((BaseActivity) getActivity());
    }

    @OnClick(R.id.avator)
    public void clickOnAvator() {
        ToastUtils.showInfoTasty("请前往电脑端淘不锈网站，修改商户资料");
    }

    @OnClick(R.id.setting)
    public void clickOnSetting() {
        DialogUtils.showItemDialog(getActivity(), null, new String[]{"修改密码", "退出登陆"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    ResetPasswordActivity.start((BaseActivity) getActivity(), "修改密码");
                } else {
                    AuthManager.quit();
                }
            }
        });
    }

    @OnClick(R.id.mine_contact)
    public void clickOnContact() {
        QtManager.instance().cantactSalesMan((BaseActivity) getActivity());
    }

    @OnClick(R.id.mine_about)
    public void clickOnAbout() {
        WebViewActivity.start((BaseActivity) getActivity(), "file:///android_asset/about_us.html", "关于我们");
    }

    @OnClick(R.id.mine_history)
    public void clickOnHistory() {
        MyHistoryActivity.start((BaseActivity) getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabEvent(OnMineTabEvent onMineTabEvent) {
        System.out.println("-----------------onMineTabEvent");
        ChoosePhotoActivity.start(getActivity());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
}


