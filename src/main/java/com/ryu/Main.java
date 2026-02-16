package com.ryu;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

class ToySource {
    private String username = "ryu2710";
}

class ToyTarget {
    private String username;

    public String getUsername() {
        return username;
    }
}

class RyuMapStruct {
    public static <T> T toDTO(Object entity, Class<T> dtoClazz) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> entityClazz = entity.getClass();
        T expectDTO = dtoClazz.getDeclaredConstructor().newInstance();

        for(Field entityField: entityClazz.getDeclaredFields()) {
            try {
                Field dtoField = dtoClazz.getDeclaredField(entityField.getName());
                entityField.setAccessible(true);
                dtoField.setAccessible(true);
                Object value = entityField.get(entity);
                dtoField.set(expectDTO, value);
            } catch (NoSuchFieldException e) {
                System.out.println("No such field exception: " + e);
            } catch (IllegalArgumentException e) {
                System.out.println("Type mismatch or security block: " + e);
            } catch (IllegalAccessException e) {
                System.out.println("Access Exception due to private: " + e);
            }
        }

        return expectDTO;
    }
}


public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        ToySource toySource = new ToySource();
        ToyTarget toyTarget = RyuMapStruct.toDTO(toySource, ToyTarget.class);

        System.out.println(toyTarget.getUsername());
    }
}