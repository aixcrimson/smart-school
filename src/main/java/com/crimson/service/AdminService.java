package com.crimson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crimson.pojo.Admin;
import com.crimson.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    /**
     * 登录
     * @param loginForm
     * @return
     */
    Admin login(LoginForm loginForm);


    /**
     * 管理员获取首页
     * @param userId
     * @return
     */
    Admin getAdminById(Long userId);
}
