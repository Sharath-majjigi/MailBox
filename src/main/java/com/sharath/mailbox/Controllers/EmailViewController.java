package com.sharath.mailbox.Controllers;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.sharath.mailbox.Models.Email;
import com.sharath.mailbox.Models.EmailListItem;
import com.sharath.mailbox.Models.EmailListKey;
import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Repository.EmailDAO;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
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


    private PrettyTime prettyTime = new PrettyTime();

    @GetMapping("/emails/{id}")
    public String emailView(@AuthenticationPrincipal OAuth2User principal,
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

        Optional<Email> email=emailDAO.findById(id);
        if (email.isEmpty()){
            return "mailbox-page";
        }

        String toIds=String.join(",",email.get().getTo());
        model.addAttribute("email",email.get());
        model.addAttribute("toIds",toIds);

        return "email-page";

    }


}