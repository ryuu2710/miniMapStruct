package com.ryu;

import com.ryu.annotations.MapFrom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

class ToySource {
    private String username = "ryu2710";
    private String first_name = "Loi";
    private Date birth_date = new Date();
    private BigDecimal balance = new BigDecimal("500.0");
}

class ToyTarget {
    private String username;

    @MapFrom(value = "first_name")
    private String firstName;

    @MapFrom(value = "birth_date")
    private String birthDate;

    private BigDecimal balance;

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return "ToyTarget{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", balance=" + balance +
                '}';
    }
}

class RyuMapStruct {
    public static <T> T toDTO(Object entity, Class<T> dtoClazz) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> entityClazz = entity.getClass();
        T expectDTO = dtoClazz.getDeclaredConstructor().newInstance();

        // inverted loop all field of dto class
        for(Field dtoField: dtoClazz.getDeclaredFields()) {
            try {
                MapFrom stickyNote = dtoField.getDeclaredAnnotation(MapFrom.class);
                String searchName = (stickyNote != null) ? stickyNote.value() : dtoField.getName();

                // search field which got from annotation and then search in entity field
                Field entityField = entityClazz.getDeclaredField(searchName);
                entityField.setAccessible(true);
                dtoField.setAccessible(true);

                Object value = entityField.get(entity);

                if(value instanceof Date dateValue && dtoField.getType().equals(String.class)) {
                    value = dateValue.toString();
                }
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

        System.out.println(toyTarget.toString());
    }
}