package com.example.common.log;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.models.entity.Log;

public interface ILogService extends IService<Log> {

    void log(String content,String username);
}
