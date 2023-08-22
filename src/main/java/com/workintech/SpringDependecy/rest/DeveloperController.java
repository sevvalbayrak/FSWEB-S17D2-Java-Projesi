package com.workintech.SpringDependecy.rest;


import com.workintech.SpringDependecy.mapping.DeveloperResponse;
import com.workintech.SpringDependecy.model.*;
import com.workintech.SpringDependecy.validation.DeveloperValidation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

import com.workintech.SpringDependecy.tax.Taxable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private Map<Integer, Developer> developers;
    private Taxable taxable;

    @Autowired
    public DeveloperController(@Qualifier("developerTax") Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init(){
        developers = new HashMap<>();
    }

    @GetMapping("/")
    public List<Developer> get(){
        return developers.values().stream().toList();
    }
    @GetMapping("/{id}")
    public DeveloperResponse getById(@PathVariable int id){
        if (!DeveloperValidation.isIdValid(id)){
            return new DeveloperResponse(developers.get(id), "Id is not valid", 400);
        }
        if (!developers.containsKey(id)){
            return new DeveloperResponse(developers.get(id), "Developer with given id is not exist: " +id, 400);
        }
        return new DeveloperResponse(developers.get(id), "Success", 200);
    }

    @PostMapping("/")
    public DeveloperResponse save(@RequestBody Developer developer){
        Developer savedDeveloper = DeveloperFactory.createDeveloper(developer, taxable);
        if (savedDeveloper == null){
            return new DeveloperResponse(null, "Developer with given experience is not valid", 400);
        }
        if (developers.containsKey(developer.getId())){
            return new DeveloperResponse(null, "Developer with given already exist: " + developer.getId(), 400);
        }
        if (!DeveloperValidation.isDevelopersCredentialsValid(developer)){
            return new DeveloperResponse(null, "Developer credentials are not valid", 400);
        }
        developers.put(developer.getId(), savedDeveloper);
        return new DeveloperResponse(developers.get(developer.getId()),"Success", 201);


//        double salaryTax = developer.getSalary();
//        if (developer.getExperience() == Experience.JUNIOR){
//            salaryTax -= taxable.getSimpleTaxRate();
//        } else if (developer.getExperience() == Experience.MID){
//            salaryTax -= taxable.getMiddleTaxRate();
//        } else if (developer.getExperience() == Experience.SENIOR) {
//            salaryTax -= developer.getSalary() * taxable.getUpperTaxRate();
//        }
//        developer.setSalary(salaryTax);
    }

    @PutMapping("{id}")
    public DeveloperResponse update(@PathVariable int id, @RequestBody Developer developer){
        if (!developers.containsKey(id)){
            return new DeveloperResponse(null, "Developer with given already exist: " + id, 400);
        }
        developer.setId(id);
        Developer updateDeveloper = DeveloperFactory.createDeveloper(developer,taxable);

        if (updateDeveloper == null){
            return new DeveloperResponse(null, "Developer with given experience is not valid", 400);
        }
        developers.put(id, updateDeveloper);
        return new DeveloperResponse(developers.get(id), "Success", 200);
    }

    @DeleteMapping("{id}")
    public DeveloperResponse remove(@PathVariable int id){
        if (!developers.containsKey(id)){
            return new DeveloperResponse(null, "Developer with given already exist: " + id, 400);
        }
        Developer developer = developers.get(id);
        developers.remove(id);
        return new DeveloperResponse(developers.get(id), "Success", 200);
    }
}
