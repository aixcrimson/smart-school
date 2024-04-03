package com.crimson.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crimson.pojo.Admin;
import com.crimson.service.AdminService;
import com.crimson.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("页码数")@PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小")@PathVariable("pageSize") Integer pageSize,
            @ApiParam("条件查询管理员姓名") String adminName
    ){
        // 封装分页信息
        Page<Admin> page = new Page<>(pageNo, pageSize);
        IPage<Admin> pageRs = adminService.getAdminByOpr(page, adminName);
        return Result.ok(pageRs);
    }

    @ApiOperation("添加或者修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @RequestBody Admin admin
    ){
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @ApiOperation("删除一个或者多个管理员")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
