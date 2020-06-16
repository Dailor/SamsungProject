package com.samsungproject.SamsungProject.models;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@Entity
public class Work {
    public long getId() {
        return id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address.trim();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getDate_given() {
        return date_given;
    }

    public void setDate_given(long date_given) {
        this.date_given = date_given;
    }

    public long getDate_end() {
        return date_end;
    }

    public void setDate_end(long date_end) {
        this.date_end = date_end;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String description;

    private String address;

    private int price;

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    private boolean payment = false;

    private long date_given = 0;
    private long date_end = 0;

    private String result;
    private String status;

    @OneToMany(mappedBy = "work")
    private Set<Photo> photos = new HashSet<>();

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    public Work(){
    }

    public String getStartTimeToString(){
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+6:00"));
        date.setTimeInMillis(this.date_given * 1000);
        return date.getTime().toString();
    }

    public String getEndTimeToString(){
        if(this.date_end != 0l){
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+6:00"));
        date.setTimeInMillis(this.date_given * 1000);
        return date.getTime().toString();
        }
        return "Не указана";
    }

    public String getWorkerFullName(){
        return this.worker.getUser().getFullName();
    }

}
