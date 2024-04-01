package com.crimson.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crimson.pojo.Grade;
import com.crimson.service.GradeService;
import com.crimson.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;


    /**
     * 分页查询年级信息
     * @param pageNo
     * @param pageSize
     * @param gradeName
     * @return
     */
    @ApiOperation("分页查询年级信息")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGradeByOpr(
            @ApiParam("分页查询页码数") @PathVariable(value = "pageNo") Integer pageNo,
            @ApiParam("分页查询页大小") @PathVariable(value = "pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配班级名") String gradeName)//模糊查询条件
    {
        // 设置分页信息
        Page<Grade> page = new Page<>(pageNo, pageSize);
        // 调用服务层方法，传入分页信息，和查询的条件
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);
        return Result.ok(pageRs);
    }

    @ApiOperation("添加或者删除年级信息")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON的grade对象转换后台数据模型")@RequestBody Grade grade
    ){
        // 调用服务层方法，实现添加或者修改年级信息
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("删除一个或多个年级")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("JSON的年级id集合，映射为后台List<Integer>")@RequestBody List<Integer> ids
            )
    {
        gradeService.removeByIds(ids);
        return Result.ok();
    }
}
