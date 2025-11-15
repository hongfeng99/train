package com.ikun.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.common.util.SnowUtil;
import com.ikun.train.business.domain.SkToken;
import com.ikun.train.business.domain.SkTokenExample;
import com.ikun.train.business.mapper.SkTokenMapper;
import com.ikun.train.business.req.SkTokenQueryReq;
import com.ikun.train.business.req.SkTokenSaveReq;
import com.ikun.train.business.resp.SkTokenQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkTokenService {
private static final Logger LOG = LoggerFactory.getLogger(SkTokenService.class);

@Resource
private SkTokenMapper skTokenMapper;

public void save(SkTokenSaveReq req){
DateTime now = DateTime.now();
SkToken skToken = BeanUtil.copyProperties(req, SkToken.class);

if(ObjectUtil.isNull(req.getId())){
skToken.setId(SnowUtil.getSnowflakeNextId());
skToken.setCreateTime(now);
skToken.setUpdateTime(now);
skTokenMapper.insert(skToken);
}else{
skToken.setUpdateTime(now);
skTokenMapper.updateByPrimaryKey(skToken);
}
}

public PageResp<SkTokenQueryResp> queryList(SkTokenQueryReq req){
    SkTokenExample skTokenExample = new SkTokenExample();
    skTokenExample.setOrderByClause("id desc");
    SkTokenExample.Criteria criteria = skTokenExample.createCriteria();


    LOG.info("查询页码：{}", req.getPage());
    LOG.info("每页条数：{}", req.getSize());
    PageHelper.startPage(req.getPage(),req.getSize());
    List<SkToken> list = skTokenMapper.selectByExample(skTokenExample);

    PageInfo<SkToken> pageInfo = new PageInfo<>(list);
    LOG.info("总行数：{}", pageInfo.getTotal());
    LOG.info("总页数：{}", pageInfo.getPages());

    List<SkTokenQueryResp> list1 = BeanUtil.copyToList(list, SkTokenQueryResp.class);
        PageResp<SkTokenQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list1);
        return pageResp;
    }

    public void delete(Long id) {
        skTokenMapper.deleteByPrimaryKey(id);
    }
}