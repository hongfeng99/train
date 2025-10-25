package com.ikun.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ikun.train.common.exception.BusinessException;
import com.ikun.train.common.exception.BusinessExceptionEnum;
import com.ikun.train.common.util.JwtUtil;
import com.ikun.train.common.util.SnowUtil;
import com.ikun.train.member.domain.Member;
import com.ikun.train.member.domain.MemberExample;
import com.ikun.train.member.mapper.MemberMapper;
import com.ikun.train.member.req.MemberLoginReq;
import com.ikun.train.member.req.MemberRegisterReq;
import com.ikun.train.member.req.MemberSendCodeReq;
import com.ikun.train.member.resp.MemberLoginResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
    
    @Resource
    private MemberMapper memberMapper;

    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq req){
        String mobile = req.getMobile();
        Member memberDB = selectByMobile(mobile);
        if(ObjectUtil.isNotNull(memberDB)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq req){
        String mobile = req.getMobile();
        Member memberDB = selectByMobile(mobile);

        // 如果手机号不存在，则插入一条记录
        if(ObjectUtil.isNull(memberDB)){
            LOG.info("手机号不存在，插入一条记录");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        }else{
            LOG.info("手机号存在，不插入记录");
        }

        // 生成验证码
//        String code = RandomUtil.randomString(4);
        String code = "9999";
        LOG.info("生成短信验证码：{}",code);

        // 保存短信记录表：手机号，验证码，有效期，是否已使用，业务类型，发送时间，使用时间
        LOG.info("生成短信记录表");

        // 对接短信通道，发送短信
        LOG.info("对接短信通道");

    }
        public MemberLoginResp login(MemberLoginReq req){
        String mobile = req.getMobile();
        String code = req.getCode();
        Member memberDB= selectByMobile(mobile);

        // 如果手机号不存在，则插入一条记录
        if(ObjectUtil.isNull(memberDB)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        // 校验短信验证码
            if(!"9999".equals(code)){
                throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
            }

            MemberLoginResp memberLoginResp = BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
            Map<String, Object> map = BeanUtil.beanToMap(memberLoginResp);
            String key = "ikun12306";
            String token = JwtUtil.createToken(memberLoginResp.getId(), memberLoginResp.getMobile());
            memberLoginResp.setToken(token);
            return memberLoginResp;
    }

    private Member selectByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> members = memberMapper.selectByExample(memberExample);
        // 如果手机号不存在，则插入一条记录
        if(CollUtil.isEmpty(members)){
            return null;
        }else{
            return members.get(0);
        }
    }
}
