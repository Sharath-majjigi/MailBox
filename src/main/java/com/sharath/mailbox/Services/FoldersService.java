package com.sharath.mailbox.Services;

import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Repository.FolderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FoldersService {

    @Autowired
    FolderDAO folderDAO;

    public List<Folder> init(String userId) {
        var defaultFolders = Arrays.asList(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "purple"),
                new Folder(userId, "Important", "red"),
                new Folder(userId, "Done", "green")
        );
        return defaultFolders;
    }

    public List<Folder> findAllFoldersById(String userId) {
        return folderDAO.findAllById(userId);
    }
}
