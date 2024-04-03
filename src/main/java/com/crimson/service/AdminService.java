package com.crimson.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页条件查询管理员信息
     * @param page
     * @param adminName
     * @return
     */
    IPage<Admin> getAdminByOpr(Page<Admin> page, String adminName);
}
