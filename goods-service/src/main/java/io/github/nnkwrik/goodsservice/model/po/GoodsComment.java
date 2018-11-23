package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/23 16:05
 */
@Data
public class GoodsComment {
    private Integer id;
    private Integer goodsId;
    private String userId;
    private Integer replyCommentId;
    private String replyUserId;
    private String content;
    private boolean isDelete;
    private Data createTime;
}
