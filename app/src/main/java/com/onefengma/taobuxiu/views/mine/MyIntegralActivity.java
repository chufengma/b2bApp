package com.onefengma.taobuxiu.views.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.AuthManager;
import com.onefengma.taobuxiu.model.entities.UserProfile;
import com.onefengma.taobuxiu.views.core.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyIntegralActivity extends BaseActivity {

    @BindView(R.id.buyer_integral)
    TextView buyerIntegral;
    @BindView(R.id.seller_integral)
    TextView sellerIntegral;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, MyIntegralActivity.class);
        activity.startActivity(intent);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);

        UserProfile userProfile = AuthManager.instance().getUserProfile();
        if (userProfile != null) {
            if (userProfile.userData != null) {
                buyerIntegral.setText(userProfile.userData.integral + "");
            }
            if (userProfile.sellerData != null) {
                sellerIntegral.setText(userProfile.sellerData.integral + "");
            }
        } else {
            finish();
        }
    }
}
