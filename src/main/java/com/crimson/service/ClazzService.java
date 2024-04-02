package com.crimson.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crimson.pojo.Clazz;

import java.util.List;

public interface ClazzService extends IService<Clazz> {

    /**
     * 分页条件查询班级信息
     * @param page
     * @param gradeName
     * @param name
     * @return
     */
    IPage<Clazz> getGradeByOpr(Page<Clazz> page, String gradeName, String name);

    /**
     * 获取所有班级信息
     * @return
     */
    List<Clazz> getClazzs();
}
