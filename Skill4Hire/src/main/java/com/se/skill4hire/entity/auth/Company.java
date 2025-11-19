package com.se.skill4hire.entity.auth;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companies")
public class Company extends User {

    private String name;
    private String description;
    private String phone;
    private String website;
    private String address;
    private String facebook;
    private String linkedin;
    private String twitter;
    private String logo;
    private String industry;
    private String companySize;
    private String founded;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Company() {
        super();
    }

    public Company(String email, String password, String name, String description,
                   String phone, String website, String address,
                   String facebook, String linkedin, String twitter,
                   String industry, String companySize, String founded,
                   String city, String state, String zipCode, String country) {
        super(email, password);
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.website = website;
        this.address = address;
        this.facebook = facebook;
        this.linkedin = linkedin;
        this.twitter = twitter;
        this.industry = industry;
        this.companySize = companySize;
        this.founded = founded;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public void setRole(String role) {
        super.setRole(role);
    }
}
