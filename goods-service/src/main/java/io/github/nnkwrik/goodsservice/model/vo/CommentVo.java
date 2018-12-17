package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.model.po.GoodsComment;
import lombok.Data;

import java.util.List;

/**
 * 商品评论
 *
 * @author nnkwrik
 * @date 18/11/23 16:09
 */
@Data
public class CommentVo extends GoodsComment {

    /*评论用户信息*/
    private SimpleUser simpleUser;

    /*回复的用户昵称*/
    private String replyUserName;

    /*回复该评论的评论列表*/
    private List<CommentVo> replyList;
}
