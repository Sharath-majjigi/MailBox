package com.sharath.mailbox.Services;

import com.sharath.mailbox.Models.Folder;
import com.sharath.mailbox.Models.UnreadEmailStats;
import com.sharath.mailbox.Repository.FolderDAO;
import com.sharath.mailbox.Repository.UnreadEmailStatsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoldersService {

    @Autowired
    FolderDAO folderDAO;

    @Autowired
    UnreadEmailStatsDAO unreadEmailStatsDAO;

    public List<Folder> init(String userId) {
        var defaultFolders = Arrays.asList(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "purple"),
                new Folder(userId, "Important", "red"),
                new Folder(userId, "Done", "green")
        );
        return defaultFolders;
    }

    public Map<String,Integer> mapCountToLabels(String userId){
        List<UnreadEmailStats> unreadEmailStats=unreadEmailStatsDAO.findAllById(userId);
        return unreadEmailStats.stream()
                .collect(Collectors.toMap(UnreadEmailStats::getLabel,UnreadEmailStats::getUnreadCount));
    }

    public List<Folder> findAllFoldersById(String userId) {
        return folderDAO.findAllById(userId);
    }
}
