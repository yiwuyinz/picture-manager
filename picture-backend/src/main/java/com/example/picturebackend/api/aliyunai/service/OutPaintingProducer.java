package com.example.picturebackend.api.aliyunai.service;

import com.example.picturebackend.api.aliyunai.config.RabbitMQConfig;
import com.example.picturebackend.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.example.picturebackend.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class OutPaintingProducer extends TaskProducer {

    public String submit(CreateOutPaintingTaskRequest request, int priority) {
        return submitTask(request, priority);
    }

    @Override
    protected String getTaskType() {
        return "OUT_PAINTING";
    }

    @Override
    protected String getRoutingKey() {
        return RabbitMQConfig.OUT_PAINTING_KEY;
    }

    @Override
    protected String getStatusKey(String localTaskId) {
        return "ai:outpainting:status:" + localTaskId;
    }
}
