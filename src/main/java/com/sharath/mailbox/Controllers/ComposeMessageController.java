package com.sharath.mailbox.Controllers;

import com.sharath.mailbox.Models.Folder;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ComposeMessageController {

    @Autowired
    FolderDAO folderDAO;

    @Autowired
    FoldersService foldersService;

    @Autowired
    EmailService emailService;


    @GetMapping("/compose")
    public String getComposeMailPage(@AuthenticationPrincipal OAuth2User principal,
                            @RequestParam(required = false) String to,
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
        model.addAttribute("stats",foldersService.mapCoutToLabels(userId));


        List<String> uniqueToIds=splitIds(to);
        model.addAttribute("toIds",String.join(",",uniqueToIds));

        return "compose-page";
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

    @PostMapping("/sendMail")
    public ModelAndView sendMail(@RequestBody MultiValueMap<String,String> formData,
                                 @AuthenticationPrincipal OAuth2User principal){

        if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))){
            return new ModelAndView("redirect:/");
        }
        String from=principal.getAttribute("login");
        List<String> toIds=splitIds(formData.getFirst("toIds"));
        String subject=formData.getFirst("subject");
        String body=formData.getFirst("body");
        emailService.sendMail(from,toIds,subject,body);

        return new ModelAndView("redirect:/");
    }
}
