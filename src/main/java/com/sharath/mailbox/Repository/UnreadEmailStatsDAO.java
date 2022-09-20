package com.sharath.mailbox.Repository;

import com.sharath.mailbox.Models.UnreadEmailStats;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnreadEmailStatsDAO extends CassandraRepository<UnreadEmailStats,String> {

    List<UnreadEmailStats> findAllById(String id);

    @Query(value = "update unread_email_stats set unreadCount=unreadCount+1" +
            " where user_id=?0 and label=?1")
    public void incrementUnreadCount(String userId,String label);

    @Query(value = "update unread_email_stats set unreadCount=unreadCount-1" +
            " where user_id=?0 and label=?1")
    public void decrementUnreadCount(String userId,String label);
}
