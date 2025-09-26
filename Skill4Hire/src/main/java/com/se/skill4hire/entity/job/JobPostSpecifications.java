package com.se.skill4hire.entity.job;

import org.springframework.data.jpa.domain.Specification;

public class JobPostSpecifications {
    public static Specification<JobPost> isActive() {
        return (root, query, cb) -> cb.equal(root.get("status"), JobPost.JobStatus.ACTIVE);
    }

    public static Specification<JobPost> titleOrDescriptionContains(String keyword) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
        );
    }

    public static Specification<JobPost> typeEquals(String type) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("type")), type.toLowerCase());
    }

    public static Specification<JobPost> locationEquals(String location) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("location")), location.toLowerCase());
    }

    public static Specification<JobPost> salaryAtLeast(Double minSalary) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salary"), minSalary);
    }

    public static Specification<JobPost> experienceAtMost(Integer maxExperience) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("experience"), maxExperience);
    }
}