package com.crimson.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crimson.pojo.Grade;

import java.util.List;

public interface GradeService extends IService<Grade> {

    /**
     * 分页查询年级信息
     * @param page
     * @param gradeName
     * @return
     */
    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);

    /**
     * 获取所有Grade信息
     * @return
     */
    List<Grade> getGrades();
}
