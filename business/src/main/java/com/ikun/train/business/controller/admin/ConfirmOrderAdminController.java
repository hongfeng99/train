package com.ikun.train.business.controller.admin;

import com.ikun.train.common.resp.CommonResp;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.business.req.ConfirmOrderQueryReq;
import com.ikun.train.business.req.ConfirmOrderDoReq;
import com.ikun.train.business.resp.confirmOrderQueryResp;
import com.ikun.train.business.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/confirm-order")
public class ConfirmOrderAdminController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody ConfirmOrderDoReq req){
        confirmOrderService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<confirmOrderQueryResp>> queryList(@Valid ConfirmOrderQueryReq req){
        PageResp<confirmOrderQueryResp> list = confirmOrderService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        confirmOrderService.delete(id);
        return new CommonResp<>();
    }
}

