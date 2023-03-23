package com.example.controller.product;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.models.entity.Product;
import com.example.framework.service.IProductService;
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


}
