package com.crimson.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crimson.pojo.LoginForm;
import com.crimson.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    /**
     * 教师登录
     * @param loginForm
     * @return
     */
    Teacher login(LoginForm loginForm);

    /**
     * 教师获取首页
     * @param userId
     * @return
     */
    Teacher getTeacherById(Long userId);

    /**
     * 分页条件查询教师信息
     * @param page
     * @param teacher
     * @return
     */
    IPage<Teacher> getTeachersByOpr(Page<Teacher> page, Teacher teacher);
}
