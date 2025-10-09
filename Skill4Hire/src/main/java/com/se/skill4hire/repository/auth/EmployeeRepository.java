package com.se.skill4hire.repository.auth;

import com.se.skill4hire.entity.auth.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Employee findByEmail(String email);
}