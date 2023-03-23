package com.example.models.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.models.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select c.*,u.username,u.avatar " +
            "from sys_notice_comment c left join sys_user u on c.uid = u.id " +
            "where c.bid = #{noticeId}")
    List<Comment> findCommentDetail(@Param("noticeId") Integer noticeId);

}
