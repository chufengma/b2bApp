package com.taobuxiu.driver.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class BaseResponse {
    public String errorMsg;
    public int status;
    public JsonNode data;
}
