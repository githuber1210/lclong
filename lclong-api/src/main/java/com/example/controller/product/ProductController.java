package com.example.controller.product;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.framework.service.IOrderService;
import com.example.framework.service.IProductService;
import com.example.framework.service.IUserService;
import com.example.models.entity.Order;
import com.example.models.entity.Product;
import com.example.models.entity.User;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "周边模块")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private IProductService productService;

    @Resource
    private IUserService userService;

    @Resource
    private IOrderService orderService;

    @PostMapping("/save")
    public Result save(@RequestBody Product product) {
        productService.saveOrUpdate(product);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        productService.removeById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String keyword) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Product::getTitle, keyword);
        Page<Product> data = productService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(data);
    }

    @GetMapping("/listAll")
    public Result listAll() {
        List<Product> productList = productService.list();
        return Result.success(productList);
    }

    @GetMapping("/buy/{id}")
    public Result buy(@PathVariable Long id) {
        Product product = productService.getById(id);
        String orderNo = IdUtil.getSnowflake().nextIdStr();
        String payUrl = "http://localhost:9999/alipay/pay?subject=" + product.getTitle()
                + "&traceNo=" + orderNo
                + "&totalAmount=" + product.getId();

        User user = userService.getCurrentLoginUser();

        Order order = new Order();
        order.setProductId(product.getId());
        order.setOrderNo(orderNo);
        order.setPayPrice(product.getPrice());
        order.setUsername(user.getUsername());
        order.setName(product.getTitle());
        order.setCreateTime(DateUtil.now());

        orderService.save(order);
        orderService.sendDelayMessageCancelOrder(orderNo);


        //todo 新建订单，扣减库存
        return Result.success(payUrl);
    }

}
