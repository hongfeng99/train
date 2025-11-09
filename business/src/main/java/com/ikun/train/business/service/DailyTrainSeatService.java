package com.ikun.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ikun.train.business.domain.DailyTrainSeat;
import com.ikun.train.business.domain.DailyTrainSeatExample;
import com.ikun.train.business.domain.TrainSeat;
import com.ikun.train.business.domain.TrainStation;
import com.ikun.train.business.mapper.DailyTrainSeatMapper;
import com.ikun.train.business.req.DailyTrainSeatQueryReq;
import com.ikun.train.business.req.DailyTrainSeatSaveReq;
import com.ikun.train.business.resp.DailyTrainSeatQueryResp;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.common.util.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainSeatService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private TrainSeatService trainSeatService;

    @Resource
    private TrainStationService trainStationService;

    public void save(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);

        if (ObjectUtil.isNull(req.getId())) {
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateByPrimaryKey(dailyTrainSeat);
        }
    }

    public PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("date desc, train_code asc, carriage_index asc, carriage_seat_index asc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }


        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainSeat> list = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);

        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(list);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainSeatQueryResp> list1 = BeanUtil.copyToList(list, DailyTrainSeatQueryResp.class);
        PageResp<DailyTrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list1);
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void genDaily(Date date, String trainCode){

        // 删除该车次当日的座位数据
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("date desc, train_code asc, carriage_index asc, carriage_seat_index asc");
        dailyTrainSeatExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date);
        dailyTrainSeatMapper.deleteByExample(dailyTrainSeatExample);

        // 生成该车次当日的座位数据
        LOG.info("开始生成日期为【{}】，车次为【{}】的车座数据", DateUtil.formatDate(date),trainCode);
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
        StringBuffer Sell= new StringBuffer();
        for(int i=1;i<trainStations.size();i++){
            Sell.append('0');
        }
        String sell = Sell.toString();

        List<TrainSeat> trainSeats = trainSeatService.selectByTrainCode(trainCode);
        if(CollUtil.isEmpty(trainSeats)){
            LOG.info("该车次无基础车座数据，结束");
        }
        for(TrainSeat trainSeat:trainSeats){
            DateTime now = DateTime.now();
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setSell(sell);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }
    }


    public int countSeat(Date date, String trainCode, String seatType){
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        example.createCriteria().andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andSeatTypeEqualTo(seatType);
        long count = dailyTrainSeatMapper.countByExample(example);
        if(count==0L){
            return -1;
        }
        return (int)count ;
    }

    public List<DailyTrainSeat> selectByCarriage(Date date, String trainCode, Integer carriageIndex) {
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        example.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andCarriageIndexEqualTo(carriageIndex);
        return dailyTrainSeatMapper.selectByExample(example);
    }

}