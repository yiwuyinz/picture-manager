package com.example.picturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.picturebackend.common.DeleteRequest;
import com.example.picturebackend.model.dto.space.SpaceAddRequest;
import com.example.picturebackend.model.dto.space.SpaceQueryRequest;
import com.example.picturebackend.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.picturebackend.model.entity.User;
import com.example.picturebackend.model.vo.PictureVO;
import com.example.picturebackend.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Dell
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2026-06-19 15:51:46
*/
public interface SpaceService extends IService<Space> {
    /**
     * 校验空间
     * @param space
     * @param add
     */
    void validSpace(Space space,boolean add);

    /**
     * 自动填充限额数据
     * @param space
     */
    void fillSpaceBySpaceLevel(Space space);

    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    SpaceVO getSpaceVO(Space space,HttpServletRequest request);

    void deleteSpace(DeleteRequest deleteRequest);
}
