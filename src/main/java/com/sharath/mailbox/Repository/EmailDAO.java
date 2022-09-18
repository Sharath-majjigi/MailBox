package com.sharath.mailbox.Repository;

import com.sharath.mailbox.Models.Email;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailDAO extends CassandraRepository<Email, UUID> {

}
