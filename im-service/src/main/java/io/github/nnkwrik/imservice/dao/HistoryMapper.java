package io.github.nnkwrik.imservice.dao;

import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.po.HistoryExample;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/05 22:20
 */
@Mapper
public interface HistoryMapper {
    /**
     * 添加一条聊天记录
     *
     * @param history
     */
    @Insert("insert into history (chat_id, u1_to_u2, message_type, message_body, send_time)\n" +
            "values (#{chatId}, #{u1ToU2}, #{messageType}, #{messageBody}, #{sendTime})")
    void addHistory(History history);

    /**
     * 添加多条聊天记录
     * @param historyList
     */
    @Insert({
            "<script>",
            "INSERT INTO history",
            "(chat_id, u1_to_u2, message_type, message_body, send_time)",
            "VALUES" +
                    "<foreach item='item' collection='historyList' open='' separator=',' close=''>" +
                    "(" +
                    "#{item.chatId}, #{item.u1ToU2}, #{item.messageType}, #{item.messageBody}, #{item.sendTime}" +
                    ")" +
                    "</foreach>",
            "</script>"})
    void addHistoryList(@Param("historyList") List<History> historyList);

    /**
     * 获取自己和所有人的最后一条"已读的"聊天记录,取offsetTime之前的,按时间倒序
     *
     * @param unreadChatIds 未读的chatId
     * @param userId        当前用户的id
     * @param offsetTime    取offsetTime之前
     * @return
     */
    @Select("<script>\n" +
            "select history.chat_id, history.message_type, history.message_body, history.send_time, chat.u1, chat.u2, chat.goods_id\n" +
            "from history\n" +
            "       inner join (select chat_id, max(send_time) as max_time\n" +
            "                   from history\n" +
            "                   where chat_id in\n" +
            "                         (select id\n" +
            "                          from chat\n" +
            "                                     where " +
            "                   <if test=\"unreadChatIds.size() > 0\">" +
            "                       id not in\n" +
            "                       <foreach item = 'item' collection = 'unreadChatIds' open = '(' separator = ',' close = ')'>\n" +
            "                       #{item}\n" +
            "                       </foreach>\n" +
            "                            and " +
            "                   </if>" +
            "                   ((u1 = #{user_id} and show_to_u1 = true) or (u2 = #{user_id} and show_to_u2 = true)))\n" +
            "                   group by chat_id\n" +
            "                   HAVING max_time &lt;= #{offset_time,jdbcType=TIMESTAMP}) as foo\n" +
            "         on foo.chat_id = history.chat_id and foo.max_time = history.send_time\n" +
            "       inner join chat on history.chat_id = chat.id\n" +
            "order by send_time desc" +
            "</script>")
    List<HistoryExample> getLastReadChat(@Param("unreadChatIds") List<Integer> unreadChatIds,
                                         @Param("user_id") String userId,
                                         @Param("offset_time") Date offsetTime);


    /**
     * 获取自己和所有人的最后一条聊天记录,按时间倒序
     *
     * @return
     */
    @Select("select history.chat_id, history.message_type, history.message_body, history.send_time, chat.u1, chat.u2, chat.goods_id\n" +
            "from history\n" +
            "       inner join (select chat_id, max(send_time) as max_time\n" +
            "                   from history\n" +
            "                   where chat_id in\n" +
            "                         (select id from chat where ((u1 = 1 and show_to_u1 = true) or (u2 = 1 and show_to_u2 = true)))\n" +
            "                   group by chat_id) as foo on foo.chat_id = history.chat_id and foo.max_time = history.send_time\n" +
            "        inner join chat where history.chat_id = chat.id\n" +
            "order by send_time desc")
    List<HistoryExample> getLastChat();

    /**
     * 根据chatId获取聊天记录
     *
     * @param chat_id
     * @return
     */
    @Select("select u1_to_u2, message_type, message_body, send_time\n" +
            "from history\n" +
            "where chat_id = #{chat_id} and send_time &lt;= #{offset_time,jdbcType=TIMESTAMP} order by send_time desc")
    List<History> getChatHistory(@Param("chat_id") int chatId,@Param("offset_time") Date offsetTime);


}
