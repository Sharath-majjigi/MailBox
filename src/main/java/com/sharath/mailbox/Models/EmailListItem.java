package com.sharath.mailbox.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "messages_of_user")
public class EmailListItem {

    @PrimaryKey
    private EmailListKey id;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String subject;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String from;

    @CassandraType(type = CassandraType.Name.LIST,typeArguments = CassandraType.Name.TEXT)
    private List<String> to;

    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean isRead;

    @Transient
    private String agoTime;


}
