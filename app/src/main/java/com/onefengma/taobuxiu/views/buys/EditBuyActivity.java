package com.onefengma.taobuxiu.views.buys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.manager.BuyManager;
import com.onefengma.taobuxiu.manager.helpers.EventBusHelper;
import com.onefengma.taobuxiu.model.CityCategory;
import com.onefengma.taobuxiu.model.IconDataCategory;
import com.onefengma.taobuxiu.model.entities.City;
import com.onefengma.taobuxiu.model.entities.IronBuyPush;
import com.onefengma.taobuxiu.model.events.IronBuyPushEvent;
import com.onefengma.taobuxiu.utils.NumbersUtils;
import com.onefengma.taobuxiu.utils.StringUtils;
import com.onefengma.taobuxiu.utils.ToastUtils;
import com.onefengma.taobuxiu.views.core.BaseActivity;
import com.onefengma.taobuxiu.views.widgets.BubbleView;
import com.onefengma.taobuxiu.views.widgets.ProgressDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBuyActivity extends BaseActivity {

    private static final String EXTRA_IRON_BUY = "iron_buy";

    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.surface)
    TextView surface;
    @BindView(R.id.material)
    TextView material;
    @BindView(R.id.proPlace)
    TextView proPlace;
    @BindView(R.id.locateCity)
    TextView locateCity;
    @BindView(R.id.height)
    EditText height;
    @BindView(R.id.length)
    EditText length;
    @BindView(R.id.width)
    EditText width;
    @BindView(R.id.numbers)
    EditText numbers;
    @BindView(R.id.unit)
    AppCompatSpinner unit;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.day)
    AppCompatSpinner day;
    @BindView(R.id.hour)
    AppCompatSpinner hour;
    @BindView(R.id.minute)
    AppCompatSpinner minute;
    @BindView(R.id.toleranceFrom)
    EditText toleranceFrom;
    @BindView(R.id.toleranceTo)
    EditText toleranceTo;
    @BindView(R.id.push)
    TextView push;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.push_layout)
    LinearLayout pushLayout;
    @BindView(R.id.edit)
    TextView edit;

    IronBuyPush ironBuyPush;

    ProgressDialog progressDialog;

    @BindView(R.id.re_push)
    TextView rePush;

    public static void start(Context context, IronBuyPush push) {
        Intent starter = new Intent(context, EditBuyActivity.class);
        starter.putExtra(EXTRA_IRON_BUY, push);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buy);
        ButterKnife.bind(this);
        ironBuyPush = (IronBuyPush) getIntent().getSerializableExtra(EXTRA_IRON_BUY);
        if (ironBuyPush == null) {
            ironBuyPush = new IronBuyPush();
            ironBuyPush.dayIndex = 1;
        }
        progressDialog = new ProgressDialog(this);
        EventBusHelper.register(this);
        setupViews();

        save.setVisibility(ironBuyPush.pushStatus == 0 ? View.VISIBLE : View.GONE);
        push.setVisibility(ironBuyPush.pushStatus == 0 ? View.VISIBLE : View.GONE);
        rePush.setVisibility(ironBuyPush.pushStatus == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);
    }

    public void setupViews() {
        type.setText(ironBuyPush.ironType);
        surface.setText(ironBuyPush.surface);
        material.setText(ironBuyPush.material);
        proPlace.setText(ironBuyPush.proPlace);
        message.setText(ironBuyPush.message);

        if (!StringUtils.isEmpty(ironBuyPush.width)) {
            width.setText(ironBuyPush.width + "");
        }
        if (!StringUtils.isEmpty(ironBuyPush.length)) {
            length.setText(ironBuyPush.length + "");
        }
        if (!StringUtils.isEmpty(ironBuyPush.height)) {
            height.setText(ironBuyPush.height + "");
        }
        if (ironBuyPush.numbers != 0) {
            numbers.setText(ironBuyPush.numbers + "");
        }
        if (!StringUtils.isEmpty(ironBuyPush.toleranceFrom)) {
            toleranceFrom.setText(ironBuyPush.toleranceFrom + "");
        }
        if (!StringUtils.isEmpty(ironBuyPush.toleranceTo)) {
            toleranceTo.setText(ironBuyPush.toleranceTo + "");
        }

        unit.setSelection(ironBuyPush.unitIndex);
        day.setSelection(ironBuyPush.dayIndex);
        hour.setSelection(ironBuyPush.hourIndex);
        minute.setSelection(ironBuyPush.minuteIndex);

        City selectCity = CityCategory.instance().getCity(ironBuyPush.locationCityId);
        if (selectCity != null) {
            City fatherCity = CityCategory.instance().getCity(selectCity.fatherId);
            locateCity.setText(fatherCity.name + " " + selectCity.name);
        }


        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    final Map<String, List<String>> specMapBan;
                    if (StringUtils.equals("不锈钢板", type.getText().toString()) &&
                            StringUtils.equals("2B", surface.getText().toString())) {
                        specMapBan = IconDataCategory.get().specMapBan2B;
                    } else if (StringUtils.equals("不锈钢板", type.getText().toString()) &&
                            StringUtils.equals("No.1", surface.getText().toString())) {
                        specMapBan = IconDataCategory.get().specMapBanNo;
                    } else if (StringUtils.equals("不锈钢卷", type.getText().toString()) &&
                            StringUtils.equals("2B", surface.getText().toString())) {
                        specMapBan = IconDataCategory.get().specMapJuan2B;
                    } else if (StringUtils.equals("不锈钢卷", type.getText().toString()) &&
                            StringUtils.equals("No.1", surface.getText().toString())) {
                        specMapBan = IconDataCategory.get().specMapJuanNo;
                    } else {
                        specMapBan = new HashMap<>();
                    }
                    if (specMapBan.isEmpty()) {
                        return;
                    }
                    new BubbleView().init(EditBuyActivity.this, view, "推荐使用以下规格：", Arrays.asList(specMapBan.keySet().toArray(new String[specMapBan.size()])), new BubbleView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(String item) {
                            if (specMapBan.containsKey(item)) {
                                List<String> values = specMapBan.get(item);
                                width.setText(values.get(0));
                                length.setText(values.get(1));
                            }
                        }
                    });
                }
            }
        };

        length.setOnFocusChangeListener(onFocusChangeListener);
        width.setOnFocusChangeListener(onFocusChangeListener);

        message.requestFocus();
        message.requestFocusFromTouch();
    }

    @OnClick(R.id.push)
    public void clickOnPush() {
        doPush();
    }

    @OnClick(R.id.re_push)
    public void clickOnRePush() {
        doRePush();
    }

    @OnClick(R.id.save)
    public void clickOnSave() {
        doSave();
    }


    @Subscribe
    public void onPushEvent(IronBuyPushEvent event) {
        if (event.isStarted()) {
            progressDialog.show("发布中...");
            return;
        } else {
            progressDialog.dismiss();
        }
        if (event.isSuccess()) {
            if (event.isRePush) {
                ToastUtils.showSuccessTasty("更新求购成功！");
            } else {
                ToastUtils.showSuccessTasty("发布求购成功！");
            }
            finish();
        } else {
            if (event.isRePush) {
                ToastUtils.showErrorTasty("更新求购失败，请重试！");
            } else {
                ToastUtils.showErrorTasty("发布求购失败，请重试！");
            }
        }
    }

    private boolean doCheck() {
        ironBuyPush.ironType = type.getText().toString();
        ironBuyPush.surface = surface.getText().toString();
        ironBuyPush.material = material.getText().toString();
        ironBuyPush.proPlace = proPlace.getText().toString();

        ironBuyPush.message = message.getText().toString();

        ironBuyPush.width = width.getText().toString();
        ironBuyPush.height = height.getText().toString();
        ironBuyPush.length = length.getText().toString();
        ironBuyPush.numbers = NumbersUtils.parseFloat(numbers.getText().toString());

        ironBuyPush.toleranceFrom = toleranceFrom.getText().toString();
        ironBuyPush.toleranceTo = toleranceTo.getText().toString();

        ironBuyPush.unit = getResources().getStringArray(R.array.units)[unit.getSelectedItemPosition()];

        ironBuyPush.timeLimit = (long) day.getSelectedItemPosition() * (1000 * 60 * 60 * 24)
                + (long) hour.getSelectedItemPosition() * (1000 * 60 * 60)
                + (long) minute.getSelectedItemPosition() * (1000 * 60);

        ironBuyPush.unitIndex = unit.getSelectedItemPosition();
        ironBuyPush.dayIndex = day.getSelectedItemPosition();
        ironBuyPush.hourIndex = hour.getSelectedItemPosition();
        ironBuyPush.minuteIndex = minute.getSelectedItemPosition();

        if (StringUtils.isEmpty(ironBuyPush.ironType) ||
                StringUtils.isEmpty(ironBuyPush.material) ||
                StringUtils.isEmpty(ironBuyPush.proPlace) ||
                StringUtils.isEmpty(ironBuyPush.locationCityId) ||
                StringUtils.isEmpty(ironBuyPush.unit)) {
            ToastUtils.showInfoTasty("品类，材料，货源地，售卖城市，单位等基本信息不能为空！");
            return false;
        }

        if (!StringUtils.isEmpty(ironBuyPush.message) && ironBuyPush.message.length() >= 35) {
            ToastUtils.showInfoTasty("备注字数过多，请重新编辑");
        }

        if (ironBuyPush.timeLimit == 0) {
            ToastUtils.showInfoTasty("时间期限不能为0");
            return false;
        }

        if (StringUtils.isEmpty(width.getText().toString())) {
            ToastUtils.showInfoTasty("宽度不能为空");
            return false;
        }
        if (StringUtils.isEmpty(height.getText().toString())) {
            ToastUtils.showInfoTasty("高度不能为空");
            return false;
        }
        if (StringUtils.isEmpty(length.getText().toString())) {
            ToastUtils.showInfoTasty("长度不能为空");
            return false;
        }
        if (StringUtils.isEmpty(numbers.getText().toString())) {
            ToastUtils.showInfoTasty("数量不能为空");
            return false;
        }
        if (StringUtils.isEmpty(toleranceFrom.getText().toString()) || StringUtils.isEmpty(width.getText().toString())) {
            ToastUtils.showInfoTasty("公差不能为空");
            return false;
        }
        return true;
    }

    private void doPush() {
        if (!doCheck()) {
            return;
        }
        BuyManager.instance().doPushIronBuy(ironBuyPush);
    }

    private void doRePush() {
        if (!doCheck()) {
            return;
        }
        BuyManager.instance().doRePushIronBuy(ironBuyPush);
    }

    private void doSave() {
        if (!doCheck()) {
            return;
        }
        BuyManager.instance().saveIronBuy(ironBuyPush);
        ToastUtils.showSuccessTasty("保存成功!");
        finish();
    }

    @OnClick(R.id.type)
    public void clickOnType(View view) {
        if (view.getTag() != null) {
            ((PopupMenu) view.getTag()).show();
        } else {
            IconDataCategory iconDataCategory = IconDataCategory.get();
            PopupMenu popupMenu = new PopupMenu(this, view);
            for (String typeItem : iconDataCategory.types) {
                popupMenu.getMenu().add(typeItem);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    type.setText(menuItem.getTitle());
                    ironBuyPush.ironType = menuItem.getTitle().toString();
                    return false;
                }
            });
            view.setTag(popupMenu);
            popupMenu.show();
        }
    }

    @OnClick(R.id.surface)
    public void clickOnSurface(View view) {
        if (view.getTag() != null) {
            ((PopupMenu) view.getTag()).show();
        } else {
            IconDataCategory iconDataCategory = IconDataCategory.get();
            PopupMenu popupMenu = new PopupMenu(this, view);
            for (String typeItem : iconDataCategory.surfaces) {
                popupMenu.getMenu().add(typeItem);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    surface.setText(menuItem.getTitle());
                    ironBuyPush.surface = menuItem.getTitle().toString();
                    return false;
                }
            });
            view.setTag(popupMenu);
            popupMenu.show();
        }
    }


    @OnClick(R.id.material)
    public void clickOnMaterial(View view) {
        if (view.getTag() != null) {
            ((PopupMenu) view.getTag()).show();
        } else {
            IconDataCategory iconDataCategory = IconDataCategory.get();
            PopupMenu popupMenu = new PopupMenu(this, view);
            for (String typeItem : iconDataCategory.materials) {
                popupMenu.getMenu().add(typeItem);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    material.setText(menuItem.getTitle());
                    ironBuyPush.material = menuItem.getTitle().toString();
                    return false;
                }
            });
            view.setTag(popupMenu);
            popupMenu.show();
        }
    }

    @OnClick(R.id.proPlace)
    public void clickOnProPlace(View view) {
        if (view.getTag() != null) {
            ((PopupMenu) view.getTag()).show();
        } else {
            IconDataCategory iconDataCategory = IconDataCategory.get();
            PopupMenu popupMenu = new PopupMenu(this, view);
            for (String typeItem : iconDataCategory.productPlaces) {
                popupMenu.getMenu().add(typeItem);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    proPlace.setText(menuItem.getTitle());
                    ironBuyPush.proPlace = menuItem.getTitle().toString();
                    return false;
                }
            });
            view.setTag(popupMenu);
            popupMenu.show();
        }
    }

    @OnClick(R.id.locateCity)
    public void clickOnLocateCity(final View view) {
        if (view.getTag() != null) {
            ((PopupMenu) view.getTag()).show();
        } else {
            PopupMenu popupMenu = new PopupMenu(this, view);
            for (int i = 0; i < CityCategory.instance().getTopCities().size(); i++) {
                City city = CityCategory.instance().getTopCities().get(i);
                popupMenu.getMenu().add(0, i, 0, city.name);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    PopupMenu popupMenu = new PopupMenu(EditBuyActivity.this, view);
                    final City topCity = CityCategory.instance().getTopCities().get(menuItem.getItemId());
                    List<City> subCities = CityCategory.instance().getSubCities(topCity.id);
                    if (subCities == null) {
                        return false;
                    }
                    for (int i = 0; i < subCities.size(); i++) {
                        City city = subCities.get(i);
                        popupMenu.getMenu().add(0, i, 0, city.name);
                    }
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            locateCity.setText(topCity.name + " " + menuItem.getTitle());
                            City subCity = topCity.sub.get(menuItem.getItemId());
                            view.setTag(R.id.city_mask, subCity.id);
                            ironBuyPush.locationCityId = subCity.id;
                            return false;
                        }
                    });
                    return false;
                }
            });
            view.setTag(popupMenu);
            popupMenu.show();
        }
    }

}
