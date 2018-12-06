package io.github.nnkwrik.imservice.dao;

import io.github.nnkwrik.imservice.model.po.History;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author nnkwrik
 * @date 18/12/05 22:20
 */
@Mapper
public interface HistoryMapper {
    @Select("insert into history (chat_id, u1_to_u2, message_type, message_body, send_time)\n" +
            "values (#{chatId}, #{u1ToU2}, #{messageType}, #{messageBody}, #{sendTime});")
    void addHistory(History history);
//    @Select("select message_type,hint,message_body,create_time\n" +
//            "from chat_history\n" +
//            "where chat_user_id = 1\n" +
//            "ORDER BY create_time DESC\n" +
//            "LIMIT 1")
//    History getMostRecentByChatId(@Param("chat_user_id")Integer chatUserId);
}
