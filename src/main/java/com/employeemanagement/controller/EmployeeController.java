package com.employeemanagement.controller;  

import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Role;
import com.employeemanagement.model.TerminatedEmployee;

import com.employeemanagement.service.EmployeeService;
import com.employeemanagement.service.RoleService;
import com.employeemanagement.service.TerminatedEmployeeService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;  
@RestController  
public class EmployeeController
{  
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private TerminatedEmployeeService terminatedEmployeeService;

    //*********** CREATE ***********//

    // * Example Call: curl -X POST http://localhost:8080/add-employee -H "Content-Type: application/json" -d '{"name": "John Doe", "salary": 60000, "grade": 2}'
    // Input: Employee
    // Output: Employee
    // Adds a new Employee to the Employee table
    @PostMapping("/add-employee")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    //*********** READ ***********//

    // * Example Call: curl http://localhost:8080/show-roles
    // Input: None
    // Output: List<Role>
    // Endpoint that returns a list of ALL Roles
    @GetMapping("/show-roles")
    public List<Role> showRoles() {
        return roleService.showRoles();
    }

    // * Example Call: curl http://localhost:8080/show-terminated
    // Input: None
    // Output: List<TerminatedEmployee>
    // Endpoint that returns a list of ALL Terminated Employees
    @GetMapping("/show-terminated")
    public List<TerminatedEmployee> showTerminated() {
        return terminatedEmployeeService.showTerminated();
    }

    // * Example Call: curl http://localhost:8080/show-employees
    // *               curl http://localhost:8080/show-employees?name=John%20Doe
    // *               curl http://localhost:8080/show-employees?id=1
    // *               curl http://localhost:8080/show-employees?ids=1,2,3
    // *               curl http://localhost:8080/show-employees?grade=2
    // Input: Optional Request Parameters - name (String), id (Long), ids (List<Long>), grade (Integer)
    // Output: List<Employee>
    // Endpoint that returns a filtered list of Employees based on given criteria: name, id, ids, or grade
    @GetMapping("/show-employees")
    public List<Employee> showEmployees(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) Integer grade) {
        return employeeService.showEmployees(name, id, ids, grade);
    }

    //*********** UPDATE ***********//

    // * Example Call: curl -X PUT "http://localhost:8080/update-employee?employee_id=1" -H "Content-Type: application/json" -d '{"name": "Jane Doe", "grade": 1}'
    // Input:
    // Query Parameter: employee_id (long) - The ID of the employee to update.
    // Request Body: JSON Object
    //    name (String, Optional) - The new name for the employee.
    //    salary (Long, Optional) - The new salary for the employee.
    //    grade (Integer, Optional) - The new grade for the employee.

    // Output:
    // Success: HTTP 200 (OK)
    //     Response Body: Updated Employee object in JSON format.
    // Failure: HTTP 400 (Bad Request)
    //     Response Body: Error message indicating the employee does not exist.
    @PutMapping("/update-employee")
    public ResponseEntity<?> updateEmployee(@RequestParam(required = true) long employee_id, @RequestBody Employee newEmployeeDetails) {
        Optional<Employee> updatedEmployee = employeeService.updateEmployee(employee_id, newEmployeeDetails);
        if (updatedEmployee.isPresent()) {
            return ResponseEntity.ok(updatedEmployee.get());
        } else {
            return ResponseEntity.badRequest().body("Employee with id " + employee_id + " does not exist.");
        }
    }

    //*********** DELETE ***********//

    // * Example Call: curl -X DELETE http://localhost:8080/delete-employee -H "Content-Type: application/json" -d "[2,3,4]"
    // Input: List<Long>
    // Output: 200 OK
    // Moves Employee(s) from Employee table to Terminated_Employee table
    @DeleteMapping("/delete-employee")
    public ResponseEntity<Void> deleteEmployee(@RequestBody List<Long> ids) {
        employeeService.deleteEmployee(ids);
        return ResponseEntity.ok().build();
    }
}