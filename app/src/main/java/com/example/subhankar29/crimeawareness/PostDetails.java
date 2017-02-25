package com.example.subhankar29.crimeawareness;

/**
 * Created by mayank on 25/2/17.
 */

public class PostDetails {

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    String subject;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    String desc;

    public ULocation getLocation() {
        return location;
    }

    public void setLocation(ULocation location) {
        this.location = location;
    }

    ULocation location;

    public PostDetails(){} //For Firebase


}


