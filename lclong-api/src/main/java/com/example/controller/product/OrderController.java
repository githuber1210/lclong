package com.example.controller.product;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.framework.service.IProductService;
import com.example.framework.service.impl.OrderServiceImpl;
import com.example.models.entity.Order;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderServiceImpl orderService;
    @Resource
    private IProductService productService;

    @PostMapping
    public Result save(@RequestBody Order Order) {
        orderService.save(Order);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Order Order) {
        orderService.updateById(Order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        orderService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Order::getOrderNo, search);
        }
        Page<Order> OrderPage = orderService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(OrderPage);
    }

    @GetMapping("/pay")
    public Result pay(@RequestParam String subject,
                      @RequestParam String traceNo,
                      @RequestParam String totalAmount) {
        String payUrl = "http://localhost:9999/alipay/pay?subject=" + subject
                + "&traceNo=" + traceNo
                + "&totalAmount=" + totalAmount;
        return Result.success(payUrl);

    }
}
