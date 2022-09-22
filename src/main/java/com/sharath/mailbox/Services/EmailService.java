package com.sharath.mailbox.Services;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.sharath.mailbox.Models.Email;
import com.sharath.mailbox.Models.EmailListItem;
import com.sharath.mailbox.Models.EmailListKey;
import com.sharath.mailbox.Repository.EmailDAO;
import com.sharath.mailbox.Repository.EmailItemDAO;
import com.sharath.mailbox.Repository.UnreadEmailStatsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    EmailDAO emailDAO;

    @Autowired
    EmailItemDAO emailItemDAO;

    @Autowired
    UnreadEmailStatsDAO unreadEmailStatsDAO;

    public void sendMail(String from, List<String> to, String subject, String body){

        Email email=new Email();
        email.setTo(to);
        email.setFrom(from);
        email.setBody(body);
        email.setSubject(subject);
        email.setId(Uuids.timeBased());
        emailDAO.save(email);

        to.forEach(toId ->{
            EmailListItem listItem=createEmailListItem(to, subject, email, toId,"Inbox");
            emailItemDAO.save(listItem);
            unreadEmailStatsDAO.incrementUnreadCount(toId,"Inbox");
        });

        EmailListItem sentItems = createEmailListItem(to, subject, email, from, "Sent");
        sentItems.setRead(true);
        emailItemDAO.save(sentItems);

    }

    public List<String> splitIds(String to){
        if(!StringUtils.hasText(to)) {
            return new ArrayList<>();
        }
        String[] splitIds= to.split(",");
        List<String> uniqueToIds= Arrays.asList(splitIds)
                .stream()
                .map(id -> StringUtils.trimWhitespace(id))
                .filter(id -> StringUtils.hasText(id))
                .distinct()
                .collect(Collectors.toList());
        return uniqueToIds;
    }
    public boolean hasAccess(String userId,Email email){
        return (userId.equals(email.getFrom()) || email.getTo().contains(userId));

    }

    public String getReplySubject(String subject){
        return "Re " + subject;
    }
    public String getReplyBody(Email email){
        return "\n\n\n----------------------------------\n"+
                "From: " + email.getFrom() + "\n"+
                "To: " + email.getTo() + "\n"+
                email.getBody();
    }

    private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner,String folder) {
        EmailListKey key=new EmailListKey();
        key.setId(itemOwner);
        key.setLabel(folder);
        key.setTimeId(email.getId());
        EmailListItem item=new EmailListItem();
        item.setId(key);
        item.setTo(to);
        item.setSubject(subject);
        item.setRead(false);
        return item;
    }
}
