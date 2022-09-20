package com.sharath.mailbox.Controllers;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.sharath.mailbox.Models.*;
import com.sharath.mailbox.Repository.EmailDAO;
import com.sharath.mailbox.Repository.EmailItemDAO;
import com.sharath.mailbox.Repository.FolderDAO;
import com.sharath.mailbox.Repository.UnreadEmailStatsDAO;
import com.sharath.mailbox.Services.EmailService;
import com.sharath.mailbox.Services.FoldersService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FolderController {

    @Autowired
    FolderDAO folderDAO;

    @Autowired
    FoldersService foldersService;

    @Autowired
    EmailItemDAO emailItemDAO;

    @Autowired
    EmailDAO emailDAO;

    @Autowired
    EmailService emailService;

    @Autowired
    UnreadEmailStatsDAO unreadEmailStatsDAO;

    private PrettyTime prettyTime = new PrettyTime();

    @GetMapping ("/")
    public String homepage(@AuthenticationPrincipal OAuth2User principal,
                           @RequestParam(required = false) String folder,
                           Model model){


        if(principal == null || principal.getAttribute("login")==null){
            return "index";
        }

        folderDAO.save(new Folder("Sharath-majjigi","Work","Green"));
        folderDAO.save(new Folder("Sharath-majjigi","Family","Yellow"));
        folderDAO.save(new Folder("Sharath-majjigi","Friends","Red"));

//        unreadEmailStatsDAO.incrementUnreadCount("Sharath-majjigi","Inbox");
//        unreadEmailStatsDAO.incrementUnreadCount("Sharath-majjigi","Inbox");
//        unreadEmailStatsDAO.incrementUnreadCount("Sharath-majjigi","Inbox");
        String userId=principal.getAttribute("login");

        EmailListKey emailListKey=new EmailListKey();
        for(int i=0; i<2; i++){
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
//
//            Email email=new Email();
//            email.setId(emailListKey.getTimeId());
//            email.setTo(emailListItem.getTo());
//            email.setFrom(emailListItem.getFrom());
//            email.setSubject(emailListItem.getSubject());
//            email.setBody("Body "+i);
//            emailDAO.save(email);
            emailService.sendMail("Sharath-majjigi", Arrays.asList("abc,god,me"),"Hello"+i,"Body"+i);
        }

        //Fetch Folders


        List<Folder> userFolders=folderDAO.findAllById(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders=foldersService.init(userId);
        model.addAttribute("defaultFolders",defaultFolders);

        model.addAttribute("stats",foldersService.mapCoutToLabels(userId));

        //Fetch message
        if(!StringUtils.hasText(folder)){
            folder="Inbox";
        }
        List<EmailListItem> emails = emailItemDAO.findAllById_IdAndId_Label(userId, folder);
        emails.stream().forEach(email -> {
            Date emailDate = new Date(Uuids.unixTimestamp(email.getId().getTimeId()));
            email.setAgoTime(prettyTime.format(emailDate));
        });


        model.addAttribute("emailList", emails);
        model.addAttribute("folderName",folder);
        System.out.println(principal.getAttribute("login").toString());
       return "mailbox-page";
    }


}
