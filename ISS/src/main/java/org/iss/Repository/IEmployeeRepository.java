package org.iss.Repository;

import org.iss.Domain.Employee;

public interface IEmployeeRepository extends Repository<Long, Employee> {
    Employee findByCredentials(String username,String password);
}
