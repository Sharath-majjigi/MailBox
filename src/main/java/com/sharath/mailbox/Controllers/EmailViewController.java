package com.sharath.mailbox.Controllers;
import com.sharath.mailbox.Models.Email;
import com.sharath.mailbox.Models.EmailListItem;
import com.sharath.mailbox.Models.EmailListKey;
import com.sharath.mailbox.Models.Folder;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailViewController {

    @Autowired
    EmailDAO emailDAO;

    @Autowired
    FolderDAO folderDAO;

    @Autowired
    FoldersService foldersService;

    @Autowired
    EmailItemDAO emailItemDAO;

    @Autowired
    UnreadEmailStatsDAO unreadEmailStatsDAO;

    @Autowired
    EmailService emailService;


    private PrettyTime prettyTime = new PrettyTime();

    @GetMapping("/emails/{id}")
    public String emailView(@AuthenticationPrincipal OAuth2User principal,
                            @RequestParam String folder,
                            @PathVariable UUID id,
                            Model model){


        if(principal == null || principal.getAttribute("login")==null){
            return "index";
        }

        //Fetch Folders
        String userId=principal.getAttribute("login");
        List<Folder> userFolders=folderDAO.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders=foldersService.init(userId);
        model.addAttribute("defaultFolders",defaultFolders);
        model.addAttribute("userName",principal.getAttribute("login").toString());


        Optional<Email> email=emailDAO.findById(id);
        if (email.isEmpty()){
            return "mailbox-page";
        }

        String toIds=String.join(",",email.get().getTo());

  //      Checking whether user is allowed to view Mail
        if(!emailService.hasAccess(userId,email.get())){
            return "redirect:/";
        }

        model.addAttribute("email",email.get());
        model.addAttribute("toIds",toIds);



        //For Unread And Read Mails

        EmailListKey key=new EmailListKey();
        key.setId(userId);
        key.setLabel(folder);
        key.setTimeId(email.get().getId());

        Optional<EmailListItem> emailListItem=emailItemDAO.findById(key);
        if(emailListItem.isPresent()){
            EmailListItem listItem=emailListItem.get();
            if(!listItem.isRead()){
                listItem.setRead(true);
                emailItemDAO.save(listItem);
                unreadEmailStatsDAO.decrementUnreadCount(userId,folder);
            }
        }

        model.addAttribute("stats",foldersService.mapCountToLabels(userId));

        return "email-page";

    }


}
