package com.sharath.mailbox.Controllers;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.sharath.mailbox.Models.EmailListItem;
import com.sharath.mailbox.Models.EmailListKey;
import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Repository.EmailItemDAO;
import com.sharath.mailbox.Repository.FolderDAO;
import com.sharath.mailbox.Services.FoldersService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Date;
import java.util.List;

@Controller
public class FolderController {

    @Autowired
    FolderDAO folderDAO;

    @Autowired
    EmailItemDAO emailItemDAO;

    private PrettyTime prettyTime = new PrettyTime();

    @GetMapping ("/")
    public String homepage(@AuthenticationPrincipal OAuth2User principal, Model model){


        if(principal == null || principal.getAttribute("login")==null){
            return "index";
        }
        String userId=principal.getAttribute("login");
//          folderDAO.save(new Folder("Sharath-majjigi","Inbox","Green"));
//        folderDAO.save(new Folder("Sharath-majjigi","Sent","Yellow"));
//        folderDAO.save(new Folder("Sharath-majjigi","Important","Red"));
//        EmailListKey emailListKey=new EmailListKey();
//        for(int i=0; i<10; i++){
//            emailListKey.setId("Sharath-majjigi");
//            emailListKey.setLabel("Inbox");
//            emailListKey.setTimeId(Uuids.timeBased());
//
//            EmailListItem emailListItem=new EmailListItem();
//            emailListItem.setId(emailListKey);
//            emailListItem.setTo(List.of("Sharath","Bharath"));
//            emailListItem.setFrom("God");
//            emailListItem.setSubject("subject"+i);
//            emailListItem.setRead(false);
//            emailItemDAO.save(emailListItem);
//        }


        List<Folder> userFolders=folderDAO.findAllById(userId);
        model.addAttribute("userFolders", userFolders);

        List<EmailListItem> emails = emailItemDAO.findAllById_IdAndId_Label(userId, "Inbox");
        emails.stream().forEach(email -> {
            Date emailDate = new Date(Uuids.unixTimestamp(email.getId().getTimeId()));
            email.setAgoTime(prettyTime.format(emailDate));
        });
        model.addAttribute("emailList", emails);
       return "mailbox-page";
    }


}
