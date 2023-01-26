package com.icia.academy.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coursetbl")
@Data
public class Course {

    @Id
    @Column(nullable = false)
    private String cname;

    @Column(nullable = false)
    private String ctname;

    @Column(nullable = false)
    private String cdate;

    @Column(nullable = false)
    private String cprice;

    @Column(nullable = false)
    private String cintro;
}
