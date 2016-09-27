package com.onefengma.taobuxiu.views.sales;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.manager.helpers.SystemHelper;
import com.onefengma.taobuxiu.model.events.sales.SalesGetSellersEvent;
import com.onefengma.taobuxiu.model.sales.SalesSellerInfo;
import com.onefengma.taobuxiu.utils.DialogUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.views.widgets.listview.XListView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yfchu
 * @date 2016/9/2
 */
public class SalesSellerUserFragment extends SalesBaseUserFragment implements TextWatcher {

    SellerSalesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        list.setOnLoadMoreListener(new XListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                SalesUserManager.instance().loadMoreBindSellers(searchBar.getText().toString());
            }
        });

        SalesUserManager.instance().reloadBindSellers(searchBar.getText().toString());
        searchBar.setInputType(InputType.TYPE_CLASS_TEXT);
        searchBar.setHint("公司名/手机号搜索");
    }

    @Subscribe
    public void onLoadSellersEvent(SalesGetSellersEvent event) {
        list.onLoadMoreComplete();

        List<SalesSellerInfo> data = SalesUserManager.instance().salesBindSellerResponse.sellerInfos;
        list.enableLoadMore(data != null
                && data.size() > 0
                && data.size() % 15 == 0);

        adapter.setSellerInfos(data);
    }

    @Override
    public void doSearch(String words) {
        SalesUserManager.instance().reloadBindSellers(words);
    }

    @Override
    public ListAdapter getAdapter() {
        adapter = new SellerSalesAdapter();
        return adapter;
    }


    class SellerSalesAdapter extends BaseAdapter {

        List<SalesSellerInfo> userInfos = new ArrayList<>();

        public void setSellerInfos(List<SalesSellerInfo> userInfos) {
            if (userInfos == null) {
                return;
            }
            this.userInfos.clear();
            this.userInfos.addAll(userInfos);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return userInfos.size();
        }

        @Override
        public SalesSellerInfo getItem(int position) {
            return userInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_user_item, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            final SalesSellerInfo userInfo = getItem(position);
            viewHolder.mobile.setText(userInfo.mobile);
            viewHolder.name.setText(userInfo.contact);
            viewHolder.company.setText(userInfo.companyName);
            viewHolder.locationCity.setText(StringUtils.getString(R.string.sales_user_locate, userInfo.locateCity));
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
            @BindView(R.id.company)
            TextView company;
            @BindView(R.id.mobile)
            TextView mobile;
            @BindView(R.id.name)
            TextView name;
            @BindView(R.id.contact)
            TextView contact;
            @BindView(R.id.locationCity)
            TextView locationCity;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
