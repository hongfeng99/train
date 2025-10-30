package com.ikun.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ikun.train.common.context.LoginMemberContext;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.common.util.SnowUtil;
import com.ikun.train.member.domain.${Domain};
import com.ikun.train.member.domain.${Domain}Example;
import com.ikun.train.member.mapper.${Domain}Mapper;
import com.ikun.train.member.req.${Domain}QueryReq;
import com.ikun.train.member.req.${Domain}SaveReq;
import com.ikun.train.member.resp.${Domain}QueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ${Domain}Service {
private static final Logger LOG = LoggerFactory.getLogger(${Domain}Service.class);

@Resource
private ${Domain}Mapper ${domain}Mapper;

public void save(${Domain}SaveReq req){
DateTime now = DateTime.now();
${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);

if(ObjectUtil.isNull(req.getId())){
${domain}.setMemberId(LoginMemberContext.getId());
${domain}.setId(SnowUtil.getSnowflakeNextId());
${domain}.setCreateTime(now);
${domain}.setUpdateTime(now);
${domain}Mapper.insert(${domain});
}else{
${domain}.setUpdateTime(now);
${domain}Mapper.updateByPrimaryKey(${domain});
}
}

public PageResp<${Domain}QueryResp> queryList(${Domain}QueryReq req){
    ${Domain}Example ${domain}Example = new ${Domain}Example();
    ${domain}Example.setOrderByClause("id desc");
    ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();
    if(ObjectUtil.isNotEmpty(req.getMemberId())){
    criteria.andMemberIdEqualTo(req.getMemberId());
    }

    LOG.info("查询页码：{}", req.getPage());
    LOG.info("每页条数：{}", req.getSize());
    PageHelper.startPage(req.getPage(),req.getSize());
    List<${Domain}> list = ${domain}Mapper.selectByExample(${domain}Example);

    PageInfo<${Domain}> pageInfo = new PageInfo<>(list);
    LOG.info("总行数：{}", pageInfo.getTotal());
    LOG.info("总页数：{}", pageInfo.getPages());

    List<${Domain}QueryResp> list1 = BeanUtil.copyToList(list, ${Domain}QueryResp.class);
        PageResp<${Domain}QueryResp> pageResp = new PageResp<>();
            pageResp.setList(list1);
            pageResp.setTotal(pageInfo.getTotal());

            return pageResp;
            }

            public void delete(Long id){
            ${domain}Mapper.deleteByPrimaryKey(id);
            }
            }
