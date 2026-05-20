package com.coachmetrics.entity;

import com.coachmetrics.enums.Department;
import com.coachmetrics.enums.TrainingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mentors")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    private String cohortCode;
    private String contact;
    private String verticalMapping;
    private String notes;

    @Enumerated(EnumType.STRING)
    private TrainingStatus trainingStatus = TrainingStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorConnect> mentorConnects = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Mentor() {}

    @PrePersist
    void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate
    void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long           getId()             { return id; }
    public String         getFullName()       { return fullName; }
    public String         getEmail()          { return email; }
    public Department     getDepartment()     { return department; }
    public String         getCohortCode()     { return cohortCode; }
    public String         getContact()        { return contact; }
    public String         getVerticalMapping(){ return verticalMapping; }
    public String         getNotes()          { return notes; }
    public TrainingStatus getTrainingStatus() { return trainingStatus; }
    public User           getCoach()          { return coach; }
    public List<MentorConnect> getMentorConnects() { return mentorConnects; }
    public LocalDateTime  getCreatedAt()      { return createdAt; }
    public LocalDateTime  getUpdatedAt()      { return updatedAt; }

    public void setId(Long id)                            { this.id              = id; }
    public void setFullName(String fullName)              { this.fullName        = fullName; }
    public void setEmail(String email)                    { this.email           = email; }
    public void setDepartment(Department department)      { this.department      = department; }
    public void setCohortCode(String cohortCode)          { this.cohortCode      = cohortCode; }
    public void setContact(String contact)                { this.contact         = contact; }
    public void setVerticalMapping(String verticalMapping){ this.verticalMapping = verticalMapping; }
    public void setNotes(String notes)                    { this.notes           = notes; }
    public void setTrainingStatus(TrainingStatus status)  { this.trainingStatus  = status; }
    public void setCoach(User coach)                      { this.coach           = coach; }
    public void setMentorConnects(List<MentorConnect> mentorConnects) { this.mentorConnects = mentorConnects; }
    public void setCreatedAt(LocalDateTime createdAt)     { this.createdAt       = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)     { this.updatedAt       = updatedAt; }
}
