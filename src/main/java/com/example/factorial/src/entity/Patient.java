package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient")
@Getter @Setter @NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "patient_id", unique = true)
    private String patientId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "birth_date")
    private String birthDate;
    
    @Column(name = "id_type")
    private String idType;
    
    @Column(name = "id_number", unique = true)
    private String idNumber;
    
    @Column(name = "phone")
    private String phone;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientReport> reports = new ArrayList<>();
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorPatientRelation> doctorRelations = new ArrayList<>();

    public Patient(String patientId, String name, String gender, String birthDate, String idType, String idNumber, String phone) {
        this.patientId = patientId;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.idType = idType;
        this.idNumber = idNumber;
        this.phone = phone;
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
} 