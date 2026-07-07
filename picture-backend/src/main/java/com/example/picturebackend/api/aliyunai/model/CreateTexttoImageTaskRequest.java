package com.example.picturebackend.api.aliyunai.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateTexttoImageTaskRequest implements Serializable{
    /**
     * 模型，例如 "image-out-painting"
     */
    private String model = "qwen-image-plus";

    /**
     * 输入图像信息
     */
    private Input input;

    /**
     * 图像处理参数
     */
    private Parameters parameters;

    @Data
    public static class Input {
        /**
         * 必选，正向提示词
         */
        private String prompt;
    }

    @Data
    public static class Parameters implements Serializable {
        /**
         * 可选，输出图像的分辨率，格式为宽*高
         */
        private String size;

        /**
         * 可选，生成图像的数量。此参数当前固定为1，设置其他值将导致报错。
         */
        private Integer n;

        /**
         * 可选，是否开启 Prompt（提示词）智能改写功能。开启后模型将对正向提示词进行优化与润色。此功能不会修改反向提示词
         */
        private boolean prompt_extend;

        /**
         * 可选，是否在图像右下角添加 "Qwen-Image" 水印。默认值为 false
         */
        private boolean watermark;

        /**
         * 可选，随机数种子，取值范围[0,2147483647]
         */
        private Integer seed;
    }
}
