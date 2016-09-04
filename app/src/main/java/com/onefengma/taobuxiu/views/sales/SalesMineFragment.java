package com.onefengma.taobuxiu.views.sales;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.Constant;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.SPHelper;
import com.onefengma.taobuxiu.views.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author yfchu
 * @date 2016/9/1
 */
public class SalesMineFragment extends BaseFragment {

    @BindView(R.id.avator)
    CircleImageView avator;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.quit)
    TextView quit;
    @BindView(R.id.mine_contact)
    LinearLayout mineContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slaes_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avator.setImageResource(R.drawable.ic_detault_icon);

        SalesManDetail salesManDetail = SPHelper.common().get(Constant.StorageKeys.SALES_PROFILE, SalesManDetail.class);
        tel.setText(salesManDetail.tel);
        name.setText(salesManDetail.name);
    }

    @OnClick(R.id.quit)
    public void clickOnQuit() {
        DialogUtils.showAlertDialog(getContext(), "确定退出吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SalesAuthManager.quit();
            }
        });
    }
}
