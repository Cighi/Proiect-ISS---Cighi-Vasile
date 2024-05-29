package org.iss.Services;

import org.iss.Domain.Employee;

import java.util.HashMap;
import java.util.Map;

public class EmployeeWatcher {
    private static final Map<String, Employee> loggedEmployees = new HashMap<>();
    public static Boolean loggedIn(Employee employee){
        return loggedEmployees.containsKey(employee.getUsername());
    }
    public static void login(Employee employee){
        loggedEmployees.put(employee.getUsername(),employee);
    }
    public static void logout(Employee employee){
        loggedEmployees.remove(employee.getUsername());
    }
}
