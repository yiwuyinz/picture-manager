package com.example.picturebackend.model.dto.space;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceEditRequest implements Serializable {

    private static final long serialVersionUID = -6414026251904594136L;
    /**
     * id
     */
    private Long id;

    /**
     * 空间名称
     */
    private String spaceName;
}
