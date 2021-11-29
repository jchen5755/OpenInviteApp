package com.example.openinvitetest;

import androidx.lifecycle.ViewModel;

public class userIdViewModel extends ViewModel {
    public String userID;

    public void setUserID(String id) {
        userID = id;
    }

    public String returnID() {
        return this.userID;
    }
}
