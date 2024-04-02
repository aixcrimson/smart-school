package com.crimson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crimson.mapper.ClazzMapper;
import com.crimson.pojo.Clazz;
import com.crimson.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    /**
     * 分页条件查询
     * @param pageParam
     * @param gradeName
     * @param name
     * @return
     */
    @Override
    public IPage<Clazz> getGradeByOpr(Page<Clazz> pageParam, String gradeName, String name) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        // 设置查询条件
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.eq("grade_name", gradeName);
        }
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 设置排序规则
        queryWrapper.orderByAsc("id");
        queryWrapper.orderByAsc("name");
        // 分页查询数据
        Page page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }

    /**
     * 获取所有班级信息
     * @return
     */
    @Override
    public List<Clazz> getClazzs() {
        List<Clazz> clazzList = baseMapper.selectList(null);
        return clazzList;
    }
}
