package com.example.picturebackend.model.dto.picture;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class PictureUploadRequest implements Serializable {
    private static final long serialVersionUID = -700869244509491932L;
    /**
     * id
     */
    private Long id;
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 图片名称
     */
    private String name;
    /**
     * 空间 id
     */
    private Long spaceId;

}
