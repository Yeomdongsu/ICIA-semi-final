package com.icia.academy.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "membertbl")
@Data
@DynamicInsert
public class Member {

    @Id
    private String mid;

    @Column(nullable = false)
    private String mpwd;

    @Column(nullable = false)
    private String mname;

    @Column(nullable = false)
    private int mage;

    @Column(nullable = false)
    private String maddress;

    @Column(nullable = false)
    private String mphone;

    @ColumnDefault("1")
    private String mgrade;
}
