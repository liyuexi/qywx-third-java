package com.tobdev.qywxthird.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobdev.qywxthird.utils.CookieUtils;
import com.tobdev.qywxthird.utils.JWTUtils;
import com.tobdev.qywxthird.utils.JsonData;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class H5LoginIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String accessToken = request.getHeader("token");
            if(accessToken == null){
                accessToken = request.getParameter("token");
            }
            if(accessToken == null){
                accessToken = CookieUtils.getCookie(request,"token");
            }
            if(StringUtils.isNoneBlank(accessToken)){
                Claims claims = JWTUtils.checkJWT(accessToken);
                if(claims == null){
                    //无登录或过期
                    sendJsonMessage(response, JsonData.buildError("登录过期，请重新登录"));
                    return  false;
                }
                //获取信息 设置信息
                String coprId = (String) claims.get("corp_id");
                String userId = (String)claims.get("user_id");
                request.setAttribute("corp_id",coprId);
                request.setAttribute("user_id",userId);
                return  true;
            }
            sendJsonMessage(response, JsonData.buildError("登录过期，请重新登录"));
            return false;
        }catch(Exception e){
            sendJsonMessage(response, JsonData.buildError("登录过期，请重新登录"));
            return false;
        }

    }

    public  static void sendJsonMessage(HttpServletResponse response,Object obj){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(objectMapper.writeValueAsString(obj));
            writer.close();
            response.flushBuffer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
