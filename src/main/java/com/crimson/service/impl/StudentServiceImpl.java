package com.crimson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crimson.mapper.StudentMapper;
import com.crimson.pojo.LoginForm;
import com.crimson.pojo.Student;
import com.crimson.service.ClazzService;
import com.crimson.service.StudentService;
import com.crimson.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    /**
     * 学生登录
     * @param loginForm
     * @return
     */
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    /**
     * 学生获取首页
     * @param userId
     * @return
     */
    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 分页条件查询学生信息
     * @param pageParam
     * @param student
     * @return
     */
    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        // 封装条件
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 获取学生姓名条件
        String name = student.getName();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 获取学生所在班级条件
        String clazzName = student.getClazzName();
        if (!StringUtils.isEmpty(clazzName)) {
            queryWrapper.eq("clazz_name", clazzName);
        }

        // 排序条件
        queryWrapper.orderByAsc("id");
        queryWrapper.orderByAsc("name");
        IPage<Student> pages = baseMapper.selectPage(pageParam, queryWrapper);
        return pages;
    }
}
