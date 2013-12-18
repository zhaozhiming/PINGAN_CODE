package com.agile.train.controller;

import com.agile.train.util.PafaCodeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);

    @Value("${repositoryName}")
    private String repositoryName;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() throws Exception {
        log.debug("welcome start ");
        return "index"; 
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public
    @ResponseBody
    String result(@RequestParam("searchKeyword") String searchKeyword) throws Exception {
        log.debug("search start");
        log.debug("searchKeyword:" + searchKeyword);
        log.debug("repositoryName:" + repositoryName);
        List<String> result = PafaCodeUtil.searchFile(repositoryName, searchKeyword);

        JSONArray resultArrayJson = new JSONArray();
        for (String path : result) {
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("jarName", "cglib-2.2-sources.jar");
            resultJsonObject.put("version", "1.0");
            resultJsonObject.put("path", path);
            resultArrayJson.put(resultJsonObject);
        }
        log.debug("resultArrayJson:" + resultArrayJson.toString());
        log.debug("search finish");
        return resultArrayJson.toString();
    }

}
