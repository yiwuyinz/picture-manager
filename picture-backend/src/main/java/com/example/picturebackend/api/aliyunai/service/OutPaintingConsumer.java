package com.example.picturebackend.api.aliyunai.service;

import cn.hutool.json.JSONUtil;
import com.example.picturebackend.api.aliyunai.config.RabbitMQConfig;
import com.example.picturebackend.api.aliyunai.model.AITaskMessage;
import com.example.picturebackend.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.example.picturebackend.api.aliyunai.model.CreateOutPaintingTaskResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutPaintingConsumer extends TaskConsumer {

    @Autowired
    private AliYunAiApi aliYunAiApi;

    @RabbitListener(queues = RabbitMQConfig.OUT_PAINTING_QUEUE)
    public void consume(AITaskMessage message) {
        handle(message);
    }

    @Override
    protected String getTaskType() {
        return "OUT_PAINTING";
    }

    @Override
    protected String getStatusKey(String localTaskId) {
        return "ai:outpainting:status:" + localTaskId;
    }

    @Override
    protected Object parseRequest(String json) {
        return JSONUtil.toBean(json, CreateOutPaintingTaskRequest.class);
    }

    @Override
    protected String createTask(Object request) {
        CreateOutPaintingTaskResponse resp =
                aliYunAiApi.createOutPaintingTask((CreateOutPaintingTaskRequest) request);
        return resp.getOutput().getTaskId();
    }

    @Override
    protected String queryTaskStatus(String dashScopeTaskId) {
        return aliYunAiApi.getOutPaintingTask(dashScopeTaskId)
                .getOutput().getTaskStatus();
    }

    @Override
    protected String getTaskResult(String dashScopeTaskId) {
        return aliYunAiApi.getOutPaintingTask(dashScopeTaskId)
                .getOutput().getOutputImageUrl();
    }
}
