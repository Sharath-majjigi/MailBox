package com.sharath.mailbox;

import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Repository.FolderDAO;
import com.sharath.mailbox.Services.FoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.file.Path;



@SpringBootApplication
@RestController
public class MailBoxApplication {

//    @Autowired
//    FolderDAO folderDAO;
    public static void main(String[] args) {
        SpringApplication.run(MailBoxApplication.class, args);
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal);
        return principal.getAttribute("name");
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

//    @PostConstruct
//    public void init(){
//       folderDAO.save(new Folder("Sharath-M1","Inbox","Green"));
//        folderDAO.save(new Folder("Sharath-M2","Sent","Yellow"));
//        folderDAO.save(new Folder("Sharath-M3","Important","Red"));
//    }


}
