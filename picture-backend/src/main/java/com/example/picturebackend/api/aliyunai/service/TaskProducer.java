package com.example.picturebackend.api.aliyunai.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.example.picturebackend.api.aliyunai.config.RabbitMQConfig;
import com.example.picturebackend.api.aliyunai.model.AITaskMessage;
import com.example.picturebackend.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.example.picturebackend.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.Map;

@Service
@Slf4j
public abstract class TaskProducer {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    public String submitTask(Object request, int priority) {
        String localTaskId = getTaskType() + "_" + System.currentTimeMillis() + "_"
                + RandomUtil.randomNumbers(6);

        AITaskMessage message = AITaskMessage.builder()
                .localTaskId(localTaskId)
                .requestJson(JSONUtil.toJsonStr(request))
                .taskType(getTaskType())
                .priority(Math.max(0, Math.min(priority, 10)))
                .submitTime(System.currentTimeMillis())
                .retryCount(0)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.AI_EXCHANGE,
                getRoutingKey(),
                message,
                msg -> {
                    msg.getMessageProperties().setPriority(message.getPriority());
                    return msg;
                }
        );

        String statusKey = getStatusKey(localTaskId);
        Map<String, Object> status = new HashMap<>();
        status.put("status", "QUEUED");
        status.put("progress", "0");
        status.put("createTime", String.valueOf(System.currentTimeMillis()));
        redisTemplate.opsForHash().putAll(statusKey, status);
        redisTemplate.expire(statusKey, 1, TimeUnit.HOURS);

        log.info("[{}] 任务[{}] 已入队，优先级: {}", getTaskType(), localTaskId, priority);
        return localTaskId;
    }

    protected abstract String getTaskType();

    protected abstract String getRoutingKey();

    protected abstract String getStatusKey(String localTaskId);
}
