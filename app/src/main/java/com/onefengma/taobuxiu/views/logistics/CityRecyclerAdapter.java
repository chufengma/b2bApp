package com.onefengma.taobuxiu.views.logistics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onefengma.taobuxiu.R;
import com.onefengma.taobuxiu.model.CityCategory;
import com.onefengma.taobuxiu.model.entities.City;
import com.onefengma.taobuxiu.model.entities.Contact;
import com.onefengma.taobuxiu.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by baisoo on 16/9/24.
 */

public class CityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;

    private static final int VIEW_TYPE_COUNT = 2;
    private Context mContext;
    private List<City> mCitys;
    private List<String> characterList; // 字母List
    private List<Contact> resultList; // 最终结果（包含分组的字母）

    private List<City> mHotCitys;

    private HashMap<String, Integer> letterIndexes;

    private OnCityClickListener onCityClickListener;

    public static final int LOCATING    = 111;
    public static final int FAILED      = 666;
    public static final int SUCCESS     = 888;
    private int locateState = LOCATING;
    private String locatedCity;

    private final int ITEM_TYPE_CHARACTER = 5;
    private final int ITEM_TYPE_CITY = 6;


    public CityRecyclerAdapter(Context mContext, List<City> mCitys) {
        this.mContext = mContext;
        this.mCitys = mCitys == null ? new ArrayList<City>() : mCitys;
        mLayoutInflater = LayoutInflater.from(mContext);


        handleCity();
    }

    private void handleCity() {
        resultList = new ArrayList<>();
        characterList = new ArrayList<>();
        letterIndexes = new HashMap<>();

        letterIndexes.put("热", 0);

        for (int index = 2; index < mCitys.size(); index++) {
            //当前城市拼音首字母
            String name = PinyinUtils.getFirstLetter(mCitys.get(index).pinyin);
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            if (!characterList.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    characterList.add(character);
                    resultList.add(new Contact(character, ITEM_TYPE_CHARACTER));
                } else {
                    if (!characterList.contains("#")) {
                        characterList.add("#");
                        resultList.add(new Contact("#", ITEM_TYPE_CHARACTER));
                    }
                }
            }
            Contact cityContact = new Contact(mCitys.get(index).name, ITEM_TYPE_CITY);
            cityContact.city = mCitys.get(index);
            resultList.add(cityContact);
        }

        for (int i = 0; i < resultList.size(); i++) {
            Contact contact = resultList.get(i);
            if (contact.getmType() == ITEM_TYPE_CHARACTER){
                letterIndexes.put(contact.getmName(), i + 1);
            }
        }

        mHotCitys = CityCategory.instance().getHotCities();
    }


    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CHARACTER) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.item_character, parent, false));
        } else if (viewType == ITEM_TYPE_CITY) {
            View allCityView = mLayoutInflater.inflate(R.layout.item_city, parent, false);
            return new CityHolder(allCityView);
        } else {
            return new HotCityHolder(mLayoutInflater.inflate(R.layout.item_hot_city, parent, false), mContext);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mCharater.setText(resultList.get(position - 1).getmName());
        } else if (holder instanceof CityHolder) {

            ((CityHolder) holder).mCityName.setText(resultList.get(position - 1).getmName());
            ((CityHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //返回定位城市
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick(resultList.get(position - 1).city);
                    }
                }
            });
        }  else if (holder instanceof HotCityHolder) {
            ((HotCityHolder) holder).setDate(mHotCitys, onCityClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : resultList.get(position - 1).getmType();
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(City city);

        void onLocateClick();
    }


    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getPositionForSection(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }


}
