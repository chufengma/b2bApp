package com.onefengma.taobuxiu.views.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.CityCategory;
import com.onefengma.taobuxiu.model.entities.City;
import com.onefengma.taobuxiu.model.events.logistics.RegionChooseEvent;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.ToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseReginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ToolBar toolbar;
    @BindView(R.id.recy_city)
    RecyclerView recyCity;

    public static void start(Activity activity, String cityId, String requestId) {
        Intent intent = new Intent(activity, ChooseReginActivity.class);
        intent.putExtra("cityId", cityId);
        intent.putExtra("requestId", requestId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_regin);
        ButterKnife.bind(this);

        RegionAdapter adapter = new RegionAdapter(CityCategory.instance().getSubCities(getIntent().getStringExtra("cityId")));
        recyCity.setAdapter(adapter);
        recyCity.setLayoutManager(new LinearLayoutManager(this));
    }

    private class RegionAdapter extends RecyclerView.Adapter<CityHolder> {

        private List<City> cities = new ArrayList<>();

        public RegionAdapter(List<City> cityList) {
            cities.addAll(cityList);
        }

        @Override
        public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View allCityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
            return new CityHolder(allCityView);
        }

        @Override
        public void onBindViewHolder(CityHolder holder, final int position) {
            holder.mCityName.setText(cities.get(position).name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBusHelper.post(new RegionChooseEvent(cities.get(position), getIntent().getStringExtra("requestId")));
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }
    }

}
