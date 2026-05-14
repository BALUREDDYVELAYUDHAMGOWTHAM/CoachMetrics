package com.coachmetrics.service;

import com.coachmetrics.dto.DashboardDto;
import com.coachmetrics.dto.ReportDto;
import com.coachmetrics.entity.Mentor;
import com.coachmetrics.entity.User;
import com.coachmetrics.enums.Department;
import com.coachmetrics.enums.UserRole;
import com.coachmetrics.exception.ResourceNotFoundException;
import com.coachmetrics.repository.MentorConnectRepository;
import com.coachmetrics.repository.MentorRepository;
import com.coachmetrics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MentorRepository mentorRepo;

    @Autowired
    private MentorConnectRepository connectRepo;

    @Autowired
    private ActivityLogService logService;

    public DashboardDto getAdminDashboard() {
        DashboardDto dto = new DashboardDto();
        dto.setTotalMentors(mentorRepo.count());
        dto.setActiveCoaches(userRepo.countByRoleAndActive(UserRole.COACH, true));
        dto.setSdetCount(mentorRepo.countByDepartment(Department.SDET));
        dto.setDotnetCount(mentorRepo.countByDepartment(Department.DOTNET));
        dto.setTotalSessions(connectRepo.countAllSessions());
        Double hours = connectRepo.sumAllHours();
        dto.setTotalHours(hours != null ? hours : 0);

        Map<String, Long> dist = new LinkedHashMap<>();
        dist.put("SDET",   mentorRepo.countByDepartment(Department.SDET));
        dist.put(".NET/C#",mentorRepo.countByDepartment(Department.DOTNET));
        dto.setDeptDistribution(dist);
        dto.setRecentActivity(logService.getRecent(10));
        return dto;
    }

    public DashboardDto getCoachDashboard(String coachEmail) {
        User coach = userRepo.findByEmail(coachEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found"));
        DashboardDto dto = new DashboardDto();
        dto.setMyMentors(mentorRepo.countByCoach(coach));
        long sessions = connectRepo.countSessionsByCoachId(coach.getId());
        dto.setSessionsThisMonth(sessions);
        dto.setUpcomingSessions(0L);
        List<String> cohorts = mentorRepo.findDistinctCohortsByCoach(coach);
        dto.setCohortsAssigned((long) cohorts.size());
        dto.setRecentActivity(logService.getRecent(8));
        return dto;
    }

    public List<ReportDto> getReport(String dept, String period) {
        List<Mentor> mentors;
        if (dept != null && !dept.isEmpty()) {
            mentors = mentorRepo.findByDepartment(Department.valueOf(dept));
        } else {
            mentors = mentorRepo.findAll();
        }
        List<ReportDto> result = new ArrayList<>();
        for (Mentor m : mentors) {
            long sessions = connectRepo.countSessionsByCoachId(m.getCoach().getId());
            Double hrs = connectRepo.sumHoursByCoachId(m.getCoach().getId());
            double totalHrs = hrs != null ? hrs : 0;
            ReportDto r = new ReportDto();
            r.setMentorName(m.getFullName());
            r.setDepartment(m.getDepartment().name());
            r.setCohortCode(m.getCohortCode());
            r.setCoachName(m.getCoach().getFullName());
            r.setTotalSessions((int) sessions);
            r.setTotalHours(totalHrs);
            r.setAvgDuration(sessions > 0 ? totalHrs / sessions : 0);
            result.add(r);
        }
        return result;
    }
}
