package com.ikun.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ikun.train.business.domain.DailyTrain;
import com.ikun.train.business.domain.DailyTrainTicket;
import com.ikun.train.business.domain.DailyTrainTicketExample;
import com.ikun.train.business.domain.TrainStation;
import com.ikun.train.business.enums.SeatTypeEnum;
import com.ikun.train.business.enums.TrainTypeEnum;
import com.ikun.train.business.mapper.DailyTrainTicketMapper;
import com.ikun.train.business.req.DailyTrainTicketQueryReq;
import com.ikun.train.business.req.DailyTrainTicketSaveReq;
import com.ikun.train.business.resp.DailyTrainTicketQueryResp;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.common.util.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class DailyTrainTicketService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;


    @Autowired
    private TrainStationService trainStationService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;



    public void save(DailyTrainTicketSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);

        if (ObjectUtil.isNull(req.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
        }
    }

    public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();
        if (ObjUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }
        if (ObjUtil.isNotEmpty(req.getStart())) {
            criteria.andStartEqualTo(req.getStart());
        }
        if (ObjUtil.isNotEmpty(req.getEnd())) {
            criteria.andEndEqualTo(req.getEnd());
        }


        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
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

    @Transactional
    public void genDaily(DailyTrain dailyTrain, Date date, String trainCode){
        LOG.info("开始生成日期为【{}】，车次为【{}】的车票信息", DateUtil.formatDate(date),trainCode);

        // 删除该车次当日车票数据
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);

        // 查出该车次的所有车站信息
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
        if(CollUtil.isEmpty(trainStations)){
            LOG.info("该日期的车次无车站基础信息，结束");
            return;
        }

        DateTime now = DateTime.now();
        for(int i=0;i<trainStations.size();i++){
            TrainStation trainStationStart = trainStations.get(i);
            BigDecimal sumKm = BigDecimal.ZERO;
            for(int j=i+1;j<trainStations.size();j++){
                TrainStation trainStationEnd = trainStations.get(j);
                sumKm = sumKm.add(trainStationEnd.getKm());

                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();

                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStationStart.getName());
                dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                dailyTrainTicket.setStartIndex(trainStationStart.getIndex());
                dailyTrainTicket.setEnd(trainStationEnd.getName());
                dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
                dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());
                int ydz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YDZ.getCode());
                int edz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.EDZ.getCode());
                int rw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.RW.getCode());
                int yw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YW.getCode());
                // 票价 = 里程之和 * 座位单价 * 车次类型系数
                String trainType = dailyTrain.getType();
                // 计算票价系数 TrainTypeEnum.priceRate
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);
                BigDecimal ydzPrice = sumKm.multiply(SeatTypeEnum.YDZ.getPrice().multiply(priceRate)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = sumKm.multiply(SeatTypeEnum.EDZ.getPrice().multiply(priceRate)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = sumKm.multiply(SeatTypeEnum.RW.getPrice().multiply(priceRate)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = sumKm.multiply(SeatTypeEnum.YW.getPrice().multiply(priceRate)).setScale(2, RoundingMode.HALF_UP);


                dailyTrainTicket.setYdz(ydz);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(edz);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(rw);
                dailyTrainTicket.setRwPrice(rwPrice);
                dailyTrainTicket.setYw(yw);
                dailyTrainTicket.setYwPrice(ywPrice);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketMapper.insert(dailyTrainTicket);

            }
        }

    }

}