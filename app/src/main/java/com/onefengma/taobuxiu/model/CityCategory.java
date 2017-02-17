package com.onefengma.taobuxiu.model;

import com.alibaba.fastjson.JSON;
import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.model.entities.City;
import com.onefengma.taobuxiu.utils.PinyinUtils;
import com.onefengma.taobuxiu.utils.StringUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chufengma on 16/8/27.
 */
public class CityCategory {

    public static List<City> cities;

    private static CityCategory instance;

    public static CityCategory instance() {
        if (instance == null) {
            instance = new CityCategory();
        }
        return instance;
    }

    public List<City> getTopCities() {
        if (cities == null) {
            try {
                InputStream inputStream = MainApplication.getContext().getAssets().open("city.json");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                char[] tmp = new char[1024];
                int len = 0;
                StringBuilder stringBuilder = new StringBuilder();
                while ((len = inputStreamReader.read(tmp)) != -1) {
                    stringBuilder.append(tmp, 0, len);
                }
                cities = JSON.parseObject(stringBuilder.toString(), Data.class).data;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cities;
    }

    public List<City> getMainCities() {
        List<City> mainCities = new ArrayList<>();
        for (City city : getTopCities()) {
            if (city.type == 1) {
                for (City city1 : city.sub) {
                    if (!StringUtils.equals(city1.name, "其他")) {
                        city1.pinyin = PinyinUtils.getPinYin(city1.name);
                        mainCities.add(city1);
                    }
                }
            } else if (city.type == 0) {
                if (!StringUtils.equals(city.name, "其他")) {
                    city.pinyin = PinyinUtils.getPinYin(city.name);
                    mainCities.add(city);
                }
            }
        }
        Collections.sort(mainCities, new CityComparator());
        return mainCities;
    }

    public List<City> getHotCities() {
        List<City> hotCities = new ArrayList<>();
        List<String> hotName = new ArrayList<>();
        hotName.add("北京");
        hotName.add("上海");
        hotName.add("深圳");
        hotName.add("广州");
        hotName.add("成都");
        hotName.add("杭州");
        for(City city : getTopCities()) {
            if (hotName.contains(city.name)) {
                hotCities.add(city);
            }
            if (city.sub == null) {
                continue;
            }
            for(City subCity : city.sub) {
                if (hotName.contains(subCity.name)) {
                    hotCities.add(subCity);
                }
            }
        }
        return hotCities;
    }

    public List<City> getSubCities(String cityID) {
        for (City city : getTopCities()) {
            if (StringUtils.equals(cityID, city.id)) {
                return city.sub;
            }
            for (City subCity : city.sub) {
                if (StringUtils.equals(cityID, subCity.id)) {
                    return subCity.sub;
                }
            }
        }

        return null;
    }

    /**
     * a-z排序
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.pinyin.substring(0, 1);
            String b = rhs.pinyin.substring(0, 1);
            return a.compareTo(b);
        }
    }


    public City getCity(String cityId) {
        for (City city : getTopCities()) {
            if (StringUtils.equals(cityId, city.id)) {
                return city;
            } else if (city.sub != null) {
                for (City sub : city.sub) {
                    if (StringUtils.equals(cityId, sub.id)) {
                        return sub;
                    } else if (sub.sub != null) {
                        for (City sub2 : sub.sub) {
                            if (StringUtils.equals(cityId, sub2.id)) {
                                return sub2;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getCityDesc(String cityId) {
        try {
            City subCity = getCity(cityId);
            City topCity = getCity(subCity.fatherId);
            return topCity.name + " " + subCity.name;
        } catch (Exception e) {
            return "";
        }
    }

    public String getCityDesc2(String cityId) {
        try {
            City subCity = getCity(cityId);
            City topCity = getCity(subCity.fatherId);
            if (!StringUtils.isEmpty(topCity.fatherId)) {
                return getCity(topCity.fatherId).name + " " + topCity.name + " " + subCity.name;
            }
            return topCity.name + " " + subCity.name;
        } catch (Exception e) {
            return "";
        }
    }

    public static class Data {
        public List<City> data;
    }

}
