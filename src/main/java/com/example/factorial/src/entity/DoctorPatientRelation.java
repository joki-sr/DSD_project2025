package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_patient_relation")
@Getter @Setter @NoArgsConstructor
public class DoctorPatientRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @Column(name = "relation_type")
    private String relationType;  // 关系类型：主治医师、专家会诊等
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "status")
    private String status;  // 关系状态：active, inactive, pending等
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "active";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public DoctorPatientRelation(Doctor doctor, Patient patient, String relationType) {
        this.doctor = doctor;
        this.patient = patient;
        this.relationType = relationType;
        this.startDate = LocalDateTime.now();
        this.status = "active";
    }
    
    @Override
    public String toString() {
        return "DoctorPatientRelation{" +
                "id=" + id +
                ", doctor=" + (doctor != null ? doctor.getDoctorId() : "null") +
                ", patient=" + (patient != null ? patient.getPatientId() : "null") +
                ", relationType='" + relationType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
} 