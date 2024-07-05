package com.iwomi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="mav_merchants")

public class MavianceMerchants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true )
    private Long id;
    private String merchant;
    private String name;
    private String category;
    private String description ;
    private String country;
    private String status;
    private String logo;
    private String logoHash;

    //constructor

    public MavianceMerchants(String merchant, String name, String category, String description, String country,
            String status, String logo, String logoHash) {
        this.merchant = merchant;
        this.name = name;
        this.category = category;
        this.description = description;
        this.country = country;
        this.status = status;
        this.logo = logo;
        this.logoHash = logoHash;
    }


    //getters and setters
    public String getMerchant() {
        return merchant;
    }
    
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getLogoHash() {
        return logoHash;
    }
    public void setLogoHash(String logoHash) {
        this.logoHash = logoHash;
    }

    


    
    
}
