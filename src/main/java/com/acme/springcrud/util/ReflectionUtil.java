/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.springcrud.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

/**
 *
 * @author eu
 */
public class ReflectionUtil {    
    
    public static boolean isNewObject(Object obj) {
        Object pkValue = getPKValue(obj);
        if (pkValue == null) {
            return true;
        }
        if (pkValue instanceof Integer) {
            return ((Integer)pkValue) == 0;
        }
        if (pkValue instanceof Long) {
            return ((Long)pkValue) == 0;
        }
        return false;
    }    
    
    public static Object getPKValue(Object t) {
        try {
            Field pkField = getPKField(t);
            pkField.setAccessible(true);
            return pkField.get(t);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }    
    
    public static Field getPKField(Object t) {
        Field pkField = null;
        Field[] fields = getAllFields(t.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
                pkField = field;
            }
        }
        return pkField;
    }    

    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<Field>();
        for (Class<?> superThing : getAllSupers(clazz)) {
            for (Field f : superThing.getDeclaredFields()) {
                allFields.add(f);
            }
        }
        return allFields.toArray(new Field[allFields.size()]);
    }

    private static Set<Class<?>> getAllSupers(Class<?> clazz) {
        Set<Class<?>> result = new HashSet<Class<?>>();
        if (clazz != null) {
            result.add(clazz);
            result.addAll(getAllSupers(clazz.getSuperclass()));
            for (Class<?> interfaceClass : clazz.getInterfaces()) {
                result.addAll(getAllSupers(interfaceClass));
            }
        }
        return result;
    }    
    
}
