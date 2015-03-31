package org.bricks.annotation.processor;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.SourceVersion;
import javax.tools.JavaFileObject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.bricks.annotation.EventHandle;

import java.util.List;

@SupportedAnnotationTypes("org.bricks.annotation.EventHandle")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class EventHandleProcessor extends CreateSourceProcessor{
	
	private final String packageName = "org.bricks.engine.event.handler";
	private final String registerClassName = "EventHandlerRegistrator";

	@Override
	public boolean process(Set<? extends TypeElement> arg0, RoundEnvironment roundEnv) {

		Set<Map<String, Object>> handlers = new HashSet<Map<String, Object>>();
		Map<String, Integer> handlerDublicates = new HashMap<String, Integer>();
		for(Element element : roundEnv.getElementsAnnotatedWith(EventHandle.class)){
			if(element.getKind() == ElementKind.METHOD){
				ExecutableElement ee = (ExecutableElement) element;
				List<? extends VariableElement> params = ee.getParameters();
				VariableElement ve = params.get(0);
				
				String eventClassName = this.stripTemplates(ve.asType().toString());
				
				Element te = element.getEnclosingElement();
				if(te.getKind() == ElementKind.CLASS){
					String targetClassName = this.stripTemplates(te.asType().toString());
					EventHandle eh = element.getAnnotation(EventHandle.class);

					String targetSimpleName = this.fetchSimpleClassName(targetClassName);
					String eventSimpleName = this.fetchSimpleClassName(eventClassName);

					String handlerName = targetSimpleName + eventSimpleName + "Handler";
					if(handlerDublicates.containsKey(handlerName)){
						int num = handlerDublicates.get(handlerName) + 1;
						handlerDublicates.put(handlerName, num);
						handlerName += "_" + num;
					}else{
						handlerDublicates.put(handlerName, 0);
					}
					
					
	                Map<String, Object> handlerData = new HashMap<String, Object>();
	                handlerData.put("targetClassName", targetClassName);
	                handlerData.put("eventClassName", eventClassName);
	                handlerData.put("handlerName", handlerName);
	                handlerData.put("handleMethod", ee.getSimpleName().toString());
	                handlerData.put("packageName", packageName);
	                
	                handlerData.put("targetSimpleName", targetSimpleName);
	                handlerData.put("eventSimpleName", eventSimpleName);
	                handlerData.put("eventType", eh.eventType());
	                if(render(packageName, handlerName, handlerData, "eventhandler.vm")){
	                	handlers.add(handlerData);
	                }
				}
			}
		}
	    return writeHandlerRegistrator(handlers);
	}

	private boolean writeHandlerRegistrator(Set<Map<String, Object>> handlers){
		if(handlers.isEmpty()){
			return false;
		}
		Map<String, Object> data = new HashMap<String, Object>();//Collections.singletonMap("handlers", handlers);
		data.put("handlers", handlers);
		data.put("packageName", packageName);
		data.put("className", registerClassName);
		return render(packageName, registerClassName, data, "handleregister.vm");
	}
}
