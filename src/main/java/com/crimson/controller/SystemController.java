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
import com.crimson.util.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
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
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        // 获取用户提交的验证码和session域中的验证码
        HttpSession session = request.getSession();
        String systemVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(systemVerifiCode)) {
            // session过期，验证码超时
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if (!loginVerifiCode.equalsIgnoreCase(systemVerifiCode)) {
            // 验证码有误
            return Result.fail().message("验证码有误，请刷新后重新输入");
        }
        // 验证码使用完毕，移除当前请求域中的验证码
        session.removeAttribute("verifiCode");

        // 准备一个Map集合，用户存放响应的信息
        Map<String, Object> map = new HashMap<>();
        // 根据用户身份，验证登录的用户信息
        switch (loginForm.getUserType()) {
            case 1://管理员身份
                try {
                    // 调用服务层登录方法，根据用户提交的LoginInfo信息，查询对应的Admin对象，找不到返回Null
                    Admin login = adminService.login(loginForm);
                    if (null != login) {
                        // 登录成功，将用户id和用户类型转换为token口令，作为信息响应给前端
                        map.put("token", JwtHelper.createToken(login.getId().longValue(), 1));
                    } else {
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
                    if (login != null) {
                        map.put("token", JwtHelper.createToken(login.getId().longValue(), 2));
                    } else {
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
                    if (login != null) {
                        map.put("token", JwtHelper.createToken(login.getId().longValue(), 3));
                    } else {
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

    @ApiOperation("通过token获取用户信息")
    @GetMapping("/getInfo")
    public Result getUserInfoByToken(HttpServletRequest request, @RequestHeader("token") String token) {
        // 获取用户中请求的token
        // 检查token是否过期 20H
        boolean isEx = JwtHelper.isExpiration(token);
        if (isEx) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        // 解析token，获取用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        // 准备一个Map集合用于存储响应的数据
        Map<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("user", admin);
                map.put("userType", 1);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("user", student);
                map.put("userType", 2);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("user", teacher);
                map.put("userType", 3);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("头像上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("文件二进制数据") @RequestPart("multipartFile") MultipartFile multipartFile
    ) {
        // 使用uuid随机生成文件名
        String uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toLowerCase();
        // 生成新的文件名字
        String filename = uuid.concat(multipartFile.getOriginalFilename());
        // 生成文件的保存路径(或者使用真正的文件存储服务器)
        String portraitPath = "D:/demo/尚硅谷毕设项目：智慧校园/smart-school/target/classes/public/upload/".concat(filename);
        // 保存文件
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String headerImg = "upload/" + filename;
        return Result.ok(headerImg);
    }
}
