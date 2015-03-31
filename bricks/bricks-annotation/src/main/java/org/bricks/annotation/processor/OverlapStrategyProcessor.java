package org.bricks.annotation.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import org.bricks.annotation.OverlapCheck;

@SupportedAnnotationTypes("org.bricks.annotation.OverlapCheck")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class OverlapStrategyProcessor extends CreateSourceProcessor {
	
	private final String packageName = "org.bricks.engine.overlap";
	private final String registratorClassName = "OSRegistrator";

	@Override
	public boolean process(Set<? extends TypeElement> arg0, RoundEnvironment roundEnv) {
		
		Set<Map<String, String>> handlers = new HashSet<Map<String, String>>();
		Map<String, Integer> osNumbers = new HashMap<String, Integer>();
		for(Element element : roundEnv.getElementsAnnotatedWith(OverlapCheck.class)){
			if(element.getKind() == ElementKind.METHOD){
				ExecutableElement ee = (ExecutableElement) element;
				
				Element te = element.getEnclosingElement();
				List<? extends VariableElement> params = ee.getParameters();
				if(params.size() != 1){
					this.error(String.format("Method '%s.%s' annotated with OverlapCheck should have only one parameter",
							te.asType().toString(), ee.asType().toString()));
				}
				VariableElement ve = params.get(0);
				String paramClassName = this.fetchSimpleClassName(ve.asType().toString());
				if(!paramClassName.equals("OverlapEvent")){
					this.error(String.format("Method '%s.%s' annotated with OverlapCheck should have OverlapEvent parameter, but '%s' given",
							te.asType().toString(), ee.asType().toString(), paramClassName));
				}
				if(te.getKind() == ElementKind.CLASS){
					Map<String, String> handlerData = new HashMap<String, String>();
					String targetClassName = this.stripTemplates(te.asType().toString());
					
//					OverlapCheck annotation = element.getAnnotation(OverlapCheck.class);
					Element annotationElement = processingEnv.getElementUtils().getTypeElement( OverlapCheck.class.getName() );
					AnnotationMirror annotationMirror = null;
					for(AnnotationMirror mirror : element.getAnnotationMirrors()){
						if(mirror.getAnnotationType().equals(annotationElement.asType())){
							annotationMirror = mirror;
						}
					}
					if(annotationMirror == null){
						this.error("Could not find mirror of CheckOverlap annotation...");
					}
					String algorithmClassName = null;
					String sourceType = null;
					String methodName = null;
					String strategyClassName = null;
					for( Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet() ){
						AnnotationValue annotationValue = entry.getValue();
						String key = entry.getKey().getSimpleName().toString();
						if(key.equals("algorithm")){
							algorithmClassName = this.stripClassEnding(this.stripTemplates(annotationValue.toString()));
						}else if(key.equals("sourceType")){
							sourceType = annotationValue.toString();
						}else if(key.equals("strategyClass")){
							strategyClassName = this.stripClassEnding(this.stripTemplates(annotationValue.toString()));
						}else if(key.equals("strategyMethod")){
							methodName = annotationValue.toString();
						}
					}
					
					if( (strategyClassName == null) || strategyClassName.equals("org.bricks.engine.event.overlap.OverlapStrategy")){
						
						strategyClassName = this.fetchSimpleClassName(targetClassName) + "OverlapStrategy";
						if(osNumbers.containsKey(strategyClassName)){
							int nmb = osNumbers.get(strategyClassName);
							strategyClassName += "_" + (++nmb);
							osNumbers.put(strategyClassName, nmb);
						}else{
							osNumbers.put(strategyClassName, 0);
						}
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("targetClassName", targetClassName);
						data.put("strategyClassName", strategyClassName);
						data.put("packageName", packageName);
						data.put("methodName", methodName);
						render(packageName, strategyClassName, data, "overlapstrategy.vm");
						strategyClassName = packageName + "." + strategyClassName;
					}
					handlerData.put("targetClassName", targetClassName);
					handlerData.put("sourceType", sourceType);
					handlerData.put("algorithmClassName", algorithmClassName);
					handlerData.put("strategyClassName", strategyClassName);
					handlers.add(handlerData);
				}
			}
		}
		return writeOverlapRegistrator(handlers);
	}

	private boolean writeOverlapRegistrator(Set<Map<String, String>> handlers){
		if(handlers.isEmpty()){
			return false;
		}
		Map<String, Object> data = new HashMap<String, Object>();//Collections.singletonMap("handlers", handlers);
		data.put("handlers", handlers);
		data.put("packageName", packageName);
		data.put("className", registratorClassName);
		return render(packageName, registratorClassName, data, "overlapregister.vm");
	}
	
	private String stripClassEnding(String fullClassName){
		return fullClassName.substring(0, fullClassName.lastIndexOf(".class"));
	}
}
