package com.example.picturebackend.api.aliyunai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ImageSyncResponse {

    private Output output;
    /**
     * 任务输出信息
     */
    @Data
    public static class Output {

        /**
         * 模型生成的输出内容
         * 此数组仅包含一个元素
         */
        private List<Choice> choices;
    }

    /**
     * 选择项（模型生成的输出内容）
     */
    @Data
    public static class Choice {

        /**
         * 任务停止原因
         * 自然停止时为 stop
         */
        private String finishReason;

        /**
         * 模型返回的消息
         */
        private Message message;

        /**
         * 任务结果统计（使用qwen-image-2.0系列时无此返回值）
         */
        private TaskMetric taskMetric;
    }

    /**
     * 消息对象
     */
    @Data
    public static class Message {

        /**
         * 消息的角色，固定为 assistant
         */
        private String role;

        /**
         * 消息内容数组
         */
        private List<Content> content;
    }

    /**
     * 内容对象
     */
    @Data
    public static class Content {

        /**
         * 生成图像的URL
         * 图像格式为PNG
         * 链接有效期为24小时，请及时下载并保存图像
         */
        private String image;
    }

    /**
     * 任务结果统计
     * 使用qwen-image-2.0系列时无此返回值
     */
    @Data
    public static class TaskMetric {

        /**
         * 总的任务数
         */
        private Integer total;

        /**
         * 任务状态为成功的任务数
         */
        private Integer succeeded;

        /**
         * 任务状态为失败的任务数
         */
        private Integer failed;
    }

    /**
     * 输出信息统计
     */
    @Data
    public static class Usage {

        /**
         * 模型生成图像的数量
         * 当前固定为1
         */
        private Integer imageCount;

        /**
         * 模型生成图像的宽度（像素）
         */
        private Integer width;

        /**
         * 模型生成图像的高度（像素）
         */
        private Integer height;
    }

    /**
     * 接口错误码。
     * <p>接口成功请求不会返回该参数。</p>
     */
    private String code;

    /**
     * 接口错误信息。
     * <p>接口成功请求不会返回该参数。</p>
     */
    private String message;

    /**
     * 请求唯一标识。
     * <p>可用于请求明细溯源和问题排查。</p>
     */
    private String requestId;
}
