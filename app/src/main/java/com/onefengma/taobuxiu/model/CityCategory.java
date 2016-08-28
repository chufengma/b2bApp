package com.onefengma.taobuxiu.model;

import com.alibaba.fastjson.JSON;
import com.onefengma.taobuxiu.MainApplication;
import com.onefengma.taobuxiu.model.entities.City;
import com.onefengma.taobuxiu.utils.StringUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
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

    public List<City> getSubCities(String cityID) {
        for(City city : getTopCities()) {
            if (StringUtils.equals(cityID, city.id)) {
                return city.sub;
            }
        }
        return null;
    }

    public City getCity(String cityId) {
        for(City city : getTopCities()) {
            if (StringUtils.equals(cityId, city.id)) {
                return city;
            } else if (city.sub != null){
               for(City sub : city.sub) {
                   if (StringUtils.equals(cityId, sub.id)) {
                       return sub;
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


    public static class Data {
        public List<City> data;
    }

}
