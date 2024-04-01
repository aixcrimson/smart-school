package com.crimson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crimson.mapper.GradeMapper;
import com.crimson.pojo.Grade;
import com.crimson.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    /**
     * 分页查询年级信息
     * @param pageParam
     * @param gradeName
     * @return
     */
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> pageParam, String gradeName) {
        // 设置查询条件
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName);
        }
        // 设置排序规则
        queryWrapper.orderByAsc("id");
        queryWrapper.orderByAsc("name");
        // 分页查询数据
        Page page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }

    /**
     * 获取所有Grade信息
     * @return
     */
    @Override
    public List<Grade> getGrades() {
        List<Grade> grades = baseMapper.selectList(null);
        return grades;
    }
}
