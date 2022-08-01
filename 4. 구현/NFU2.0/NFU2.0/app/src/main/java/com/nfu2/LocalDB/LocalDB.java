package com.nfu2.LocalDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LocalDB {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean bioFlag;
    private String user_id;
    private String user_pwd;

    public LocalDB(boolean bioFlag, String user_id, String user_pwd) {
        this.bioFlag = bioFlag;
        this.user_id = user_id;
        this.user_pwd = user_pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBioFlag() {
        return bioFlag;
    }

    public void setBioFlag(boolean bioFlag) {
        this.bioFlag = bioFlag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }
}
