package com.fita.vetclinic.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.fita.vetclinic.dao.UserDAO;
import com.fita.vetclinic.models.User;

public class UserSession {

    private static UserSession instance;
    private User currentUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void clearSession() {
        currentUser = null;
        instance = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean updateAvatar(File selectedFile) {
        User user = this.getUser();
        try {
            File destDir = new File(System.getProperty("user.dir"), "img");
            if (!destDir.exists()) destDir.mkdirs();

            String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
            String newFileName = "avatar_" + System.currentTimeMillis() + ext;
            File destFile = new File(destDir, newFileName);

            Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            String relativePath = "resources/images/user/" + newFileName;

            UserDAO userDAO = new UserDAO();
            if (userDAO.updateAvatar(user.getEmail(), relativePath)) {
                user.setImagePath(relativePath);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
