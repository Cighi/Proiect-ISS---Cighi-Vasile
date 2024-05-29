package org.iss.Services;

import org.iss.Domain.*;
import org.iss.Repository.IBugRepository;
import org.iss.Services.Utils.Observable;
import org.iss.Services.Utils.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BugService implements Observable {
    private final IBugRepository bugRepository;
    private final Validator<Bug> bugValidator;
    private final List<Observer> observers = new ArrayList<>();


    public BugService(IBugRepository bugRepository, Validator<Bug> bugValidator) {
        this.bugRepository = bugRepository;
        this.bugValidator = bugValidator;
    }
    public void addBug(String name,String description) throws DomainException {
        Bug bug = new Bug(name,description);
        bugValidator.validate(bug);
        bugRepository.add(bug);
        notifyObservers();
    }
    public Collection<Bug> getAllBugs(){
        return bugRepository.findAll();
    }
    public void updateBug(Bug bug,String newName,String newDescription) throws DomainException{
        bug.setDescription(newDescription);
        bug.setName(newName);
        bugValidator.validate(bug);
        bugRepository.update(bug);
        notifyObservers();
    }
    public Boolean deleteBug(Bug bug){
        Boolean deleted = bugRepository.delete(bug.getId());
        notifyObservers();
        return deleted;
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
