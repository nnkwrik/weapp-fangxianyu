package io.github.nnkwrik.goodsservice.model.vo.inner;

import io.github.nnkwrik.common.dto.SimpleUser;
import lombok.Data;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/23 16:09
 */
@Data
public class CommentVo {
    private Integer id;
    private Integer goodsId;
    private String userId;
    private Integer replyCommentId;
    private String replyUserId;
    private String content;
    private String createTime;

    private SimpleUser simpleUser;
    private String replyUserName;
    private List<CommentVo> replyList;
}
