package com.stuartmorse.neuralsim;

import java.util.ResourceBundle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.stuartmorse.neuralsim")
public class NeuralSimConfig {
	
   @Bean
   public NeuralSimDefaults neuralSimDefaults() {
	   
	   NeuralSimDefaults defaults =  new NeuralSimDefaults();
	   ResourceBundle bundle = ResourceBundle.getBundle("properties/defaults");
		
	   defaults.setSodiumPotential(bundle.getString("sodium-potential"));
	   defaults.setDendriteCount(bundle.getString("dendrite-count"));
	   defaults.setNodeCount(bundle.getString("node-count"));
	   defaults.setTerminalCount(bundle.getString("terminal-count"));
	   defaults.setReceptorCount(bundle.getString("receptor-count"));
	   defaults.setCngIonChannelCount(bundle.getString("cng-ion-channel-count"));
	   defaults.setVesicleCount(bundle.getString("vesicle-count"));
	   
	   return defaults;
    }
}
