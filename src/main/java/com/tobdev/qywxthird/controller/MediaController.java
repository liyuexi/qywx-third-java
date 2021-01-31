package com.tobdev.qywxthird.controller;

import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class MediaController {

    @Autowired
    private QywxThirdService qywxThirdService;

    @RequestMapping("/media/index")
    public String index(HttpServletRequest request, ModelMap model) throws Exception{

        String sendTextUrl = CommonUtils.RouteToUrl(request,"/media/download");
        model.put("mdedia_download_url",sendTextUrl);
        return  "media/index";
    }

    @RequestMapping({"/media/download"})
    void download(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "media_id") String mediaId) throws IOException {

        String corpId = (String) request.getAttribute("corp_id");
        byte[]  filebytes =  qywxThirdService.downloadMedia(corpId,mediaId);

        OutputStream out = response.getOutputStream();
        out.write(filebytes);
        out.flush();
        out.close();

    }

//    @RequestMapping({"/media/upload"})
//    String upload(HttpServletRequest request, ModelMap model){
//        //先判断是否登录
//
//        return  ;
//    }


}
