package com.agile.train;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);

    @Value("${appKey}")
    private String appKey;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() throws Exception {
        log.debug("welcome start ");
        return "index"; 
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String result(@RequestParam("searchKeyword") String searchKeyword, ModelMap model) throws Exception {
        log.debug("search start");
        log.debug("searchKeyword:" + searchKeyword);
        log.debug("search finish");
        return "index";
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public
    @ResponseBody
    String data() throws Exception {
        JSONObject result = new JSONObject();
        result.put("message", "hello world");
        return result.toString();
    }

    @RequestMapping(value = "/property", method = RequestMethod.GET)
    public
    @ResponseBody
    String property() throws Exception {
        JSONObject result = new JSONObject();
        result.put("property", appKey);
        return result.toString();
    }
}
