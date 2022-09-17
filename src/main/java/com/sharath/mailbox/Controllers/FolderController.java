package com.sharath.mailbox.Controllers;

import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Repository.FolderDAO;
import com.sharath.mailbox.Services.FoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
public class FolderController {

    @Autowired
    FolderDAO folderDAO;

    @GetMapping ("/")
    public String homepage(@AuthenticationPrincipal OAuth2User principal, Model model){


        if(principal == null || principal.getAttribute("login")==null){
            return "index";
        }
        String userId=principal.getAttribute("login");
//          folderDAO.save(new Folder("Sharath-majjigi","Inbox","Green"));
//        folderDAO.save(new Folder("Sharath-majjigi","Sent","Yellow"));
//        folderDAO.save(new Folder("Sharath-majjigi","Important","Red"));
        List<Folder> userFolders=folderDAO.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
       return "mailbox-page";
    }


}
