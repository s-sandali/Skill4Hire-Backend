package com.se.skill4hire.dto.auth;

public class CompanyRegRequest extends RegisterRequest {

    private String name;
    private String description;
    private String phone;
    private String website;
    private String address;
    private String facebook;
    private String linkedin;
    private String twitter;

    public CompanyRegRequest() {}

    public CompanyRegRequest(String email, String password, String role,
                                  String name, String description,
                                  String phone, String website, String address,
                                  String facebook, String linkedin, String twitter) {
        super(email, password, role);
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.website = website;
        this.address = address;
        this.facebook = facebook;
        this.linkedin = linkedin;
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    // Getters and Setters...
}
