package com.example.framework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.service.IFileService;
import com.example.models.entity.Files;
import com.example.models.mapper.FileMapper;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {
}
