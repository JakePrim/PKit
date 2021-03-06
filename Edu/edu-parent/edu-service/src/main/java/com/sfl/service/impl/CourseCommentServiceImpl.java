package com.sfl.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sfl.mapper.CourseCommentDao;
import com.sfl.pojo.CourseComment;
import com.sfl.pojo.CourseCommentFavoriteRecord;
import com.sfl.pojo.bo.PageBo;
import com.sfl.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: Edu
 * @Description: 服务实现
 * @author: sufulu
 * @version: 1.0.0
 * @create: 2021-02-07 15:16
 * @PackageName: com.sfl.service.impl
 * @ClassName: CourseCommentServiceImpl.java
 **/
@Service(interfaceClass = CourseCommentService.class)
public class CourseCommentServiceImpl implements CourseCommentService {

    @Autowired
    private CourseCommentDao courseCommentDao;

    @Override
    public Integer saveComment(CourseComment courseComment) {
        Integer row = courseCommentDao.saveComment(courseComment);
        return row;
    }

    @Override
    public PageInfo<CourseComment> findCommentByCourseId(PageBo pageBo) {
        if (pageBo != null) {
            PageHelper.startPage(pageBo.getPage(), pageBo.getPageSize());
            List<CourseComment> comments = courseCommentDao.findCommentByCourseId(pageBo.getCourseId());
            PageInfo<CourseComment> pageInfo = new PageInfo<>(comments);
            return pageInfo;
        }
        return null;
    }

    /**
     * 0 表示失败
     * 1 表示成功
     *
     * @param record
     * @return
     */
    @Transactional
    @Override
    public Integer favoriteComment(CourseCommentFavoriteRecord record) {
        if (record != null) {
            Integer row = courseCommentDao.existsFavoriteByUser(record.getCommentId(), record.getUserId());
            if (row == 0) {//该用户没有对这条评论点赞 保存点赞信息
                //保存点赞信息
                Integer favorite = courseCommentDao.saveFavorite(record);
                //更新评论表 增加点赞数量
                courseCommentDao.updateAddLikeCount(record.getCommentId());
                return favorite;
            } else {// 用户已经对这条评论点过赞了、或者没有点过赞 点击取消赞或点赞 更改状态
                Integer isDel = courseCommentDao.findIsDel(record.getCommentId(), record.getUserId());
                if (isDel == 0) {
                    isDel = 1;
                } else {
                    isDel = 0;
                }
                Integer update = 0;
                update = courseCommentDao.updateFavorite(isDel, record.getCommentId(), record.getUserId());
                if (isDel == 1) {
                    //更新评论表 减少点赞数量
                    courseCommentDao.updateSubLikeCount(record.getCommentId());
                } else {
                    //更新评论表 增加点赞的数量
                    courseCommentDao.updateAddLikeCount(record.getCommentId());
                }

                return update;
            }
        }
        return null;
    }
}
