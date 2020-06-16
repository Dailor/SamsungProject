package com.samsungproject.SamsungProject.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.DigestUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String name;

    private String surname;

    private String password;

    private boolean active;

    private String aboutMe;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private String secret_question;

    private String answer_question;

    private long balance = 0;


    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Worker worker;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Director director;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EventNotification> eventNotifications = new HashSet<>();

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public String getSecret_question() {
        return secret_question;
    }

    public void setSecret_question(String secret_question) {
        this.secret_question = secret_question;
    }

    public String getAnswer_question() {
        return answer_question;
    }

    public void setAnswer_question(String answer_question) {
        this.answer_question = DigestUtils.md5DigestAsHex(answer_question.getBytes());
    }

    public long getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }

    public Worker getWorker() {
        return worker;
    }

    public Director getDirector() {
        return director;
    }

    public void updateBalance(long value) {
        this.balance += value;
    }

    public User() {
    }

    public String getFullName() {
        return this.surname + " " + this.name;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<EventNotification> getEventNotifications() {
        return eventNotifications;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public boolean checkSecretQuestion(String answer_question){
        return this.answer_question.equals(DigestUtils.md5DigestAsHex(answer_question.getBytes()));
    }
}

