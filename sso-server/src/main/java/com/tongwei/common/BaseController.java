package com.tongwei.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tongwei.common.dao.BaseQuery;

/**
 * @author yangz
 * @date 2017年3月31日 下午10:49:19
 */
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    protected Map<String, Object> renderPage(List<?> data, BaseQuery<?> query) {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("rows", data);
        map.put("total", query.getTotal());
        return map;
    }

}
