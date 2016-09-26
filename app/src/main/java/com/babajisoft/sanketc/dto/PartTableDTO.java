package com.babajisoft.sanketc.dto;

/**
 * Created by babaji on 8/9/16.
 */

public class PartTableDTO {
    public int getYadibhag() {
        return yadibhag;
    }

    public void setYadibhag(int yadibhag) {
        this.yadibhag = yadibhag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSectionno() {
        return sectionno;
    }

    public void setSectionno(int sectionno) {
        this.sectionno = sectionno;
    }

    int yadibhag;
   int sectionno;
   String address;
}
