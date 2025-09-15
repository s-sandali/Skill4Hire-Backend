package com.se.skill4hire.service;

import com.se.skill4hire.employee.entity.Employee;
import com.se.skill4hire.employee.repository.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository repo;

    public EmployeeUserDetailsService(EmployeeRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee e = repo.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Employee not found"));
        return new User(e.getEmail(), e.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
    }
}
