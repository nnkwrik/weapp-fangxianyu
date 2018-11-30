package io.github.nnkwrik.goodsservice.model.vo;

import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.goodsservice.model.po.GoodsComment;
import lombok.Data;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/23 16:09
 */
@Data
public class CommentVo extends GoodsComment {
    private SimpleUser simpleUser;
    private String replyUserName;
    private List<CommentVo> replyList;
}
