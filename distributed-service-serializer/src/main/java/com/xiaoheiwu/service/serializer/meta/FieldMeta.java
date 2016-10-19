package com.xiaoheiwu.service.serializer.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.xiaoheiwu.service.serializer.annotation.Required;
import com.xiaoheiwu.service.serializer.annotation.Transient;

public class FieldMeta {
	private String fieldName;
	private IObjectMeta objectMeta;
	private Method getMethod;
	private Method setMethod;
	private Set<Annotation> annotations=new HashSet<Annotation>();
	public String getFieldName() {
		return fieldName;
	}
	public IObjectMeta getObjectMeta() {
		return objectMeta;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public void setObjectMeta(IObjectMeta objectMeta) {
		this.objectMeta = objectMeta;
	}
	public Set<Annotation> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}
	public void setAnnotations(Annotation[] annotations) {
		if(annotations==null)return;
		Set<Annotation> annotationSet=new HashSet<Annotation>();
		for(Annotation annotation: annotations){
			annotationSet.add(annotation);
		}
		this.setAnnotations(annotationSet);
	}
	public boolean isRequired(){
		Annotation annotation = getMatchAnnotation(Required.class);
		if(annotation==null)return false;
		Required required=(Required)annotation;
		return required.value();
	}
	public boolean isAnnotationIgnore(){
		Annotation annotation = getMatchAnnotation(Transient.class);
		if(annotation!=null)return true;
		return false;
	}
	protected Annotation getMatchAnnotation(Class clazz){
		Iterator<Annotation> it = this.annotations.iterator();
		while (it.hasNext()){
			Annotation annotation=it.next();
			if(annotation.annotationType().equals(clazz)){
				return annotation;
			}
		}
		return null;
	}
	public <T> void setFieldValue(Object object,T value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		setMethod.invoke(object, value);
	}
	public Object getFieldValue(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return getMethod.invoke(object);
	}
	public void setGetMethod(Method getMethod) {
		this.getMethod = getMethod;
	}
	public void setSetMethod(Method setMethod) {
		this.setMethod = setMethod;
	}
	
}
