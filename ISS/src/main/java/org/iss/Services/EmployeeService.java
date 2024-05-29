package org.iss.Services;

import org.iss.Domain.Employee;
import org.iss.Domain.DomainException;
import org.iss.Domain.Role;
import org.iss.Domain.Validator;
import org.iss.Repository.Encrypt;
import org.iss.Repository.IEmployeeRepository;
import org.iss.Services.Utils.Observable;
import org.iss.Services.Utils.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmployeeService implements Observable {
    final private IEmployeeRepository employeeRepository;
    final private Validator<Employee> employeeValidator;
    private final List<Observer> observers = new ArrayList<>();


    public EmployeeService(IEmployeeRepository employeeRepository,Validator<Employee> validator) {
        this.employeeRepository = employeeRepository;
        this.employeeValidator = validator;

    }

    public void addEmployee(String name, String username, String password, Role role) throws DomainException {
        password = Encrypt.passwordEncrypt(password);
        Employee employee = new Employee(username, password, role);
        employee.setName(name);
        employeeValidator.validate(employee);
        employeeRepository.add(employee);
        notifyObservers();
    }

    public Collection<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Boolean deleteEmployee(Employee employee) {
        Boolean deleted = employeeRepository.delete(employee.getId());
        notifyObservers();
        return deleted;
    }

    public void updateEmployee(Employee employee,String name,String username,Role role) throws DomainException{
        employee.setRole(role);
        employee.setUsername(username);
        employee.setName(name);
        employeeValidator.validate(employee);
        employeeRepository.update(employee);
        notifyObservers();
    }
    public Employee findEmployeeByCredentials(String username,String password){
        password = Encrypt.passwordEncrypt(password);
        return employeeRepository.findByCredentials(username,password);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
