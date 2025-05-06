package utils;


import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// Retry Listener to dynamically assign RetryAnalyser
public class RetryListenerClass implements IAnnotationTransformer {
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		// Assign the RetryAnalyser class to each test annotation
		annotation.setRetryAnalyzer(utils.RetryAnalyser.class);
	}
}
