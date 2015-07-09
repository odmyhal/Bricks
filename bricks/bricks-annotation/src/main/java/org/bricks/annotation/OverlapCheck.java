package org.bricks.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import org.bricks.engine.event.overlap.OverlapAlgorithm;
import org.bricks.engine.event.overlap.OverlapStrategy;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface OverlapCheck{

	String sourceType();
	Class<? extends OverlapAlgorithm> algorithm();
	Class<? extends OverlapStrategy.HabitantDataExtractor> extractor();
	Class<? extends OverlapStrategy.EventProducer> producer();
	Class<? extends OverlapStrategy> strategyClass() default OverlapStrategy.class;
	String strategyMethod() default "default";
}
