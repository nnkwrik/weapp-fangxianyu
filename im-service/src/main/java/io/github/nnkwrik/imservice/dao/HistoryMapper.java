package io.github.nnkwrik.imservice.dao;

import io.github.nnkwrik.imservice.model.po.ChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author nnkwrik
 * @date 18/12/05 22:20
 */
@Mapper
public interface HistoryMapper {
    @Select("select message_type,hint,message_body,create_time\n" +
            "from chat_history\n" +
            "where chat_user_id = 1\n" +
            "ORDER BY create_time DESC\n" +
            "LIMIT 1")
    ChatHistory getMostRecentByChatId(@Param("chat_user_id")Integer chatUserId);
}
