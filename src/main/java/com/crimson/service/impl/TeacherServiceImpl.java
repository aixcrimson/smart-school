package com.crimson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crimson.mapper.TeacherMapper;
import com.crimson.pojo.LoginForm;
import com.crimson.pojo.Teacher;
import com.crimson.service.TeacherService;
import com.crimson.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    /**
     * 教师登录
     * @param loginForm
     * @return
     */
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    /**
     * 教师获取首页
     * @param userId
     * @return
     */
    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 分页条件查询教师信息
     * @param pageParam
     * @param teacher
     * @return
     */
    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher) {
        // 封装条件
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        String name = teacher.getName();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name", name);
        }
        String clazzName = teacher.getClazzName();
        if(!StringUtils.isEmpty(clazzName)){
            queryWrapper.eq("clazz_name", clazzName);
        }
        // 排序条件
        queryWrapper.orderByAsc("id");
        queryWrapper.orderByAsc("name");
        IPage<Teacher> page = baseMapper.selectPage(pageParam, queryWrapper);
        Page<Teacher> page1 = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }
}
