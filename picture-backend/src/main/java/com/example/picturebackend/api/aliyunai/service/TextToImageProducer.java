package com.example.picturebackend.api.aliyunai.service;

import com.example.picturebackend.api.aliyunai.config.RabbitMQConfig;
import com.example.picturebackend.api.aliyunai.model.CreateTexttoImageTaskRequest;
import org.springframework.stereotype.Service;

@Service
public class TextToImageProducer extends TaskProducer {

    public String submit(CreateTexttoImageTaskRequest request, int priority) {
        return submitTask(request, priority);
    }

    @Override
    protected String getTaskType() {
        return "TEXT_TO_IMAGE";
    }

    @Override
    protected String getRoutingKey() {
        return RabbitMQConfig.TEXT_TO_IMAGE_KEY;
    }

    @Override
    protected String getStatusKey(String localTaskId) {
        return "ai:text2image:status:" + localTaskId;
    }
}
