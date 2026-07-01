package com.example.picturebackend.api.aliyunai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ImageSyncRequest {

    private String model = "qwen-image-2.0-pro";

    /**
     * 输入的基本信息（必选）
     */
    private Input input;

    /**
     * 图像处理参数（可选）
     */
    private Parameters parameters;

    /**
     * 输入信息
     */
    @Data
    public static class Input {

        /**
         * 请求内容数组（必选）
         * 当前仅支持单轮对话，数组内有且只有一个元素
         */
        private List<Message> messages;
    }

    /**
     * 消息对象
     */
    @Data
    public static class Message {

        /**
         * 消息的角色（必选）
         * 必须设置为 user
         */
        private String role = "user";

        /**
         * 消息内容数组（必选）
         */
        private List<Content> content;
    }

    /**
     * 内容对象
     */
    @Data
    public static class Content {

        /**
         * 正向提示词（必选）
         * 用于描述期望生成的图像内容、风格和构图
         * 支持中英文，qwen-image-2.0系列模型长度上限为1300 Token，其他模型为800 Token
         * 注意: 仅支持传入一个text，不传或传入多个将报错
         */
        private String text;
    }

    /**
     * 图像处理参数
     */
    @Data
    public static class Parameters {

        /**
         * 反向提示词（可选）
         * 用于描述不希望在图像中出现的内容
         * 支持中英文，长度不超过500个字符
         */
        private String negativePrompt;

        /**
         * 输出图像的分辨率（可选）
         * 格式: 宽*高
         * qwen-image-2.0系列: 总像素需在512*512至2048*2048之间，默认2048*2048
         * 推荐: 2688*1536(16:9), 1536*2688(9:16), 2048*2048(1:1, 默认), 2368*1728(4:3), 1728*2368(3:4)
         * qwen-image-max/plus系列: 默认1664*928
         * 可选: 1664*928(16:9, 默认), 1472*1104(4:3), 1328*1328(1:1), 1104*1472(3:4), 928*1664(9:16)
         */
        private String size;

        /**
         * 输出图像的数量（可选）
         * 默认值为1
         * qwen-image-2.0系列: 1-6张
         * qwen-image-max/plus系列: 固定为1
         */
        private Integer n;

        /**
         * 是否开启Prompt智能改写（可选）
         * true: 开启（默认），模型将对正向提示词进行优化与润色
         * false: 关闭，图像细节更可控
         */
        private Boolean promptExtend;

        /**
         * 是否在图像右下角添加水印（可选）
         * 默认值为false
         */
        private Boolean watermark;

        /**
         * 随机数种子（可选）
         * 取值范围 [0, 2147483647]
         * 使用相同的seed可使生成内容保持相对稳定
         */
        private Integer seed;
    }
}
