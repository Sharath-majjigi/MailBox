package com.sharath.mailbox.Controllers;

import com.sharath.mailbox.Models.Email;
import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Repository.EmailDAO;
import com.sharath.mailbox.Repository.FolderDAO;
import com.sharath.mailbox.Services.EmailService;
import com.sharath.mailbox.Services.FoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ComposeMessageController {

    @Autowired
    FolderDAO folderDAO;

    @Autowired
    FoldersService foldersService;

    @Autowired
    EmailService emailService;

    @Autowired
    EmailDAO emailDAO;


    @GetMapping("/compose")
    public String getComposeMailPage(@AuthenticationPrincipal OAuth2User principal,
                            @RequestParam(required = false) String to,
                            @RequestParam(required = false) UUID id,
                            Model model) {


        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }

        //Fetch Folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderDAO.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = foldersService.init(userId);
        model.addAttribute("defaultFolders", defaultFolders);
        model.addAttribute("stats",foldersService.mapCountToLabels(userId));
        model.addAttribute("userName",principal.getAttribute("login").toString());


        List<String> uniqueToIds=emailService.splitIds(to);
        model.addAttribute("toIds",String.join(",",uniqueToIds));



       if(id!=null){
           Optional<Email> optionalEmail=emailDAO.findById(id);
           if(optionalEmail.isPresent()){
               Email email=optionalEmail.get();
            if(emailService.hasAccess(userId,email)){
               model.addAttribute("subject",emailService.getReplySubject(email.getSubject()));
               model.addAttribute("body",emailService.getReplyBody(email));
               }
           }
       }




        return "compose-page";
    }
    @PostMapping("/sendMail")
    public ModelAndView sendMail(@RequestBody MultiValueMap<String,String> formData,
                                 @AuthenticationPrincipal OAuth2User principal){

        if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))){
            return new ModelAndView("redirect:/");
        }
        String from=principal.getAttribute("login");
        List<String> toIds=emailService.splitIds(formData.getFirst("toIds"));
        String subject=formData.getFirst("subject");
        String body=formData.getFirst("body");
        emailService.sendMail(from,toIds,subject,body);

        return new ModelAndView("redirect:/");
    }

}
