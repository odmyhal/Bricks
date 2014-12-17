package org.bricks.annotation.processor;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaFileObject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public abstract class CreateSourceProcessor extends AbstractProcessor {

	protected boolean render(String targetPackage, String targetClass, Map<String, ?> data, String template){
		JavaFileObject jfo = null;
		try {
			jfo = processingEnv.getFiler().createSourceFile(String.format("%s.%s", targetPackage, targetClass));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		Properties props = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");
        try {
			props.load(url.openStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

        VelocityEngine vengin = new VelocityEngine(props);
        vengin.init();

        VelocityContext vcontext = new VelocityContext();
        
        for(String key: data.keySet()){
        	vcontext.put(key, data.get(key));
        }

        Template vt = vengin.getTemplate(template);
        
        Writer writer = null; 
        try {
			writer = jfo.openWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
        vt.merge(vcontext, writer);
        try {
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
        return true;
	}
}
