package com.example.picturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureUpdateRequest implements Serializable {
    private static final long serialVersionUID = -3124377643925125722L;
    /**
     * id
     */
    private Long id;
    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（JSON 数组）
     */
    private String tags;
}
