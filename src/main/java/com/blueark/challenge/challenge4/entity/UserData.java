package com.blueark.challenge.challenge4.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "USERS")
public class UserData {

    @Id
    @Column
    private String userId;
    @Column
    private Date departureDate;
    @Column
    private Date returnDate;
    @Column
    private String residenceType;
    @Column
    private Double averageConsumption;

}
