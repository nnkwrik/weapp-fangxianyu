package io.github.nnkwrik.imservice.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.imservice.model.po.History;
import lombok.Data;

import java.util.Date;

/**
 * 消息列表页面的各个消息.
 *
 * @author nnkwrik
 * @date 18/12/07 15:56
 */
@Data
public class ChatIndexEle {

    /*未读数*/
    private Integer unreadCount;

    /*对方用户信息*/
    private SimpleUser otherSide;

    /*与本次聊天相关的商品的信息*/
    private SimpleGoods goods;

    /*最后一条聊天记录*/
    private History lastChat;

    /*方便调用其他服务进行查询,不传前端*/
    @JsonIgnore
    private String userId;

    @JsonIgnore
    private Integer goodsId;

}

