package com.example.picturebackend.api.aliyunai.service;

import cn.hutool.json.JSONUtil;
import com.example.picturebackend.api.aliyunai.config.RabbitMQConfig;
import com.example.picturebackend.api.aliyunai.model.AITaskMessage;
import com.example.picturebackend.api.aliyunai.model.CreateTexttoImageTaskRequest;
import com.example.picturebackend.api.aliyunai.model.CreateTexttoImageTaskResponse;
import com.example.picturebackend.api.aliyunai.model.GetTexttoImageTaskResponse;
import com.example.picturebackend.exception.BusinessException;
import com.example.picturebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TextToImageConsumer extends TaskConsumer {

    @Autowired
    private AliYunAiApi aliYunAiApi;

    @RabbitListener(queues = RabbitMQConfig.TEXT_TO_IMAGE_QUEUE)
    public void consume(AITaskMessage message) {
        handle(message);
    }

    @Override
    protected String getTaskType() {
        return "TEXT_TO_IMAGE";
    }

    @Override
    protected String getStatusKey(String localTaskId) {
        return "ai:text2image:status:" + localTaskId;
    }

    @Override
    protected Object parseRequest(String json) {
        return JSONUtil.toBean(json, CreateTexttoImageTaskRequest.class);
    }

    @Override
    protected String createTask(Object request) {
        CreateTexttoImageTaskResponse resp =
                aliYunAiApi.createTexttoImageTask((CreateTexttoImageTaskRequest) request);
        return resp.getOutput().getTaskId();
    }

    @Override
    protected String queryTaskStatus(String dashScopeTaskId) {
        GetTexttoImageTaskResponse resp =
                aliYunAiApi.getTexttoImageTask(dashScopeTaskId);
        // 加日志：打印完整响应
        log.info("DashScope响应: {}", JSONUtil.toJsonStr(resp));
        return resp.getOutput().getTaskStatus();
    }

    @Override
    protected String getTaskResult(String dashScopeTaskId) {
        return aliYunAiApi.getTexttoImageTask(dashScopeTaskId)
                .getOutput().getResults().get(0).getUrl();
    }
}
