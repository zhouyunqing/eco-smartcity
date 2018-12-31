package com.eco.wisdompark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eco.wisdompark.common.dto.ResponseData;
import com.eco.wisdompark.common.exceptions.WisdomParkException;
import com.eco.wisdompark.domain.dto.req.dept.*;
import com.eco.wisdompark.domain.model.Dept;
import com.eco.wisdompark.enums.ConsumeIdentity;
import com.eco.wisdompark.mapper.DeptMapper;
import com.eco.wisdompark.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eco.wisdompark.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组织架构 服务实现类
 * </p>
 *
 * @author litao
 * @since 2018-12-28
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Autowired
    private UserService userService;


    @Override
    public Integer addDeptLevel1(AddLevel1DeptDto addLevel1DeptDto) {

        if (this.isExists(addLevel1DeptDto.getDeptName()) > 0)
            throw new WisdomParkException(ResponseData.STATUS_CODE_463, "组织架构名称已经存在");
        Dept dept = new Dept();
        dept.setDeptUpId(0);
        dept.setDeptName(addLevel1DeptDto.getDeptName());
        ConsumeIdentity consumeIdentity = ConsumeIdentity.valueOf(addLevel1DeptDto.getConsumeIdentity());
        if (consumeIdentity == null) throw new WisdomParkException(ResponseData.STATUS_CODE_464, "未匹配到消费类型");
        dept.setConsumeIdentity(consumeIdentity.getCode());
        Integer result = baseMapper.insert(dept);
        return result;
    }

    @Override
    public Integer addDeptLevel2(AddLevel2DeptDto addLevel2DeptDto) {
        if (StringUtils.isBlank(addLevel2DeptDto.getDeptName()))
            throw new WisdomParkException(ResponseData.STATUS_CODE_465, "请输入组织架构名称");
        if (this.isExists(addLevel2DeptDto.getDeptName()) > 0)
            throw new WisdomParkException(ResponseData.STATUS_CODE_463, "组织架构名称已经存在");
        Dept l1Dept = baseMapper.selectById(addLevel2DeptDto.getId());
        Integer result = null;
        if (l1Dept != null) {
            Dept dept = new Dept();
            dept.setDeptUpId(l1Dept.getId());
            dept.setDeptName(addLevel2DeptDto.getDeptName());
            dept.setConsumeIdentity(l1Dept.getConsumeIdentity());
            result = baseMapper.insert(dept);
        }
        return result;
    }

    @Override
    public List<DeptDto> getLevel1Dept(GetLevel1DeptDto getLevel1DeptDto) {
        QueryWrapper<Dept> wrapper = new QueryWrapper<Dept>();
        wrapper.eq("dept_up_id", 0);
        if (StringUtils.isNotBlank(getLevel1DeptDto.getDeptName())) {
            wrapper.like("dept_name", getLevel1DeptDto.getDeptName());
        }
        List<Dept> depts = baseMapper.selectList(wrapper);
        return this.convertDto(depts);
    }

    @Override
    public List<DeptDto> getLevel2Dept(AddLevel2DeptDto addLevel2DeptDto) {

        QueryWrapper<Dept> wrapper = new QueryWrapper<Dept>();
        wrapper.eq("dept_up_id", addLevel2DeptDto.getId());
        if (StringUtils.isNotBlank(addLevel2DeptDto.getDeptName())) {
            wrapper.like("dept_name", addLevel2DeptDto.getDeptName());
        }
        List<Dept> depts = baseMapper.selectList(wrapper);
        return this.convertDto(depts);
    }

    @Override
    public Integer delDept(DelDeptDto delDeptDto) {
        if (userService.countByDept(delDeptDto.getId()) > 0)
            throw new WisdomParkException(ResponseData.STATUS_CODE_467, "组织架构下存在人员无法删除");

        return  baseMapper.deleteById(delDeptDto.getId());
    }

    private Integer isExists(String deptName) {
        QueryWrapper<Dept> wrapper = new QueryWrapper<Dept>();
        wrapper.eq("dept_name", deptName);
        return baseMapper.selectCount(wrapper);

    }

    private List<DeptDto> convertDto(List<Dept> depts) {
        List<DeptDto> result = new ArrayList<DeptDto>();
        if (depts != null && depts.size() > 0) {
            depts.forEach(e -> {
                DeptDto dto = new DeptDto();
                BeanUtils.copyProperties(e, dto);
                result.add(dto);
            });
        }
        return result;

    }
}