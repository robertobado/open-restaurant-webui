package net.openrally.entity;

import java.lang.reflect.Field;

import net.openrally.annotations.EntityId;

public abstract class Entity {
	
	public Long getId(){
		Field idField = findIdField();
		
		try {
			return (Long) idField.get(this);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		return null;
		
		
	}
	
	public void setId(Long value){
		Field idField = findIdField();
		try {
			idField.set(this, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private Field findIdField(){
		Field[] entityFields = this.getClass().getDeclaredFields();
		
		for(Field field : entityFields){
			if(field.isAnnotationPresent(EntityId.class)){
				field.setAccessible(true);
				return field;
			}
		}
		
		throw new RuntimeException("Class " + this.getClass().getName() + " does not have "+ EntityId.class.getName() +" annotation markup");
	}

}
