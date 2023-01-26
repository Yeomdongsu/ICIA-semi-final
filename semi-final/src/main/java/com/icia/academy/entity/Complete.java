package com.icia.academy.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "completetbl")
@Data
public class Complete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpnum;

    @ManyToOne
    @JoinColumn(name = "cpcname")
    private Course cpcname;

    @ManyToOne
    @JoinColumn(name = "cpcmid")
    private Member cpcmid;

    @Column
    @CreationTimestamp()
    private Timestamp cpdate;

}
