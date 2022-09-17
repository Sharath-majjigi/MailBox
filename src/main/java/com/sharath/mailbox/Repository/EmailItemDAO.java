package com.sharath.mailbox.Repository;

import com.sharath.mailbox.Models.EmailListItem;
import com.sharath.mailbox.Models.EmailListKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailItemDAO extends CassandraRepository<EmailListItem, EmailListKey> {

    List<EmailListItem> findAllById_IdAndId_Label(String userId, String label);
}
