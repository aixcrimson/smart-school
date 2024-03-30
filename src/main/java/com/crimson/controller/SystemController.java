package com.crimson.controller;

import com.crimson.pojo.Admin;
import com.crimson.pojo.LoginForm;
import com.crimson.pojo.Student;
import com.crimson.pojo.Teacher;
import com.crimson.service.AdminService;
import com.crimson.service.StudentService;
import com.crimson.service.TeacherService;
import com.crimson.util.CreateVerifiCodeImage;
import com.crimson.util.JwtHelper;
import com.crimson.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        // 获取验证码图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        // 获取图片上的验证码字符串
        String verifiCode = String.valueOf(CreateVerifiCodeImage.getVerifiCode());
        // 将验证码文本放入session域，为下一次验证做准备
        request.getSession().setAttribute("verifiCode", verifiCode);
        // 将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation("登录请求验证")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request){
        // 获取用户提交的验证码和session域中的验证码
        HttpSession session = request.getSession();
        String systemVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if("".equals(systemVerifiCode)){
            // session过期，验证码超时
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if(!loginVerifiCode.equalsIgnoreCase(systemVerifiCode)){
            // 验证码有误
            return Result.fail().message("验证码有误，请刷新后重新输入");
        }
        // 验证码使用完毕，移除当前请求域中的验证码
        session.removeAttribute("verifiCode");

        // 准备一个Map集合，用户存放响应的信息
        Map<String, Object> map = new HashMap<>();
        // 根据用户身份，验证登录的用户信息
        switch (loginForm.getUserType()){
            case 1://管理员身份
                try {
                    // 调用服务层登录方法，根据用户提交的LoginInfo信息，查询对应的Admin对象，找不到返回Null
                    Admin login = adminService.login(loginForm);
                    if(null != login){
                        // 登录成功，将用户id和用户类型转换为token口令，作为信息响应给前端
                        map.put("token", JwtHelper.createToken(login.getId().longValue(), 1));
                    }else {
                        throw new RuntimeException("用户名或者密码有误!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常，向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }

            case 2://学生身份
                try {
                    Student login = studentService.login(loginForm);
                    if(login != null){
                        map.put("token",JwtHelper.createToken(login.getId().longValue(), 2));
                    }else{
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 3://教师身份
                try {
                    Teacher login = teacherService.login(loginForm);
                    if(login != null){
                        map.put("token",JwtHelper.createToken(login.getId().longValue(), 3));
                    }else{
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        // 查无此用户，响应失败
        return Result.fail().message("查无此用户");
    }
}
