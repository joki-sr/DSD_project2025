package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_report")
@Getter @Setter @NoArgsConstructor
public class PatientReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "report_id", unique = true)
    private String reportId;
    
    @Column(name = "report_type")
    private String reportType;
    
    @Column(name = "report_date")
    private LocalDateTime reportDate;
    
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;
    
    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "file_path")
    private String filePath;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public PatientReport(String reportId, String reportType, LocalDateTime reportDate,
                         String diagnosis, String treatment, String notes) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.reportDate = reportDate;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "PatientReport{" +
                "id=" + id +
                ", reportId='" + reportId + '\'' +
                ", reportType='" + reportType + '\'' +
                ", reportDate=" + reportDate +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatment='" + treatment + '\'' +
                '}';
    }
} 