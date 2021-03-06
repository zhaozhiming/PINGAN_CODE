package com.agile.train.controller;

import com.agile.train.model.MethodDisplayer;
import com.agile.train.model.SourceFile;
import com.agile.train.util.PinganCodeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
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
        log.debug("repositoryName:" + repositoryName);

        String[] searchKeywords = searchKeyword.split("\\s");
        log.debug("searchKeyword:" + Arrays.toString(searchKeywords));

        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword(repositoryName, searchKeywords);
        ObjectMapper mapper = new ObjectMapper();
        String resultArrayJson = mapper.writeValueAsString(result);

        log.debug("resultArrayJson:" + resultArrayJson);
        log.debug("search finish");
        return resultArrayJson;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam("sourceFilePath") String sourceFilePath,
                       @RequestParam("jarFilePath") String jarFilePath,
                       ModelMap model) throws Exception {
        log.debug("show start");
        log.debug("sourceFilePath:" + sourceFilePath);
        log.debug("jarFilePath:" + jarFilePath);
        String sourceCode = PinganCodeUtil.readSourceCodeBy(jarFilePath, sourceFilePath);
        List<MethodDisplayer> methods = PinganCodeUtil.retrieveMethodsBy(jarFilePath, sourceFilePath);
        model.put("sourceCode", sourceCode);
        model.put("methods", methods);
        log.debug("show finish");
        return "show";
    }

}
