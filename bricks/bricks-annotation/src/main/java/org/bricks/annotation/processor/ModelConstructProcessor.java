package org.bricks.annotation.processor;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.bricks.annotation.ConstructModel;

@SupportedAnnotationTypes("org.bricks.annotation.ConstructModel")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ModelConstructProcessor extends CreateSourceProcessor {
	
	private final String constructToolPackage = "org.bricks.enterprise.gen";
	private final String constructToolName = "GenModelConstructTool";

	@Override
	public boolean process(Set<? extends TypeElement> arg0, RoundEnvironment roundEnv) {
		boolean empty = true;
		Map<String, String[]> calls = new HashMap<String, String[]>();
		for(Element element : roundEnv.getElementsAnnotatedWith(ConstructModel.class)){
			String modelConstructorName = element.asType().toString();
			ConstructModel cm = element.getAnnotation(ConstructModel.class);
			calls.put(modelConstructorName, cm.value());
			empty = false;
		}
		if(empty){
			return false;
		}
		Map<String, Object> constructorData = new HashMap<String, Object>();
		constructorData.put("calls", calls);
		constructorData.put("constructToolPackage", constructToolPackage);
		constructorData.put("constructToolName", constructToolName);
		return render(constructToolPackage, constructToolName, constructorData, "modelconstructor.vm");
	}

}
