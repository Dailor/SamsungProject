package com.samsungproject.SamsungProject.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name = "";
    private String description = "";

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Worker> workers = new  HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Work> works = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Photo> photos = new HashSet<>();


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<Invite> invites = new HashSet<>();

    public void setDirector(Director director) {
        this.director = director;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    public void setWorks(Set<Work> works) {
        this.works = works;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public Director getDirector() {
        return director;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public Set<Work> getWorks() {
        return works;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }


}
