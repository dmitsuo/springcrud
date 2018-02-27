/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.springcrud.web;

import com.acme.springcrud.model.Person;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author eu
 */
@ManagedBean
@ViewScoped
public class PersonJsfBean extends BaseCrudJsfBean<Person> {

    public Person getPerson() {
        return getCrudObj();
    }

    public void setPerson(Person person) {
        setCrudObj(person);
    }
    
    public List<Person> getPeople() {
        return getCrudList();
    }

    public void setPeople(List<Person> people) {
        setCrudList(people);
    }    
}
