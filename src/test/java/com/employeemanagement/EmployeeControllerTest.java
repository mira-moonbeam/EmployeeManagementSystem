package com.employeemanagement;

import com.employeemanagement.controller.EmployeeController;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Role;
import com.employeemanagement.model.TerminatedEmployee;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.service.EmployeeService;
import com.employeemanagement.service.RoleService;
import com.employeemanagement.service.TerminatedEmployeeService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class EmployeeControllerTest {

    @Mock
    private RoleService roleService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private TerminatedEmployeeService terminatedEmployeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void testAddEmployee() {
        Employee employee = new Employee("John", 50000L, 2);

        when(employeeService.addEmployee(any(Employee.class))).thenReturn(employee);

        Employee result = employeeController.addEmployee(employee);

        assertEquals("John", result.getName());
        assertEquals(50000L, result.getSalary());
        assertEquals(2, result.getGrade());
    }

    @Test
    public void testShowRoles() {
        Role role1 = new Role();
        role1.setRoleId(1);
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setRoleId(2);
        role2.setName("User");

        List<Role> roles = Arrays.asList(role1, role2);

        when(roleService.showRoles()).thenReturn(roles);

        List<Role> result = employeeController.showRoles();

        assertEquals(2, result.size());
        assertEquals("Admin", result.get(0).getName());
        assertEquals("User", result.get(1).getName());
    }


    @Test
    public void testShowEmployees() {
        // Prepare test data
        Employee john = new Employee("John", 60000L, 2);
        Employee jane = new Employee("Jane", 55000L, 1);
        Employee johnSecond = new Employee("John", 70000L, 2);

        List<Employee> allEmployees = Arrays.asList(john, jane, johnSecond);
        List<Employee> grade2Employees = Arrays.asList(john, johnSecond);

        // Mock repository methods
        when(employeeService.showEmployees(null, null, null, null)).thenReturn(allEmployees);
        when(employeeService.showEmployees("John", null, null, null)).thenReturn(Arrays.asList(john, johnSecond));
        when(employeeService.showEmployees(null, 1L, null, null)).thenReturn(Collections.singletonList(john));
        when(employeeService.showEmployees(null, 4L, null, null)).thenReturn(Collections.emptyList());
        when(employeeService.showEmployees("FakeName", null, null, null)).thenReturn(Arrays.asList());
        when(employeeService.showEmployees(null, null, (Arrays.asList(1L, 2L, 3L)), null)).thenReturn(allEmployees);
        when(employeeService.showEmployees(null, null, null, 2)).thenReturn(grade2Employees);

        // Test showEmployees All
        assertEquals(3, employeeController.showEmployees(null, null, null, null).size());

        // Test showEmployees by ID
        assertEquals("John", employeeController.showEmployees(null, 1L, null, null).get(0).getName());

        // Test showEmployees by Name
        assertEquals("John", employeeController.showEmployees("John", null, null, null).get(0).getName());
        assertEquals(2, employeeController.showEmployees("John", null, null, null).size());

        // Test showEmployees wrong ID and Name
        assertTrue(employeeController.showEmployees(null, 4L, null, null).isEmpty());
        assertTrue(employeeController.showEmployees("NotExist", null, null, null).isEmpty());

        // Test showEmployees with list of IDs
        assertEquals(3, employeeController.showEmployees(null, null, Arrays.asList(1L, 2L, 3L), null).size());

        // Test showEmployees by grade
        assertEquals(2, employeeController.showEmployees(null, null, null, 2).size());
    }

    @Test
    public void testShowTerminated() {
        // Prepare test data
        TerminatedEmployee terminatedJohn = new TerminatedEmployee();
        terminatedJohn.setName("Terminated John");
        terminatedJohn.setSalary(50000L);
        terminatedJohn.setGrade(1);

        TerminatedEmployee terminatedJane = new TerminatedEmployee();
        terminatedJane.setName("Terminated Jane");
        terminatedJane.setSalary(55000L);
        terminatedJane.setGrade(2);

        List<TerminatedEmployee> allTerminatedEmployees = Arrays.asList(terminatedJohn, terminatedJane);

        // Mock repository methods
        when(terminatedEmployeeService.showTerminated()).thenReturn(allTerminatedEmployees);

        // Test showTerminated
        List<TerminatedEmployee> result = employeeController.showTerminated();
        assertEquals(2, result.size());
        assertEquals("Terminated John", result.get(0).getName());
        assertEquals("Terminated Jane", result.get(1).getName());
    }

    @Test
    public void testUpdateEmployeeName() {
        // Given
        long id = 1L;
        Employee newDetails = new Employee("Jane", null, null);
        Employee updatedEmployee = new Employee("Jane", 50000L, 2);

        when(employeeService.updateEmployee(id, newDetails)).thenReturn(Optional.of(updatedEmployee));

        // When
        ResponseEntity<?> result = employeeController.updateEmployee(id, newDetails);

        // Then
        assertEquals("Jane", ((Employee) result.getBody()).getName());
        assertEquals(ResponseEntity.ok(updatedEmployee), result);
    }

    @Test
    public void testUpdateEmployeeSalary() {
        // Given
        long id = 1L;
        Employee newDetails = new Employee(null, 60000L, null);
        Employee updatedEmployee = new Employee("Jane", 60000L, 2);

        when(employeeService.updateEmployee(id, newDetails)).thenReturn(Optional.of(updatedEmployee));

        // When
        ResponseEntity<?> result = employeeController.updateEmployee(id, newDetails);

        // Then
        assertEquals(60000L, ((Employee) result.getBody()).getSalary());
        assertEquals(ResponseEntity.ok(updatedEmployee), result);
    }

    @Test
    public void testUpdateEmployeeGrade() {
        // Given
        long id = 1L;
        Employee newDetails = new Employee(null, null, 2);
        Employee updatedEmployee = new Employee("Jane", 60000L, 2);

        when(employeeService.updateEmployee(id, newDetails)).thenReturn(Optional.of(updatedEmployee));

        // When
        ResponseEntity<?> result = employeeController.updateEmployee(id, newDetails);

        // Then
        assertEquals(2, ((Employee) result.getBody()).getGrade());
        assertEquals(ResponseEntity.ok(updatedEmployee), result);
    }

    @Test
    public void testUpdateEmployeeAllFields() {
        // Given
        long id = 1L;
        Employee newDetails = new Employee("Jane", 60000L, 2);
        Employee updatedEmployee = new Employee("Jane", 60000L, 2);

        when(employeeService.updateEmployee(id, newDetails)).thenReturn(Optional.of(updatedEmployee));

        // When
        ResponseEntity<?> result = employeeController.updateEmployee(id, newDetails);

        // Then
        assertEquals("Jane", updatedEmployee.getName());
        assertEquals(60000L, updatedEmployee.getSalary());
        assertEquals(2, updatedEmployee.getGrade());
        assertEquals(ResponseEntity.ok(updatedEmployee), result);
    }

    @Test
    public void testUpdateEmployeeNotFound() {
        // Given
        long id = 1L;
        Employee newDetails = new Employee("Jane", 60000L, 3);
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> result = employeeController.updateEmployee(id, newDetails);

        // Then
        assertEquals(ResponseEntity.badRequest().body("Employee with id " + id + " does not exist."), result);
    }

    @Test
    public void testDeleteEmployee() {
        // Prepare test data
        List<Long> idsToDelete = Arrays.asList(1L, 2L, 3L);

        // Test deleteEmployee
        ResponseEntity<Void> result = employeeController.deleteEmployee(idsToDelete);

        verify(employeeService, times(1)).deleteEmployee(idsToDelete);

        assertEquals(ResponseEntity.ok().build(), result);
    }
}
