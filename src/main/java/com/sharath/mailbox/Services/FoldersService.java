package com.sharath.mailbox.Services;

import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Repository.FolderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoldersService {

    @Autowired
    FolderDAO folderDAO;

    public Folder createFolder(){
        return folderDAO.save(new Folder("Sharath-M1","Inbox","Green"));
    }

    public List<Folder> findAllFoldersById(String userId) {
        return folderDAO.findAllById(userId);
    }
}
