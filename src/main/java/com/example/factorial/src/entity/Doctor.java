package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor")
@Getter @Setter @NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "doctor_id", unique = true)
    private String doctorId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "phone")
    private String phone;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorPatientRelation> patientRelations = new ArrayList<>();

    public Doctor(String doctorId, String name, String gender, String department, String title, String phone) {
        this.doctorId = doctorId;
        this.name = name;
        this.gender = gender;
        this.department = department;
        this.title = title;
        this.phone = phone;
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", doctorId='" + doctorId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", department='" + department + '\'' +
                ", title='" + title + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
} 