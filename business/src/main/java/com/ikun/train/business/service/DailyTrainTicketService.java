package com.ikun.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.common.util.SnowUtil;
import com.ikun.train.business.domain.DailyTrainTicket;
import com.ikun.train.business.domain.DailyTrainTicketExample;
import com.ikun.train.business.mapper.DailyTrainTicketMapper;
import com.ikun.train.business.req.DailyTrainTicketQueryReq;
import com.ikun.train.business.req.DailyTrainTicketSaveReq;
import com.ikun.train.business.resp.DailyTrainTicketQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyTrainTicketService {
private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

@Resource
private DailyTrainTicketMapper dailyTrainTicketMapper;

public void save(DailyTrainTicketSaveReq req){
DateTime now = DateTime.now();
DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);

if(ObjectUtil.isNull(req.getId())){
dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
dailyTrainTicket.setCreateTime(now);
dailyTrainTicket.setUpdateTime(now);
dailyTrainTicketMapper.insert(dailyTrainTicket);
}else{
dailyTrainTicket.setUpdateTime(now);
dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
}
}

public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req){
    DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
    dailyTrainTicketExample.setOrderByClause("id desc");
    DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();


    LOG.info("查询页码：{}", req.getPage());
    LOG.info("每页条数：{}", req.getSize());
    PageHelper.startPage(req.getPage(),req.getSize());
    List<DailyTrainTicket> list = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);

    PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(list);
    LOG.info("总行数：{}", pageInfo.getTotal());
    LOG.info("总页数：{}", pageInfo.getPages());

    List<DailyTrainTicketQueryResp> list1 = BeanUtil.copyToList(list, DailyTrainTicketQueryResp.class);
        PageResp<DailyTrainTicketQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list1);
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }
}