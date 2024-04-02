package com.crimson.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crimson.pojo.Teacher;
import com.crimson.service.TeacherService;
import com.crimson.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("分页条件查询教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Teacher teacher
    ) {
        // 准备分页信息封装的page对象
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        // 获取分页的教师信息
        IPage<Teacher> iPage = teacherService.getTeachersByOpr(page, teacher);
        return Result.ok(iPage);
    }

    @ApiOperation("添加或者修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result savaOrUpdateTeacher(
            @ApiParam("要保存或修改的教师JSON") @RequestBody Teacher teacher
    ) {
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("删除一个或者多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeachers(
            @ApiParam("多个学生id的JSON") @RequestBody List<Integer> ids
    ) {
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
