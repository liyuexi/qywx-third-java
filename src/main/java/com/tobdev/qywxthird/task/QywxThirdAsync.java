package com.tobdev.qywxthird.task;

import com.tobdev.qywxthird.model.entity.WechatCorpLogin;
import com.tobdev.qywxthird.service.QywxThirdCacheService;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Async
public class QywxThirdAsync {

    @Autowired
    QywxThirdCacheService qywxThirdCacheService;

    public void  authLoginTask(String companyId){

        WechatCorpLogin login = null;
        try {
            login = qywxThirdCacheService.getAuthLoginCache(companyId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建无Chrome无头参数
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.setHeadless(true);
        //Chrome在root权限下跑
        /**  options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
         options.addArguments("--headless");
         options.setExperimentalOption("useAutomationExtension", false);
         options.addArguments("start-maximized"); // open Browser in maximized mode
         options.addArguments("disable-infobars"); // disabling infobars
         options.addArguments("--disable-extensions"); // disabling extensions
         options.addArguments("--disable-gpu"); // applicable to windows os only
         options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
         **/
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(chromeOptions);
//        WebDriver driver = new ChromeDriver();
        try {

            //打开登录授权链接所在页面
            String qywxUrl = "http://tobdev.ant-xy.com:9900/ser/index";
            System.out.println(qywxUrl);
            driver.get(qywxUrl);

            //获取链接并跳转扫码页
            driver.findElement(By.cssSelector("#oauth_a")).click();
//        String oauthUrl =   oauthelement.getAttribute("src");
//        driver.get(oauthUrl);

            //切换到新窗口
            List<String> list=new ArrayList( driver.getWindowHandles());
            driver.switchTo().window(list.get(1));

            //切换到iframe
            WebElement iframe = driver.findElements(By.tagName("iframe")).get(0);
            driver.switchTo().frame(iframe);

           // WebElement xx = driver.findElement(By.cssSelector("#oauth_a"));

            //等待扫码完成
            WebDriverWait waitScan ;
            waitScan =  new  WebDriverWait(driver,30000);
            waitScan.until( ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".qrcode_login_img")));


            //在扫码页获取二维码链接 并保存
            WebElement qrElement = driver.findElement(By.cssSelector(".qrcode_login_img"));
            String qrUrl =   qrElement.getAttribute("src");
            //1 设置缓存logourl
            login.setState(1);
            login.setLogoUrl(qrUrl);
            qywxThirdCacheService.setAuthLoginCache(login);

            //等待扫码完成
            WebDriverWait wait = new  WebDriverWait(driver,30000);
            wait.until( ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#login-success")));


            //跳转到企业微信页获取cookie保存
            driver.get("https://open.work.weixin.qq.com");
            Cookie cookie  =driver.manage().getCookieNamed("wwopen.open.sid");
            System.out.println(cookie);
            System.out.println(cookie.toString());
            //2 设置cookie及成功
            login.setState(7);
            login.setCookieSid(cookie.toString());

        }catch (Exception e){
            //设置失败
            login.setState(9);
            e.printStackTrace();
        }

        qywxThirdCacheService.setAuthLoginCache(login);
        //关闭selenium
        driver.close();
//        return  login;
    }


}
