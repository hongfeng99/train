package com.ikun.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.common.util.SnowUtil;
import com.ikun.train.business.domain.confirmOrder;
import com.ikun.train.business.domain.confirmOrderExample;
import com.ikun.train.business.mapper.confirmOrderMapper;
import com.ikun.train.business.req.confirmOrderQueryReq;
import com.ikun.train.business.req.confirmOrderSaveReq;
import com.ikun.train.business.resp.confirmOrderQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class confirmOrderService {
private static final Logger LOG = LoggerFactory.getLogger(confirmOrderService.class);

@Resource
private confirmOrderMapper confirmOrderMapper;

public void save(confirmOrderSaveReq req){
DateTime now = DateTime.now();
confirmOrder confirmOrder = BeanUtil.copyProperties(req, confirmOrder.class);

if(ObjectUtil.isNull(req.getId())){
confirmOrder.setId(SnowUtil.getSnowflakeNextId());
confirmOrder.setCreateTime(now);
confirmOrder.setUpdateTime(now);
confirmOrderMapper.insert(confirmOrder);
}else{
confirmOrder.setUpdateTime(now);
confirmOrderMapper.updateByPrimaryKey(confirmOrder);
}
}

public PageResp<confirmOrderQueryResp> queryList(confirmOrderQueryReq req){
    confirmOrderExample confirmOrderExample = new confirmOrderExample();
    confirmOrderExample.setOrderByClause("id desc");
    confirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();


    LOG.info("查询页码：{}", req.getPage());
    LOG.info("每页条数：{}", req.getSize());
    PageHelper.startPage(req.getPage(),req.getSize());
    List<confirmOrder> list = confirmOrderMapper.selectByExample(confirmOrderExample);

    PageInfo<confirmOrder> pageInfo = new PageInfo<>(list);
    LOG.info("总行数：{}", pageInfo.getTotal());
    LOG.info("总页数：{}", pageInfo.getPages());

    List<confirmOrderQueryResp> list1 = BeanUtil.copyToList(list, confirmOrderQueryResp.class);
        PageResp<confirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list1);
        return pageResp;
    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }
}