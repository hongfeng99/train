package com.ikun.train.member.controller;

import com.ikun.train.common.context.LoginMemberContext;
import com.ikun.train.common.resp.CommonResp;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.member.req.TicketQueryReq;
import com.ikun.train.member.resp.TicketQueryResp;
import com.ikun.train.member.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> query(@Valid TicketQueryReq req) {
        CommonResp<PageResp<TicketQueryResp>> commonResp = new CommonResp<>();
        req.setMemberId(LoginMemberContext.getId());
        PageResp<TicketQueryResp> pageResp = ticketService.queryList(req);
        commonResp.setContent(pageResp);
        return commonResp;
    }
}