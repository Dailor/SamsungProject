package com.samsungproject.SamsungProject.models;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    public Set<Invite> getInvites() {
        return invites;
    }

    public void setInvites(Set<Invite> invites) {
        this.invites = invites;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public Company getCompany() {
        return company;
    }

    public User getUser() {
        return user;
    }

    public Set<Work> getWorks() {
        return works;
    }

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<Work> works = new HashSet<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private Set<Invite> invites = new HashSet<>();

}



