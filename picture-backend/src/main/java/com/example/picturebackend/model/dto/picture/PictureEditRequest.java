package com.example.picturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureEditRequest implements Serializable {
    private static final long serialVersionUID = -1721587064397218099L;
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
    private List<String> tags;
    /**
     * 空间 id
     */
    private Long spaceId;
}
