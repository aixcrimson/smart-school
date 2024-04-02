package com.crimson.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crimson.pojo.Student;
import com.crimson.service.StudentService;
import com.crimson.util.MD5;
import com.crimson.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("分页条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Student student
    ) {
        // 准备分页信息封装的page对象
        Page<Student> page = new Page<>(pageNo, pageSize);
        // 获取分页的学生信息
        IPage<Student> iPage = studentService.getStudentByOpr(page, student);
        // 返回学生信息
        return Result.ok(iPage);
    }

    @ApiOperation("添加或者修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("要保存或修改的学生JSON") @RequestBody Student student
    ) {
        // 如果是添加学生，对学生的密码进行加密
        Integer id = student.getId();
        if (id == null || id == 0) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        // 保存学生信息进入数据库
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("删除一个或者多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result deleteStudentByIds(
            @ApiParam("多个学生id的JSON") @RequestBody List<Integer> ids

    ) {
        studentService.removeByIds(ids);
        return Result.ok();
    }
}
