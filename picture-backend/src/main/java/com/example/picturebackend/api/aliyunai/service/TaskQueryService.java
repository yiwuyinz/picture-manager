package com.example.picturebackend.api.aliyunai.service;

import com.example.picturebackend.api.aliyunai.model.TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public abstract class TaskQueryService {
    @Autowired
    protected StringRedisTemplate redisTemplate;

    public TaskVO getStatus(String localTaskId) {
        String statusKey = getStatusKey(localTaskId);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(statusKey);

        if (entries.isEmpty()) {
            return null;
        }

        return TaskVO.builder()
                .localTaskId(localTaskId)
                .dashScopeTaskId((String) entries.get("dashScopeTaskId"))
                .status((String) entries.get("status"))
                .progress(Integer.parseInt((String) entries.getOrDefault("progress", "0")))
                .result((String) entries.get("result"))
                .error((String) entries.get("error"))
                .build();
    }

    protected abstract String getStatusKey(String localTaskId);
}
