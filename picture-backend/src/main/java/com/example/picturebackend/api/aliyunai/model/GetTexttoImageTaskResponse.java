package com.example.picturebackend.api.aliyunai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTexttoImageTaskResponse {
    /**
     * 请求唯一标识
     */
    private String requestId;

    /**
     * 输出信息
     */
    private Output output;

    private Useage useage;

    /**
     * 表示任务的输出信息
     */
    @Data
    public static class Output {

        /**
         * 任务 ID
         */
        private String taskId;

        /**
         * 任务状态
         * <ul>
         *     <li>PENDING：排队中</li>
         *     <li>RUNNING：处理中</li>
         *     <li>SUSPENDED：挂起</li>
         *     <li>SUCCEEDED：执行成功</li>
         *     <li>FAILED：执行失败</li>
         *     <li>UNKNOWN：任务不存在或状态未知</li>
         * </ul>
         */
        private String taskStatus;

        /**
         * 提交时间
         * 格式：YYYY-MM-DD HH:mm:ss.SSS
         */
        private String submitTime;

        /**
         * 调度时间
         * 格式：YYYY-MM-DD HH:mm:ss.SSS
         */
        private String scheduledTime;

        /**
         * 结束时间
         * 格式：YYYY-MM-DD HH:mm:ss.SSS
         */
        private String endTime;

        /**
         *  任务结果
         */
        private List<Result> results;

        /**
         * 接口错误码
         * <p>接口成功请求不会返回该参数</p>
         */
        private String code;

        /**
         * 接口错误信息
         * <p>接口成功请求不会返回该参数</p>
         */
        private String message;

    }

    /**
     * 表示任务的统计信息
     */
    @Data
    public static class Result {

        /**
         * 原始输入的prompt
         */
        private String orig_prompt;

        /**
         * 开启 prompt 智能改写后，返回实际使用的优化后 prompt。若未开启该功能，则不返回此字段。
         */
        private String actual_prompt;

        /**
         * 模型生成图像的URL地址
         */
        private String url;
    }

    @Data
    public static class Useage{
        /**
         * 模型生成图像的数量，当前固定为1。
         */
        private Integer image_count;
    }
}
