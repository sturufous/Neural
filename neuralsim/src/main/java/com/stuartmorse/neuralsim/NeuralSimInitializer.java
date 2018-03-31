package com.stuartmorse.neuralsim;

import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class NeuralSimInitializer implements WebApplicationInitializer {

    public void onStartup(ServletContext servletContext) throws ServletException {        
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();  
        context.register(NeuralSimConfig.class);  
        context.setServletContext(servletContext);    
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));  
        servlet.addMapping("/");  
        servlet.setLoadOnStartup(1);
    }
}
