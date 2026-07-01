package com.example.picturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.picturebackend.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.example.picturebackend.api.aliyunai.model.ImageSyncRequest;
import com.example.picturebackend.api.aliyunai.model.ImageSyncResponse;
import com.example.picturebackend.model.dto.file.UploadPictureResult;
import com.example.picturebackend.model.dto.picture.*;
import com.example.picturebackend.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.picturebackend.model.entity.User;
import com.example.picturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

/**
 * @author Dell
 * @description 针对表【my_picture(图片)】的数据库操作Service
 * @createDate 2026-06-11 17:25:47
 */
public interface PictureService extends IService<Picture> {
    /**
     * 上传图片
     * @param inpitSource
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inpitSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 分页查询
     * @param pictureQueryRequest
     * @return
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取图片封装，可以为原有图片关联用户信息
     * @param picture
     * @param request
     * @return
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 分页获取图片封装
     * @param picturePage
     * @param request
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 图片数据校验方法，用于更新和修改时校验
     * @param picture
     */
    void validPicture(Picture picture);

    /**
     * 图片审核
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest,User loginUser);

    /**
     * 补充审核参数
     */
    void fillReviewParams(Picture picture,User loginUser);

    /**
     * 批量抓取图片
     * @param pictureUploadByBatchRequest
     * @param loginUser
     * @return
     */
    Integer uploadPictureByBatch(PictureUploadByBatchRequest pictureUploadByBatchRequest, User loginUser);

    /**
     * 图片清理
     * @param oldPicture
     */
    void clearPictureFile(Picture oldPicture);

    /**
     * 校验逻辑
     * @param loginUser
     * @param picture
     */
    void checkPictureAuth(User loginUser,Picture picture);

    /**
     * 删除图片以及文件
     * @param pictureId
     * @param loginUser
     */
    void deletePicture(long pictureId,User loginUser);

    /**
     * 编辑图片
     * @param pictureEditRequest
     * @param loginUser
     */
    void editPicture(PictureEditRequest pictureEditRequest,User loginUser);

    CreateOutPaintingTaskResponse createPictureOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest,User loginUser);

    ImageSyncResponse createImageSyncTask(ImageSyncRequest imageSyncRequest);
}
