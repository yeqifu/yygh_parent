package com.yeqifu.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.yygh.common.exception.YyghException;
import com.yeqifu.yygh.common.result.Result;
import com.yeqifu.yygh.common.utils.MD5;
import com.yeqifu.yygh.hosp.service.HospitalSetService;
import com.yeqifu.yygh.model.hosp.HospitalSet;
import com.yeqifu.yygh.vo.hosp.HospitalQueryVo;
import com.yeqifu.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: 落亦-
 * @Date: 2021/4/25 23:33
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询所有的医院设置
     *
     * @return
     */
    @ApiOperation("查询所有医院设置")
    @GetMapping("/findAll")
    public Result findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 逻辑删除医院设置
     *
     * @param id 医院ID
     */
    @ApiOperation("逻辑删除医院设置")
    @ApiParam(name = "id", value = "医院设置的ID", required = true)
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id) {
        // 修改成功就是true ， 修改失败就是 false
        boolean flag = hospitalSetService.removeById(id);
        if (flag == true) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 带条件的分页查询所有医院设置
     *
     * @param current            当前页码
     * @param limit              每页条数
     * @param hospitalSetQueryVo 查询条件
     * @return
     */
    @ApiOperation(value = "带条件的分页查询所有医院设置")
    @PostMapping("/findPageHospitalSet/{current}/{limit}")
    public Result findPageHospitalSet(@PathVariable("current") Long current,
                                      @PathVariable("limit") Long limit,
                                      @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 声明一个分页对象
        Page<HospitalSet> page = new Page<HospitalSet>(current, limit);
        // 声明查询条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<HospitalSet>();
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHosname())) {
            wrapper.like("hosname", hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode())) {
            wrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        return Result.ok(hospitalSetPage);
    }

    /**
     * 添加医院设置
     *
     * @param hospitalSet
     * @return
     */
    @ApiOperation(value = "添加医院设置")
    @PostMapping("/addHospitalSet")
    public Result addHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 0-不可用  1-可用
        hospitalSet.setStatus(1);
        // 设置密钥
        Random random = new Random();
        String signKey = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(signKey);
        boolean save = hospitalSetService.save(hospitalSet);
        if (save == true) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据医院设置ID查询医院设置
     * @param id
     * @return
     */
    @ApiOperation(value = "根据医院设置ID查询医院设置")
    @GetMapping("/getHospitalSetById/{id}")
    public Result getHospitalSetById(@PathVariable("id") Long id){
        try{
            int a = 1/0;
        }catch (Exception e){
            throw new YyghException("分母不能为0",201);
        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 修改医院设置
     * @param hospitalSet
     * @return
     */
    @ApiOperation(value = "修改医院设置")
    @PutMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag == true){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 批量删除医院设置
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteBatchHospitalSet")
    public Result deleteBatchHospitalSet(@RequestBody List<Long> ids){
        boolean flag = hospitalSetService.removeByIds(ids);
        if (flag == true){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 锁定与解锁医院设置
     * @param id    医院设置ID
     * @param status    医院设置状态
     * @return
     */
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id, @PathVariable Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag == true){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 通过短信发送密钥
     * @param id
     * @return
     */
    @PostMapping("/sendSignKeyHospitalSet/{id}")
    public Result sendSignKeyHospitalSet(@PathVariable("id") Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.getHosname();
        hospitalSet.getSignKey();
        // TODO  短信发送
        return Result.ok();
    }

}
