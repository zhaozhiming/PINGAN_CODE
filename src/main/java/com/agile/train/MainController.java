package com.agile.train;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);

    @Value("${appKey}")
    private String appKey;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) throws Exception {
        log.debug("index start");
        model.put("message", "hello world");
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String result(ModelMap model) throws Exception {
        log.debug("result start");
        return "result";
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
