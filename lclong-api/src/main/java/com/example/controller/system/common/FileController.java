package com.example.controller.system.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.common.util.oss.AliOssUtil;
import com.example.models.entity.Files;
import com.example.framework.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "文件模块")
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${server.port}")
    private String port;

    @Value("${file.ip}")
    private String serverIp;

    @Resource
    private IFileService fileService;

    @ApiOperation("根据文件ID批量删除文件")
    @PostMapping("/delete/batch")
    public Result removeByIds(@RequestBody  List<Integer> ids){
        fileService.removeBatchByIds(ids);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public Result list(@RequestParam Integer pageNum,
                       @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String keyword) {
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
        filesQueryWrapper.like("name", keyword);
        Page<Files> page = fileService.page(new Page<>(pageNum, pageSize), filesQueryWrapper);
        return Result.success(page);
    }

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file){


        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        File uploadFile = new File(System.getProperty("user.dir") + "/files/" + fileUUID);
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String url;

        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        url = "http://" + serverIp + ":" + port + "/file/" + fileUUID;
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/(1024));
        saveFile.setUrl(url);
        saveFile.setTime(DateUtil.now());


        fileService.save(saveFile);
        return url;
    }

    @ApiOperation("下载文件")
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {

        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(System.getProperty("user.dir") + "/files/" + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    @ApiOperation("下载文件")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) {
        return Result.success(fileService.getById(id));
    }

    @ApiOperation("查看所有文件")
    @GetMapping("/listAll")
    public Result list() {
        return Result.success(fileService.list());
    }


    @ApiOperation("oss上传文件")
    @PostMapping("/upload/oss")
    public Result ossUpload(MultipartFile file) {
        return Result.success(AliOssUtil.upload("test/", file));
    }

}
