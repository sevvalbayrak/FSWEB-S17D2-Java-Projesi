package com.workintech.SpringDependecy.validation;

import com.workintech.SpringDependecy.model.Developer;

import java.util.Map;

public class DeveloperValidation {
    public static boolean isIdValid(int id){
        return !(id <= 0);
    }

    public static boolean isMapContainsKey(Map<Integer, Developer> developers, int id){
        return developers.containsKey(id);
    }

    public static boolean isDevelopersCredentialsValid(Developer developer){
        return !(developer.getId() <= 0 || developer.getName() == null || developer.getName().isEmpty()
                || developer.getSalary() > 25000);
    }
}
