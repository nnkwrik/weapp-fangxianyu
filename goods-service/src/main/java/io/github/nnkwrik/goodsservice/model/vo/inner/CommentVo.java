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
    private Integer goods_id;
    private String user_id;
    private Integer reply_comment_id;
    private String reply_user_id;
    private String content;
    private boolean is_delete;
    private String create_time;

    SimpleUser simpleUser;
    List<CommentVo> replyList;
}
