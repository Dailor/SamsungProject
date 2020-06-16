package com.samsungproject.SamsungProject.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Company> companies = new HashSet<>();

    public void setUser(User user) {
        this.user = user;
    }

    public void addCompany(Company company){
        company.setDirector(this);
        this.companies.add(company);
    }

    public User getUser() {
        return user;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

}
