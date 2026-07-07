package com.example.picturebackend.api.aliyunai.service;

import com.example.picturebackend.api.aliyunai.model.TaskVO;
import org.springframework.stereotype.Service;

@Service
public class TextToImageQueryService extends TaskQueryService {

    public TaskVO query(String localTaskId) {
        return getStatus(localTaskId);
    }

    @Override
    protected String getStatusKey(String localTaskId) {
        return "ai:text2image:status:" + localTaskId;
    }
}
