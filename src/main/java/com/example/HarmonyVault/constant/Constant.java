package com.example.HarmonyVault.constant;

public class Constant {

    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Documents/HarmonyVault-Uploads/"; 
    // retrieves the user's home directory path
    // on Windows, it might be C:\Users\Username
    public static final String X_REQUESTED_WITH = "X-Requested-With";

    //"X-Requested-With" is a common HTTP header used by web clients to signal that a 
    // request was made via JavaScript, usually with a value like XMLHttpRequest

}
