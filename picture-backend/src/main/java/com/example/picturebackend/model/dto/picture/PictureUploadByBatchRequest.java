package com.example.picturebackend.model.dto.picture;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class PictureUploadByBatchRequest {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 抓取数量
     */
    private Integer count = 10;
    /**
     * 名称前缀
     */
    private String namePrefix;
}
