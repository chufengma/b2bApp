package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.CityCategory;
import com.onefengma.taobuxiu.model.entities.City;
import com.onefengma.taobuxiu.model.events.logistics.RegionChooseEvent;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.SideBar;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseCityActivity extends BaseActivity {

    @BindView(R.id.recy_city)
    RecyclerView recyCity;
    @BindView(R.id.contact_dialog)
    TextView contactDialog;
    @BindView(R.id.contact_sidebar)
    SideBar contactSidebar;
    @BindView(R.id.toolbar)
    ToolBar toolbar;

    private CityRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private City currentCity;

    public static void start(Activity activity, String requestId) {
        Intent intent = new Intent(activity, ChooseCityActivity.class);
        intent.putExtra("requestId", requestId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        ButterKnife.bind(this);
        EventBusHelper.register(this);

        adapter = new CityRecyclerAdapter(this, CityCategory.instance().getMainCities());
        linearLayoutManager = new LinearLayoutManager(this);
        recyCity.setLayoutManager(linearLayoutManager);
        recyCity.setAdapter(adapter);

        adapter.setOnCityClickListener(new CityRecyclerAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(City city) {
                currentCity = city;
                ChooseReginActivity.start(ChooseCityActivity.this, city.id, getIntent().getStringExtra("requestId"));
            }

            @Override
            public void onLocateClick() {

            }
        });

        contactSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                int position = adapter.getPositionForSection(s);
                if (position != -1) {
//                    mRecyCity.scrollToPosition(position);
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                }

            }
        });
    }

    @Subscribe
    public void onEvent(RegionChooseEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }
}
