package com.agile.train.controller;

import com.agile.train.model.SourceFile;
import com.agile.train.util.PinganCodeUtil;
import japa.parser.ast.body.MethodDeclaration;
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

import java.io.File;
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

        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword(repositoryName, searchKeyword);
        ObjectMapper mapper = new ObjectMapper();
        String resultArrayJson = mapper.writeValueAsString(result);

        log.debug("resultArrayJson:" + resultArrayJson);
        log.debug("search finish");
        return resultArrayJson;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam("jarName") String jarName,
                       @RequestParam("path") String path,
                       ModelMap model) throws Exception {
        log.debug("show start");
        log.debug("jarName:" + jarName);
        log.debug("path:" + path);
        String jarFileName = repositoryName + File.separator + jarName;
        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInJar(jarFileName, path);
        List<MethodDeclaration> methods = PinganCodeUtil.retrieveMethodsByFileNameInJar(jarFileName, path);
        model.put("sourceCode", sourceCode);
        model.put("methods", methods);
        log.debug("show finish");
        return "show";
    }

}
