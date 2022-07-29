package com.xlt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlt.mapper.IBrandMapper;
import com.xlt.model.po.BrandPo;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.PagedResponse;
import com.xlt.model.vo.BrandVo;
import com.xlt.model.vo.PageVo;
import com.xlt.service.IBrandService;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.ObjectUtil;
import com.xlt.utils.common.PoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BrandService implements IBrandService {

    @Autowired
    private IBrandMapper brandMapper;

    @Override
    public PagedResponse<List<BrandVo>> queryBrandPageList(BrandVo brandVo, PageVo pageVo) {
        QueryWrapper<BrandPo> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(brandVo.getBrandId())) {
            queryWrapper.eq("brandId", brandVo.getBrandId());
        }
        if (StringUtils.isNotEmpty(brandVo.getName())) {
            queryWrapper.like("name", brandVo.getName());
        }
        if (StringUtils.isNotEmpty(brandVo.getInitial())) {
            queryWrapper.eq("initial", brandVo.getInitial());
        }
        Page<BrandPo> page = new Page<>(pageVo.getCurrentPage(), pageVo.getPageSize());
        Page<BrandPo> brandPoPage = brandMapper.selectPage(page, queryWrapper);
        List<BrandVo> brandVos = ObjectUtil.convertObjsList(brandPoPage.getRecords(), BrandVo.class);
        pageVo.setTotalPages(page.getPages());
        pageVo.setTotal(page.getTotal());
        return new PagedResponse<>(brandVos, pageVo);
    }

    @Override
    public BasicResponse createBrand(BrandVo brandVo) {
        log.info("create input info:{}", brandVo);
        AssertUtil.isStringEmpty(brandVo.getName(), "name can't be empty");
        AssertUtil.isStringEmpty(brandVo.getImage(), "image url can't empty");
        QueryWrapper<BrandPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", brandVo.getName());
        List<BrandPo> brandPos = brandMapper.selectList(queryWrapper);
        AssertUtil.isTrue(!CollectionUtils.isEmpty(brandPos), "Brand " + brandVo.getName() + " has exist");
        brandVo.setInitial(brandVo.getName().substring(0, 1));
        BrandPo brandPo = ObjectUtil.convertObjs(brandVo, BrandPo.class);
        PoUtil.buildCreateUserInfo(brandPo);
        brandMapper.insert(brandPo);
        return new BasicResponse();
    }

    @Override
    public BasicResponse updateBrand(BrandVo brandVo) {
        log.info("update info:{}", brandVo);
        AssertUtil.isNull(brandVo.getBrandId(), "brandId can't be empty");
        BrandPo existBrandPo = brandMapper.selectById(brandVo.getBrandId());
        AssertUtil.isNull(existBrandPo, "brandId " + brandVo.getBrandId() + " not exist");
        LambdaUpdateWrapper<BrandPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrandPo::getBrandId, brandVo.getBrandId());
        if (StringUtils.isNotEmpty(brandVo.getName()) && !brandVo.getName().equals(existBrandPo.getName())) {
            updateWrapper.set(BrandPo::getName, brandVo.getName());
            updateWrapper.set(BrandPo::getInitial, brandVo.getName().substring(0, 1));
        }
        updateWrapper.set(StringUtils.isNotEmpty(brandVo.getImage()), BrandPo::getImage, brandVo.getImage());
        brandMapper.update(null, updateWrapper);
        return new BasicResponse();
    }

    @Override
    public BasicResponse deleteBrand(Long id) {
        AssertUtil.isNull(id, "id can't be empty");
        BrandPo brandPo = brandMapper.selectById(id);
        AssertUtil.isNull(brandPo, "brand not exist");
        brandMapper.deleteById(id);
        return new BasicResponse();
    }
}
