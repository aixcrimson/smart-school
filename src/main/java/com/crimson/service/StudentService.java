package com.crimson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crimson.pojo.LoginForm;
import com.crimson.pojo.Student;

public interface StudentService extends IService<Student> {
    /**
     * 学生登录
     * @param loginForm
     * @return
     */
    Student login(LoginForm loginForm);

    /**
     * 学生获取首页
     * @param userId
     * @return
     */
    Student getStudentById(Long userId);
}
