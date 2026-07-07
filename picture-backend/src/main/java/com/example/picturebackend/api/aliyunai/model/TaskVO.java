package com.example.picturebackend.api.aliyunai.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskVO {
    private String localTaskId;
    private String dashScopeTaskId;
    private String status;
    private int progress;
    private String result;
    private String error;
}
