package com.babajisoft.sample.dto;

import java.io.Serializable;

/**
 * Created by babaji on 28/8/16.
 */

public class PersonInfoDTO implements Serializable {
    private int id;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String NewAddr;

    public String getRedGreen() {
        return redGreen;
    }

    public void setRedGreen(String redGreen) {
        this.redGreen = redGreen;
    }

    private String redGreen;

    public String getJat() {
        return jat;
    }

    public void setJat(String jat) {
        this.jat = jat;
    }

    private String jat;


    public String getNewAddr() {
        return NewAddr;
    }

    public void setNewAddr(String newAddr) {
        NewAddr = newAddr;
    }


    public int getVotedone() {
        return votedone;
    }

    public void setVotedone(int votedone) {
        this.votedone = votedone;
    }

    private int votedone;

    public int getFamilycode() {
        return familycode;
    }

    public void setFamilycode(int familycode) {
        this.familycode = familycode;
    }

    private int familycode;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAliveDead() {
        return aliveDead;
    }

    public void setAliveDead(String aliveDead) {
        this.aliveDead = aliveDead;
    }

    private String dob;
    private String aliveDead;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    private String cardNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    private String mobileNo;

    private int vibhagNo;
    private int partNo;

    public String getHno() {
        return hno;
    }

    public void setHno(String hno) {
        this.hno = hno;
    }

    private String hno;
    private String ageSex;

    public int getSectionNo() {
        return sectionNo;
    }

    public void setSectionNo(int sectionNo) {
        this.sectionNo = sectionNo;
    }

    private int sectionNo;

    public int getVoterNo() {
        return voterNo;
    }

    public void setVoterNo(int voterNo) {
        this.voterNo = voterNo;
    }

    private int voterNo;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getVibhagNo() {
        return vibhagNo;
    }

    public void setVibhagNo(int vibhagNo) {
        this.vibhagNo = vibhagNo;
    }

    public int getPartNo() {
        return partNo;
    }

    public void setPartNo(int partNo) {
        this.partNo = partNo;
    }

    public String getAgeSex() {
        return ageSex;
    }

    public void setAgeSex(String ageSex) {
        this.ageSex = ageSex;
    }

}
