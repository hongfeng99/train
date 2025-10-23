package com.ikun.train.member.controller;

import com.ikun.train.common.resp.CommonResp;
import com.ikun.train.member.req.MemberLoginReq;
import com.ikun.train.member.req.MemberRegisterReq;
import com.ikun.train.member.req.MemberSendCodeReq;
import com.ikun.train.member.resp.MemberLoginResp;
import com.ikun.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> count(){
        int count =  memberService.count();
        CommonResp<Integer> commonResp = new CommonResp<>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid MemberRegisterReq req){
        long id = memberService.register(req);
//        CommonResp<Long> commonResp = new CommonResp<>();
//        commonResp.setContent(id);
//        return commonResp;
        return new CommonResp<>(id);
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid @RequestBody MemberSendCodeReq req){
        memberService.sendCode(req);
        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid MemberLoginReq req){
        MemberLoginResp resp = memberService.login(req);
        return new CommonResp<>(resp);
    }
}

