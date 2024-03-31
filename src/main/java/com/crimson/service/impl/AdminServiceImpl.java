package com.crimson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crimson.mapper.AdminMapper;
import com.crimson.pojo.Admin;
import com.crimson.pojo.LoginForm;
import com.crimson.service.AdminService;
import com.crimson.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    /**
     * 超级管理员登录
     * @param loginForm
     * @return
     */
    @Override
    public Admin login(LoginForm loginForm) {
        // 创建Querywrapper对象
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        // 拼接查询条件
        queryWrapper.eq("name", loginForm.getUsername());
        // 转换成密文进行查询
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Admin admin = baseMapper.selectOne(queryWrapper);

        return admin;
    }

    /**
     * 获取管理员主页
     * @param userId
     * @return
     */
    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }
}
