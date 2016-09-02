package com.onefengma.taobuxiu.views.sales;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.SystemHelper;
import com.onefengma.taobuxiu.model.entities.NormalUserInfo;
import com.onefengma.taobuxiu.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/9/2
 */
public class SalesSellerUserFragment extends SalesBaseUserFragment implements TextWatcher {

    NormalSalesAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NormalUserInfo userInfo = new NormalUserInfo();
        userInfo.mobile = "18344441222";
        NormalUserInfo userInfo2 = new NormalUserInfo();
        userInfo2.name = "sss";
        userInfo2.mobile = "18344441221";
        adapter.setUserInfos(Arrays.asList(userInfo, userInfo2));
    }

    @Override
    public void doSearch(String words) {

    }

    @Override
    public ListAdapter getAdapter() {
        adapter = new NormalSalesAdapter();
        return adapter;
    }


    class NormalSalesAdapter extends BaseAdapter {

        List<NormalUserInfo> userInfos = new ArrayList<>();

        public void setUserInfos(List<NormalUserInfo> userInfos) {
            this.userInfos.clear();
            this.userInfos.addAll(userInfos);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return userInfos.size();
        }

        @Override
        public NormalUserInfo getItem(int position) {
            return userInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.normal_user_item, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            final NormalUserInfo userInfo = getItem(position);
            viewHolder.mobile.setText(userInfo.mobile);
            viewHolder.name.setText(userInfo.name);
            viewHolder.contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showAlertDialog(getContext(), "确定拨打用户电话：" + userInfo.mobile, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SystemHelper.call(userInfo.mobile);
                        }
                    });
                }
            });
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.mobile)
            TextView mobile;
            @BindView(R.id.name)
            TextView name;
            @BindView(R.id.contact)
            TextView contact;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

        }
    }

}
