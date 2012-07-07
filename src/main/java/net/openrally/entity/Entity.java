package net.openrally.entity;

import java.lang.reflect.Field;

import net.openrally.annotations.EntityId;

public abstract class Entity {
	
	public Long getId(){
		
		Field[] entityFields = this.getClass().getDeclaredFields();
		
		for(Field field : entityFields){
			if(field.isAnnotationPresent(EntityId.class)){
				field.setAccessible(true);
				try {
					return (Long) field.get(this);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

}
