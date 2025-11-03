package com.ikun.train.business.controller.admin;

import com.ikun.train.common.context.LoginMemberContext;
import com.ikun.train.common.resp.CommonResp;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.business.req.DailyTrainTicketQueryReq;
import com.ikun.train.business.req.DailyTrainTicketSaveReq;
import com.ikun.train.business.resp.DailyTrainTicketQueryResp;
import com.ikun.train.business.service.DailyTrainTicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyTrainTicketAdminController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainTicketSaveReq req){
        dailyTrainTicketService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req){
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        dailyTrainTicketService.delete(id);
        return new CommonResp<>();
    }
}

