package com.sharath.mailbox.Repository;

import com.sharath.mailbox.Models.Folder;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderDAO extends CassandraRepository<Folder,String> {


    List<Folder> findAllById(String UserId);
}
