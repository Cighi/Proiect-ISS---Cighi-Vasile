package org.iss.Domain;

public class BugValidator implements Validator<Bug>{
    @Override
    public void validate(Bug bug) throws DomainException {
        String error = "";
        if(bug.getDescription().length()<20)
            error += "Description not long enough";
        if(!error.isEmpty())
            throw new DomainException(error);
    }
}
