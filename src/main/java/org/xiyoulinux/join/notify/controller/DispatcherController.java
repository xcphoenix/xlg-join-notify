package org.xiyoulinux.join.notify.controller;

import org.springframework.web.bind.annotation.*;
import org.xiyoulinux.join.notify.model.dto.result.Result;
import org.xiyoulinux.join.notify.service.DispatchService;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/15 上午11:59
 */
@RestController
@RequestMapping("/dispatch")
public class DispatcherController {

    private final DispatchService dispatchService;

    public DispatcherController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @PostMapping("/preview")
    public Result<Boolean> preview(@RequestBody(required = false) List<Long> senderList) {
        return dispatchService.preview(senderList);
    }

    @PostMapping("/action")
    public Result<Boolean> doDispatch() {
        return dispatchService.dispatch();
    }

    @DeleteMapping("/reset")
    public Result<Boolean> resetDispatch() {
        return dispatchService.resetDispatch();
    }

}
