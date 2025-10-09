package com.se.skill4hire.migration;

// ...existing code...

import com.se.skill4hire.entity.CompanyProfile;
import com.se.skill4hire.entity.EmployeeProfile;
import com.se.skill4hire.entity.Notification;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.repository.ApplicationRepository;
import com.se.skill4hire.repository.CandidatePreferredJobRoleRepository;
import com.se.skill4hire.repository.profile.CompanyProfileRepository;
import com.se.skill4hire.repository.profile.EmployeeProfileRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("migration")
public class MigrationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MigrationRunner.class);

    private final JdbcTemplate jdbcTemplate;
    private final JobPostRepository jobPostRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final ApplicationRepository applicationRepository;
    private final CandidatePreferredJobRoleRepository candidatePreferredJobRoleRepository;

    @Value("${migration.batch-size:500}")
    private int batchSize;

    public MigrationRunner(JdbcTemplate jdbcTemplate,
                           JobPostRepository jobPostRepository,
                           CompanyProfileRepository companyProfileRepository,
                           EmployeeProfileRepository employeeProfileRepository,
                           ApplicationRepository applicationRepository,
                           CandidatePreferredJobRoleRepository candidatePreferredJobRoleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jobPostRepository = jobPostRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.applicationRepository = applicationRepository;
        this.candidatePreferredJobRoleRepository = candidatePreferredJobRoleRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Starting migration (profile 'migration') with H2 URL provided in properties...");

        migrateJobPosts();
        migrateCompanies();
        migrateEmployeeProfiles();
        migrateApplications();
        migrateCandidatePreferredJobRoles();

        log.info("Migration finished.");
    }

    private void migrateJobPosts() {
        String table = "job_post";
        String sql = "SELECT * FROM " + table;
        try {
            List<JobPost> rows = jdbcTemplate.query(sql, new RowMapper<JobPost>() {
                @Override
                public JobPost mapRow(ResultSet rs, int rowNum) throws SQLException {
                    JobPost j = new JobPost();
                    j.setId(getString(rs, "id", "_id"));
                    j.setTitle(getString(rs, "title"));
                    j.setDescription(getString(rs, "description"));
                    j.setType(getString(rs, "type"));
                    j.setLocation(getString(rs, "location"));
                    j.setSalary(getDouble(rs, "salary"));
                    j.setExperience(getInteger(rs, "experience"));
                    j.setDeadline(getDateAsLocalDate(rs, "deadline"));
                    j.setCompanyId(getString(rs, "company_id", "companyId", "company"));
                    String status = getString(rs, "status");
                    if (status != null) {
                        try { j.setStatus(JobPost.JobStatus.valueOf(status)); } catch (Exception ignored) {}
                    }
                    j.setCreatedAt(getTimestampAsLocalDateTime(rs, "created_at", "createdAt"));
                    j.setUpdatedAt(getTimestampAsLocalDateTime(rs, "updated_at", "updatedAt"));
                    return j;
                }
            });

            saveInBatches(jobPostRepository::saveAll, rows, "job_posts");
        } catch (Exception e) {
            log.warn("Skipping job posts migration - table '{}' not found or error: {}", table, e.getMessage());
        }
    }

    private void migrateCompanies() {
        String table = "company_profile";
        String sql = "SELECT * FROM " + table;
        try {
            List<CompanyProfile> rows = jdbcTemplate.query(sql, new RowMapper<CompanyProfile>() {
                @Override
                public CompanyProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                    CompanyProfile c = new CompanyProfile();
                    c.setId(getString(rs, "id", "_id"));
                    c.setCompanyName(getString(rs, "company_name", "companyName", "name"));
                    c.setContactNumber(getString(rs, "contact_number", "contactNumber", "phone"));
                    c.setAddress(getString(rs, "address"));
                    c.setEmail(getString(rs, "email"));
                    c.setWebsite(getString(rs, "website"));
                    c.setFacebook(getString(rs, "facebook"));
                    c.setTwitter(getString(rs, "twitter"));
                    c.setLinkedin(getString(rs, "linkedin"));
                    c.setNotificationsEnabled(getBoolean(rs, "notifications_enabled", "notificationsEnabled"));
                    c.setLogoUrl(getString(rs, "logo_url", "logoUrl"));
                    c.setPassword(getString(rs, "password"));
                    return c;
                }
            });

            saveInBatches(companyProfileRepository::saveAll, rows, "company_profile");
        } catch (Exception e) {
            log.warn("Skipping companies migration - table '{}' not found or error: {}", table, e.getMessage());
        }
    }

    private void migrateEmployeeProfiles() {
        String table = "employee_profile";
        String sql = "SELECT * FROM " + table;
        try {
            List<EmployeeProfile> rows = jdbcTemplate.query(sql, new RowMapper<EmployeeProfile>() {
                @Override
                public EmployeeProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                    EmployeeProfile p = new EmployeeProfile();
                    p.setId(getString(rs, "id", "_id"));
                    p.setUserId(getString(rs, "user_id", "userId"));
                    p.setName(getString(rs, "name"));
                    p.setEmail(getString(rs, "email"));
                    p.setPhoneNumber(getString(rs, "phone_number", "phoneNumber"));
                    p.setLocation(getString(rs, "location"));
                    p.setDateOfBirth(getDateAsLocalDate(rs, "date_of_birth", "dob"));
                    p.setTitle(getString(rs, "title"));
                    p.setHeadline(getString(rs, "headline"));
                    String skills = getString(rs, "skills");
                    if (skills != null) {
                        List<String> list = new ArrayList<>();
                        for (String s : skills.split(",")) {
                            if (!s.trim().isEmpty()) list.add(s.trim());
                        }
                        p.setSkills(list);
                    }
                    p.setEducation(getString(rs, "education"));
                    p.setExperience(getString(rs, "experience"));
                    p.setNotificationPreferences(getString(rs, "notification_preferences", "notificationPreferences"));
                    p.setProfilePicturePath(getString(rs, "profile_picture_path", "profilePicturePath"));
                    p.setProfileCompleteness(getDouble(rs, "profile_completeness"));
                    p.setCreatedAt(getTimestampAsLocalDateTime(rs, "created_at", "createdAt"));
                    p.setUpdatedAt(getTimestampAsLocalDateTime(rs, "updated_at", "updatedAt"));
                    return p;
                }
            });

            saveInBatches(employeeProfileRepository::saveAll, rows, "employee_profiles");
        } catch (Exception e) {
            log.warn("Skipping employee profiles migration - table '{}' not found or error: {}", table, e.getMessage());
        }
    }

    private void migrateApplications() {
        String table = "application";
        String sql = "SELECT * FROM " + table;
        try {
            List<com.se.skill4hire.entity.Application> rows = jdbcTemplate.query(sql, new RowMapper<com.se.skill4hire.entity.Application>() {
                @Override
                public com.se.skill4hire.entity.Application mapRow(ResultSet rs, int rowNum) throws SQLException {
                    com.se.skill4hire.entity.Application a = new com.se.skill4hire.entity.Application();
                    // Preserve original id if needed; removed for compile-safety (Mongo will generate ids on insert)
                    // String originalId = getString(rs, "id", "_id");
                    a.setCandidateId(getString(rs, "candidate_id", "candidateId"));
                    a.setCompanyId(getString(rs, "company_id", "companyId"));
                    a.setCompanyName(getString(rs, "company_name", "companyName"));
                    String status = getString(rs, "status");
                    if (status != null) {
                        try { a.setStatus(com.se.skill4hire.entity.Application.ApplicationStatus.valueOf(status)); } catch (Exception ignored) {}
                    }
                    a.setAppliedAt(getTimestampAsLocalDateTime(rs, "applied_at", "appliedAt"));
                    a.setRejectionReason(getString(rs, "rejection_reason", "rejectionReason"));
                    a.setDecisionAt(getTimestampAsLocalDateTime(rs, "decision_at", "decisionAt"));
                    a.setDecidedBy(getString(rs, "decided_by", "decidedBy"));
                    a.setJobPostId(getString(rs, "job_post_id", "jobPostId"));
                    return a;
                }
            });

            saveInBatches(applicationRepository::saveAll, rows, "applications");
        } catch (Exception e) {
            log.warn("Skipping applications migration - table '{}' not found or error: {}", table, e.getMessage());
        }
    }

    private void migrateCandidatePreferredJobRoles() {
        String table = "candidate_preferred_job_role";
        String sql = "SELECT * FROM " + table;
        try {
            List<Notification.CandidatePreferredJobRole> rows = jdbcTemplate.query(sql, new RowMapper<Notification.CandidatePreferredJobRole>() {
                @Override
                public Notification.CandidatePreferredJobRole mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Notification.CandidatePreferredJobRole n = new Notification.CandidatePreferredJobRole();
                    n.setId(getString(rs, "id", "_id"));
                    n.setCandidateId(getString(rs, "candidate_id", "candidateId"));
                    n.setJobRole(getString(rs, "job_role", "jobRole"));
                    return n;
                }
            });

            saveInBatches(candidatePreferredJobRoleRepository::saveAll, rows, "candidate_preferred_job_role");
        } catch (Exception e) {
            log.warn("Skipping candidate preferred job roles migration - table '{}' not found or error: {}", table, e.getMessage());
        }
    }

    // Helper to save in batches with a simple save function
    private <T> void saveInBatches(java.util.function.Consumer<List<T>> saver, List<T> items, String name) {
        if (items == null || items.isEmpty()) {
            log.info("No rows found for {}", name);
            return;
        }
        log.info("Found {} rows for {}. Saving in batches of {}", items.size(), name, batchSize);
        int i = 0;
        while (i < items.size()) {
            int end = Math.min(i + batchSize, items.size());
            List<T> batch = items.subList(i, end);
            saver.accept(new ArrayList<>(batch));
            i = end;
            log.info("Saved batch to {} ({} / {})", name, i, items.size());
        }
    }

    // Utility getters that try multiple column names and handle nulls
    private String getString(ResultSet rs, String... names) {
        for (String n : names) {
            try {
                String v = rs.getString(n);
                if (v != null) return v;
            } catch (SQLException ignored) {}
        }
        return null;
    }

    private Integer getInteger(ResultSet rs, String... names) {
        for (String n : names) {
            try {
                int v = rs.getInt(n);
                if (!rs.wasNull()) return v;
            } catch (SQLException ignored) {}
        }
        return null;
    }

    private Double getDouble(ResultSet rs, String... names) {
        for (String n : names) {
            try {
                double v = rs.getDouble(n);
                if (!rs.wasNull()) return v;
            } catch (SQLException ignored) {}
        }
        return null;
    }

    private boolean getBoolean(ResultSet rs, String... names) {
        for (String n : names) {
            try {
                boolean v = rs.getBoolean(n);
                if (!rs.wasNull()) return v;
            } catch (SQLException ignored) {}
        }
        return false;
    }

    private LocalDate getDateAsLocalDate(ResultSet rs, String... names) {
        for (String n : names) {
            try {
                java.sql.Date d = rs.getDate(n);
                if (d != null) return d.toLocalDate();
            } catch (SQLException ignored) {}
        }
        return null;
    }

    private LocalDateTime getTimestampAsLocalDateTime(ResultSet rs, String... names) {
        for (String n : names) {
            try {
                Timestamp t = rs.getTimestamp(n);
                if (t != null) return t.toLocalDateTime();
            } catch (SQLException ignored) {}
        }
        return null;
    }
}
