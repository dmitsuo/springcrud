/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.springcrud.web.exception;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author davi
 */
public class JsfExceptionHandler extends ExceptionHandlerWrapper {
    private ExceptionHandler wrapped;

    public JsfExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        //Iterate over all unhandeled exceptions
        Iterator i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            //obtain throwable object
            Throwable t = context.getException();

            //here you do what ever you want with exception
            try {                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ExceptionUtils.getRootCauseMessage(t), null));
            } finally {
                //after exception is handeled, remove it from queue
                i.remove();
            }
        }
        //let the parent handle the rest
        getWrapped().handle();
    }
}
