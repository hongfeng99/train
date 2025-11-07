package com.ikun.train.business.controller.admin;

import com.ikun.train.common.context.LoginMemberContext;
import com.ikun.train.common.resp.CommonResp;
import com.ikun.train.common.resp.PageResp;
import com.ikun.train.business.req.confirmOrderQueryReq;
import com.ikun.train.business.req.confirmOrderSaveReq;
import com.ikun.train.business.resp.confirmOrderQueryResp;
import com.ikun.train.business.service.confirmOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/confirm-order")
public class confirmOrderAdminController {

    @Resource
    private confirmOrderService confirmOrderService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody confirmOrderSaveReq req){
        confirmOrderService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<confirmOrderQueryResp>> queryList(@Valid confirmOrderQueryReq req){
        PageResp<confirmOrderQueryResp> list = confirmOrderService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        confirmOrderService.delete(id);
        return new CommonResp<>();
    }
}

