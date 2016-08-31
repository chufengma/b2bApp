package com.onefengma.taobuxiu.views.offers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.OfferManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.IconDataCategory;
import com.onefengma.taobuxiu.model.entities.SubscribeInfo;
import com.onefengma.taobuxiu.model.events.GetSubscribeInfoEvent;
import com.onefengma.taobuxiu.model.events.UpdateSubscribeInfoEvent;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySubscribeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    ProgressDialog progressDialog;

    CheckAdapter typesAdapter;
    CheckAdapter surfacesAdapter;
    CheckAdapter materialAdapter;
    CheckAdapter proPlaceadapter;

    public static void start(BaseActivity activity) {
        Intent intent = new Intent(activity, MySubscribeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribe);
        ButterKnife.bind(this);
        viewPager.setAdapter(new MyPageAdapter());
        viewPager.setOffscreenPageLimit(2);
        tab.setupWithViewPager(viewPager);
        progressDialog = new ProgressDialog(this);

        EventBusHelper.register(this);

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                OfferManager.instance().getMySubscribeInfo();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    @OnClick(R.id.right_title)
    public void clickOnSave() {
        if (materialAdapter == null || proPlaceadapter == null || typesAdapter == null || surfacesAdapter == null) {
            return;
        }
        SubscribeInfo subscribeInfo = new SubscribeInfo();
        subscribeInfo.materials = materialAdapter.checkedList;
        subscribeInfo.proPlaces = proPlaceadapter.checkedList;
        subscribeInfo.types = typesAdapter.checkedList;
        subscribeInfo.surfaces = surfacesAdapter.checkedList;
        OfferManager.instance().updateMySubscribeInfo(subscribeInfo);
    }

    @Subscribe
    public void onGetSubscirbeEvent(GetSubscribeInfoEvent event) {
        if (event.isStarted()) {
            progressDialog.show("加载中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            SubscribeInfo subscribeInfo = event.subscribeInfo;
            typesAdapter.setCheckedList(subscribeInfo.types);
            surfacesAdapter.setCheckedList(subscribeInfo.surfaces);
            materialAdapter.setCheckedList(subscribeInfo.materials);
            proPlaceadapter.setCheckedList(subscribeInfo.proPlaces);
        } else {
            ToastUtils.showErrorTasty("加载关注列表失败！");
            finish();
        }
    }

    @Subscribe
    public void onPushSubscirbeEvent(UpdateSubscribeInfoEvent event) {
        if (event.isStarted()) {
            progressDialog.show("保存中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            ToastUtils.showSuccessTasty("保存关注列表成功");
        } else {
            ToastUtils.showErrorTasty("保存关注列表失败，请重试！");
        }
    }

    class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.subscribe_layout, container, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            GridLayoutManager layoutManager = new GridLayoutManager(container.getContext(), 3);
            recyclerView.setLayoutManager(layoutManager);
            CheckAdapter adapter = null;

            List<String> data ;
            switch (position) {
                case 0 :
                    data = IconDataCategory.get().types;
                    typesAdapter = new CheckAdapter();
                    adapter = typesAdapter;
                    break;
                case 1 :
                    data = IconDataCategory.get().surfaces;
                    surfacesAdapter = new CheckAdapter();
                    adapter = surfacesAdapter;
                    break;
                case 2 :
                    data = IconDataCategory.get().materials;
                    materialAdapter = new CheckAdapter();
                    adapter = materialAdapter;
                    break;
                default:
                    data = IconDataCategory.get().productPlaces;
                    proPlaceadapter = new CheckAdapter();
                    adapter = proPlaceadapter;
            }

            adapter.setList(data);
            recyclerView.setAdapter(adapter);
            container.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0 :
                    return "品类";
                case 1 :
                    return "表面";
                case 2 :
                    return "材料";
                default:
                    return "产地";
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class CheckAdapter extends RecyclerView.Adapter<ViewHolder> implements CompoundButton.OnCheckedChangeListener {

        public List<String> list = new ArrayList<>();
        public List<String> checkedList = new ArrayList<>();

        public void setList(List<String> list) {
            this.list.clear();
            this.list.addAll(list);
            this.notifyDataSetChanged();
        }

        public void setCheckedList(List<String> checkedList) {
            if (checkedList == null) {
                return;
            }
            this.checkedList.clear();
            this.checkedList.addAll(checkedList);
            this.notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_layout_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String str = list.get(position);
            holder.itemCheckout.setText(str);
            for(String checked : checkedList) {
                if (StringUtils.equals(checked, str)) {
                    holder.itemCheckout.setChecked(true);
                    return;
                }
            }
            holder.itemCheckout.setChecked(false);
            holder.itemCheckout.setOnCheckedChangeListener(this);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                checkedList.add(buttonView.getText().toString());
            } else {
                checkedList.remove(buttonView.getText().toString());
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_checkout)
        CheckBox itemCheckout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
