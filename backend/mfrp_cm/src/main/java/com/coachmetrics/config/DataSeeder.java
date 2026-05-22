package com.coachmetrics.config;

import com.coachmetrics.entity.Mentor;
import com.coachmetrics.entity.MentorConnect;
import com.coachmetrics.entity.User;
import com.coachmetrics.enums.Department;
import com.coachmetrics.enums.MentorConnectMode;
import com.coachmetrics.enums.TrainingStatus;
import com.coachmetrics.enums.UserRole;
import com.coachmetrics.repository.MentorConnectRepository;
import com.coachmetrics.repository.MentorRepository;
import com.coachmetrics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MentorRepository mentorRepo;
    @Autowired
    private MentorConnectRepository connectRepo;
    @Autowired
    private PasswordEncoder encoder;

    private static final String[] WEEK_RANGES = {
        "19 Jan - 23 Jan", "27 Jan - 30 Jan", "2 Feb - 6 Feb",
        "9 Feb - 13 Feb", "16 Feb - 20 Feb", "23 Feb - 27 Feb",
        "2 Mar - 6 Mar", "9 Mar - 13 Mar", "16 Mar - 19 Mar",
        "23 Mar - 27 Mar", "30 Mar - 03 Apr", "06 Apr - 10 Apr",
        "13 Apr - 17 Apr", "20 Apr - 24 Apr"
    };

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return;
        System.out.println("Seeding Coach Metrics demo data...");

        // Admin
        User admin = new User();
        admin.setFullName("Admin User");
        admin.setEmail("admin@cm.com");
        admin.setPassword(encoder.encode("password"));
        admin.setRole(UserRole.ADMIN);
        admin.setActive(true);
        admin = userRepo.save(admin);

        // Coaches
        User coach1 = new User();
        coach1.setFullName("Sarah Williams");
        coach1.setEmail("sarah@cm.com");
        coach1.setPassword(encoder.encode("password"));
        coach1.setRole(UserRole.COACH);
        coach1.setDepartment(Department.SDET);
        coach1.setContact("9876543210");
        coach1.setActive(true);
        coach1 = userRepo.save(coach1);

        User coach2 = new User();
        coach2.setFullName("John Davis");
        coach2.setEmail("john@cm.com");
        coach2.setPassword(encoder.encode("password"));
        coach2.setRole(UserRole.COACH);
        coach2.setDepartment(Department.DOTNET);
        coach2.setContact("9876543211");
        coach2.setActive(true);
        coach2 = userRepo.save(coach2);

        User coach3 = new User();
        coach3.setFullName("Priya Sharma");
        coach3.setEmail("priya@cm.com");
        coach3.setPassword(encoder.encode("password"));
        coach3.setRole(UserRole.COACH);
        coach3.setDepartment(Department.SDET);
        coach3.setContact("9876543212");
        coach3.setActive(true);
        coach3 = userRepo.save(coach3);

        // Mentors for Sarah (SDET)
        createMentorWithConnects("Arun Kumar",    "arun@mentor.com",    Department.SDET,   "COH-2024-A", "SDET Track 1", coach1, TrainingStatus.ACTIVE,    true);
        createMentorWithConnects("Divya Nair",    "divya@mentor.com",   Department.SDET,   "COH-2024-A", "SDET Track 1", coach1, TrainingStatus.ACTIVE,    true);
        createMentorWithConnects("Ravi Patel",    "ravi@mentor.com",    Department.SDET,   "COH-2024-B", "SDET Track 2", coach1, TrainingStatus.ON_HOLD,   false);

        // Mentors for John (.NET)
        createMentorWithConnects("Meena Thomas",  "meena@mentor.com",   Department.DOTNET, "COH-2024-C", ".NET Track 1", coach2, TrainingStatus.ACTIVE,    true);
        createMentorWithConnects("Suresh Babu",   "suresh@mentor.com",  Department.DOTNET, "COH-2024-C", ".NET Track 1", coach2, TrainingStatus.COMPLETED,  true);
        createMentorWithConnects("Kavya Menon",   "kavya@mentor.com",   Department.DOTNET, "COH-2024-D", ".NET Track 2", coach2, TrainingStatus.ACTIVE,    true);

        // Mentors for Priya (SDET)
        createMentorWithConnects("Michael Brown", "michael@mentor.com", Department.SDET,   "COH-2024-E", "SDET Track 3", coach3, TrainingStatus.ACTIVE,    true);
        createMentorWithConnects("Anita Rao",     "anita@mentor.com",   Department.SDET,   "COH-2024-E", "SDET Track 3", coach3, TrainingStatus.DROPPED,   false);

        System.out.println("Demo data seeded. Login: admin@cm.com / sarah@cm.com / john@cm.com / priya@cm.com (password: password)");
    }

    private void createMentorWithConnects(String name, String email, Department dept,
                                           String cohortCode, String vertical, User coach,
                                           TrainingStatus status, boolean hasConnects) {
        Mentor mentor = new Mentor();
        mentor.setFullName(name);
        mentor.setEmail(email);
        mentor.setDepartment(dept);
        mentor.setCohortCode(cohortCode);
        mentor.setVerticalMapping(vertical);
        mentor.setTrainingStatus(status);
        mentor.setCoach(coach);
        mentor = mentorRepo.save(mentor);

        // Create 14 weekly connect entries
        for (int i = 0; i < WEEK_RANGES.length; i++) {
            MentorConnect mc = new MentorConnect();
            mc.setMentor(mentor);
            mc.setWeekRange(WEEK_RANGES[i]);
            mc.setWeekNumber(i + 1);

            // Fill first 6 weeks with data if hasConnects
            if (hasConnects && i < 6) {
                mc.setHappened(true);
                mc.setMode(i % 3 == 0 ? MentorConnectMode.IN_PERSON : MentorConnectMode.VIRTUAL);
                mc.setConnectDate(LocalDate.of(2026, 1, 19).plusWeeks(i));
                mc.setHours(i % 2 == 0 ? 1.5 : 1.0);
                mc.setReason(null);
            } else if (hasConnects && i == 6) {
                mc.setHappened(false);
                mc.setMode(MentorConnectMode.NOT_HAPPENED);
                mc.setReason("Mentor on leave");
            } else {
                mc.setHappened(false);
            }
            connectRepo.save(mc);
        }
    }
}
