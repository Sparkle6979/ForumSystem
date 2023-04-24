package com.example.forumsystem.controller;

import com.example.forumsystem.annotation.LoginRequired;
import com.example.forumsystem.pojo.User;
import com.example.forumsystem.service.UserService;
import com.example.forumsystem.utils.ForumUtil;
import com.example.forumsystem.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/22 15:01
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${forum.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","您还未选择图片");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }

//        生成随机文件名
        filename = ForumUtil.generateUUID() + suffix;
        System.out.println(filename);
        // 确定文件在硬盘上存储的路径
        File dest  = new File(uploadPath + "/" + filename);

        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            e.printStackTrace();
        }

        User user = hostHolder.getUser();
        // 更新用户的headurl，稍后需要重写这个请求
        String headerUrl = domain + contextPath + "user/header/" + filename;
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }

    //
    @GetMapping("/header/{filename}")
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        filename = uploadPath + "/" + filename;
//        System.out.println("---------------" + filename);
        String suffix = filename.substring(filename.lastIndexOf("."));

        response.setContentType("image/" + suffix);

        try {
            FileInputStream fs = new FileInputStream(filename);
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fs.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
            fs.close();
        } catch (FileNotFoundException e) {
            logger.error("文件不存在" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("文件上传失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping("/updatepwd")
    public String updatePassword(String oldPassword,String newPassword,Model model){

        User user = hostHolder.getUser();
        oldPassword = ForumUtil.md5(oldPassword + user.getSalt());
//        System.out.println("++++++++++" + oldPassword);
        if (!user.getPassword().equals(oldPassword)){
            model.addAttribute("oldPasswordMsg","旧密码输入错误，请重新输入");
            return "/site/setting";
        }

        newPassword = ForumUtil.md5(newPassword + user.getSalt());
        userService.updatePassword(user.getId(),newPassword);
        return "redirect:/logout";

    }
}
