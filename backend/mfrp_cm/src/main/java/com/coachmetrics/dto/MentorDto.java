package com.coachmetrics.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MentorDto {

    public static class Request {
        private String fullName;
        private String email;
        private String department;
        private String cohortCode;
        private String contact;
        private String verticalMapping;
        private String notes;
        private String trainingStatus;
        public Request() {}
        public String getFullName()       { return fullName; }
        public String getEmail()          { return email; }
        public String getDepartment()     { return department; }
        public String getCohortCode()     { return cohortCode; }
        public String getContact()        { return contact; }
        public String getVerticalMapping(){ return verticalMapping; }
        public String getNotes()          { return notes; }
        public String getTrainingStatus() { return trainingStatus; }
        public void setFullName(String fullName)             { this.fullName        = fullName; }
        public void setEmail(String email)                   { this.email           = email; }
        public void setDepartment(String department)         { this.department      = department; }
        public void setCohortCode(String cohortCode)         { this.cohortCode      = cohortCode; }
        public void setContact(String contact)               { this.contact         = contact; }
        public void setVerticalMapping(String verticalMapping){ this.verticalMapping = verticalMapping; }
        public void setNotes(String notes)                   { this.notes           = notes; }
        public void setTrainingStatus(String trainingStatus) { this.trainingStatus  = trainingStatus; }
    }

    public static class Response {
        private Long         id;
        private String       fullName;
        private String       email;
        private String       department;
        private String       cohortCode;
        private String       contact;
        private String       verticalMapping;
        private String       notes;
        private String       trainingStatus;
        private String       coachName;
        private String       coachEmail;
        private Long         coachId;
        private int          totalSessions;
        private double       totalHours;
        private List<MentorConnectDto.Response> connects;
        private LocalDateTime createdAt;
        public Response() {}
        public Long          getId()             { return id; }
        public String        getFullName()       { return fullName; }
        public String        getEmail()          { return email; }
        public String        getDepartment()     { return department; }
        public String        getCohortCode()     { return cohortCode; }
        public String        getContact()        { return contact; }
        public String        getVerticalMapping(){ return verticalMapping; }
        public String        getNotes()          { return notes; }
        public String        getTrainingStatus() { return trainingStatus; }
        public String        getCoachName()      { return coachName; }
        public String        getCoachEmail()     { return coachEmail; }
        public Long          getCoachId()        { return coachId; }
        public int           getTotalSessions()  { return totalSessions; }
        public double        getTotalHours()     { return totalHours; }
        public List<MentorConnectDto.Response> getConnects() { return connects; }
        public LocalDateTime getCreatedAt()      { return createdAt; }
        public void setId(Long id)                           { this.id             = id; }
        public void setFullName(String fullName)             { this.fullName       = fullName; }
        public void setEmail(String email)                   { this.email          = email; }
        public void setDepartment(String department)         { this.department     = department; }
        public void setCohortCode(String cohortCode)         { this.cohortCode     = cohortCode; }
        public void setContact(String contact)               { this.contact        = contact; }
        public void setVerticalMapping(String v)             { this.verticalMapping= v; }
        public void setNotes(String notes)                   { this.notes          = notes; }
        public void setTrainingStatus(String trainingStatus) { this.trainingStatus = trainingStatus; }
        public void setCoachName(String coachName)           { this.coachName      = coachName; }
        public void setCoachEmail(String coachEmail)         { this.coachEmail     = coachEmail; }
        public void setCoachId(Long coachId)                 { this.coachId        = coachId; }
        public void setTotalSessions(int totalSessions)      { this.totalSessions  = totalSessions; }
        public void setTotalHours(double totalHours)         { this.totalHours     = totalHours; }
        public void setConnects(List<MentorConnectDto.Response> connects) { this.connects = connects; }
        public void setCreatedAt(LocalDateTime createdAt)    { this.createdAt      = createdAt; }
    }
}
