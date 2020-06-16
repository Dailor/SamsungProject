package com.samsungproject.SamsungProject.models;

import org.hibernate.type.BlobType;

import javax.persistence.*;


import static org.hibernate.type.StandardBasicTypes.BLOB;

@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] fileimage;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Work work;

    public byte[] getFileimage() {
        return fileimage;
    }

    public void setFileimage(byte[] fileimage) {
        this.fileimage = fileimage;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
