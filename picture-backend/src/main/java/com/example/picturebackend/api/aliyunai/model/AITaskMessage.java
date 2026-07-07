package com.example.picturebackend.api.aliyunai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AITaskMessage implements Serializable {

    private String localTaskId;
    private String requestJson;
    private String taskType;

    @Builder.Default
    private int priority = 0;

    private long submitTime;

    @Builder.Default
    private int retryCount = 0;
}
