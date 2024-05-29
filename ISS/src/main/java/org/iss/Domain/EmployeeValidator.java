package org.iss.Domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeValidator implements Validator<Employee>{
    @Override
    public void validate(Employee employee) throws DomainException {
        String error="";
        Pattern pattern = Pattern.compile("[A-Z][a-z]+ [A-Z][a-z]+(-[A-Z][a-z]+)?");
        Matcher matcher = pattern.matcher(employee.getName());
        if(employee.getUsername().length()<3)
            error+="Invalid Username\n";
        if(!matcher.find())
            error+="Invalid Name\n";
        if(employee.getPassword().isEmpty())
            error+="Invalid Password";
        if(!error.isEmpty())
            throw new DomainException(error);
    }
}
