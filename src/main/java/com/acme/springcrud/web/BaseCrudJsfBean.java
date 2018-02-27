/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.springcrud.web;

import com.acme.springcrud.service.CrudService;
import com.acme.springcrud.util.ReflectionUtil;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 *
 * @author eu
 */
public abstract class BaseCrudJsfBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    protected List<T> crudList;
    protected T crudObj;
    @ManagedProperty(value = "#{crudService}")
    protected CrudService crudService;
    @ManagedProperty("#{i18n}")
    private ResourceBundle i18n;

    public void setI18n(ResourceBundle i18n) {
        this.i18n = i18n;
    }

    public T getCrudObj() {
        return crudObj;
    }

    public void setCrudObj(T crudObj) {
        this.crudObj = crudObj;
    }

    public List<T> getCrudList() {
        return crudList;
    }

    public void setCrudList(List<T> crudList) {
        this.crudList = crudList;
    }

    public void setCrudService(CrudService crudService) {
        this.crudService = crudService;
    }

    protected void listAll() {
        crudList = crudService.findAll(getCrudClass());
    }

    public Class getCrudClass() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) pt.getActualTypeArguments()[0];
    }

    @PostConstruct
    public void init() {
        listAll();
        createNewCrudObj();
    }

    public void save() {
        saveCrudObj();
        showSuccessSaveMessage();        
        postSave();
    }

    protected void saveCrudObj() {
        crudObj = crudService.save(crudObj);
    }
    
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }    
    
    public void info(String message) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }    

    protected void postSave() {
        reloadCrudObj();
        reloadCrudList();
    }

    protected void showSuccessSaveMessage() {
        info(i18n.getString("crud.successsave"));
    }

    protected void reloadCrudList() {
        listAll();
    }

    protected void reloadCrudObj() {        
        crudObj = (T) crudService.find(getCrudClass(), ReflectionUtil.getPKValue(crudObj));
    }

    public void createNewCrudObj() {        
        try {            
            crudObj = (T) getCrudClass().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void editCrudObj(T obj) {
        crudObj = obj;
    }
    
    public void deleteCrudObj(T obj) {        
        doDeleteCrudObj(obj);
        showSuccessDeleteMessage();
        postDelete();        
    }

    protected void doDeleteCrudObj(T obj) {
        crudService.delete(obj);
    }

    protected void showSuccessDeleteMessage() {
        info(i18n.getString("crud.successdelete"));
    }

    protected void postDelete() {
        reloadCrudList();
        createNewCrudObj();
    }

}
