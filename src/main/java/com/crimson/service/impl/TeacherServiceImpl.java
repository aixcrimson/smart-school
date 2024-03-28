package com.crimson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crimson.mapper.TeacherMapper;
import com.crimson.pojo.Teacher;
import com.crimson.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
}
