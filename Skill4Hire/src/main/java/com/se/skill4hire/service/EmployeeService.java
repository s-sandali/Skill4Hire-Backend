package com.se.skill4hire.service;

public class EmployeeService {
}
package com.se.skill4hire.employee.service;

import com.se.skill4hire.employee.dto.EmployeeDto;
import com.se.skill4hire.employee.dto.EmployeeUpdateRequest;
import com.se.skill4hire.employee.entity.Employee;
import com.se.skill4hire.employee.exception.ApiException;
import com.se.skill4hire.employee.repository.EmployeeRepository;
import org.springframework.data.domain.*;
        import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

