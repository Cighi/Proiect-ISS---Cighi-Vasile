package org.iss.Services;

import org.iss.Repository.IBugRepository;
import org.iss.Repository.EmployeeRepository;
import org.iss.Repository.IEmployeeRepository;

public class ServiceContainer {
    private static EmployeeService employeeService;
    private static BugService bugService;
    private ServiceContainer(){

    }

    public static EmployeeService getEmployeeService() {
        return employeeService;
    }

    public static void setEmployeeService(EmployeeService employeeService) {
        ServiceContainer.employeeService = employeeService;
    }

    public static BugService getBugService() {
        return bugService;
    }

    public static void setBugService(BugService bugService) {
        ServiceContainer.bugService = bugService;
    }
}
