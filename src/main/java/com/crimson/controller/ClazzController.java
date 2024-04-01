package com.crimson.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crimson.pojo.Clazz;
import com.crimson.service.ClazzService;
import com.crimson.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @ApiOperation("分页条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询页码数") @PathVariable(value = "pageNo") Integer pageNo,
            @ApiParam("分页查询页大小") @PathVariable(value = "pageSize") Integer pageSize,
            @ApiParam("分页查询年级名") String gradeName,
            @ApiParam("分页查询模糊匹配班级名") String name
    ) {
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> pageRs = clazzService.getGradeByOpr(page, gradeName, name);
        return Result.ok(pageRs);
    }

    @ApiOperation("新增或者修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON转换后端Clazz数据模型") @RequestBody Clazz clazz
    ) {
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("删除一个或者多个班级信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazzByIds(
            @ApiParam("多个班级的JSON") @RequestBody List<Integer> ids
    ) {
        clazzService.removeByIds(ids);
        return Result.ok();
    }
}
