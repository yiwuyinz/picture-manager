package com.example.picturebackend.api.aliyunai.service;


import cn.hutool.json.JSONUtil;
import com.example.picturebackend.api.aliyunai.model.AITaskMessage;
import com.example.picturebackend.api.aliyunai.model.GetTexttoImageTaskResponse;
import com.example.picturebackend.exception.BusinessException;
import com.example.picturebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public abstract class TaskConsumer {
    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Autowired
    @Qualifier("pollingExecutor")
    protected ThreadPoolExecutor pollingExecutor;

    public void handle(AITaskMessage message) {
        String localTaskId = message.getLocalTaskId();
        String statusKey = getStatusKey(localTaskId);

        try {
            Object request = parseRequest(message.getRequestJson());

            redisTemplate.opsForHash().put(statusKey, "status", "SUBMITTING");

            log.info("[{}] 任务[{}] 正在创建...", getTaskType(), localTaskId);
            String dashScopeTaskId = createTask(request);

            Map<String, Object> map = new HashMap<>();
            map.put("status", "SUBMITTED");
            map.put("dashScopeTaskId", dashScopeTaskId);
            map.put("progress", "20");
            redisTemplate.opsForHash().putAll(statusKey, map);

            log.info("[{}] 任务[{}] 创建成功，交给线程池轮询", getTaskType(), localTaskId);

            pollingExecutor.submit(() ->
                    pollUntilDone(localTaskId, dashScopeTaskId, statusKey)
            );

        } catch (Exception e) {
            log.error("[{}] 任务[{}] 创建失败", getTaskType(), localTaskId, e);
            Map<String, Object> failMap = new HashMap<>();
            failMap.put("status", "FAILED");
            failMap.put("error", e.getMessage());
            redisTemplate.opsForHash().putAll(statusKey, failMap);
        }
    }

    private void pollUntilDone(String localTaskId, String dashScopeTaskId, String statusKey) {
        try {
            String result = doPoll(dashScopeTaskId, statusKey);

            Map<String, Object> successMap = new HashMap<>();
            successMap.put("status", "SUCCEEDED");
            successMap.put("progress", "100");
            successMap.put("result", result);
            successMap.put("finishTime", String.valueOf(System.currentTimeMillis()));
            redisTemplate.opsForHash().putAll(statusKey, successMap);

            log.info("[{}] 任务[{}] 轮询完成", getTaskType(), localTaskId);

        } catch (Exception e) {
            log.error("[{}] 任务[{}] 轮询失败", getTaskType(), localTaskId, e);
            Map<String, Object> failMap = new HashMap<>();
            failMap.put("status", "FAILED");
            failMap.put("error", e.getMessage());
            redisTemplate.opsForHash().putAll(statusKey, failMap);
        }
    }

    private String doPoll(String dashScopeTaskId, String statusKey)
            throws InterruptedException {
        for (int i = 0; i < 60; i++) {
            Thread.sleep(5000);

            String taskStatus = queryTaskStatus(dashScopeTaskId);
            log.info("DashScope任务[{}] 状态: {}", dashScopeTaskId, taskStatus);

            if ("SUCCEEDED".equals(taskStatus)) {
                return getTaskResult(dashScopeTaskId);
            }
            if ("FAILED".equals(taskStatus)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "DashScope任务执行失败");
            }

            redisTemplate.opsForHash().put(statusKey, "status", "RUNNING");
            redisTemplate.opsForHash().put(statusKey, "progress", "60");
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "DashScope任务轮询超时");
    }

    protected abstract String getTaskType();

    protected abstract String getStatusKey(String localTaskId);

    protected abstract Object parseRequest(String json);

    protected abstract String createTask(Object request);

    protected abstract String queryTaskStatus(String dashScopeTaskId);

    protected abstract String getTaskResult(String dashScopeTaskId);
}
