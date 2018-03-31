package com.stuartmorse.neuralsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.stuartmorse.neural.IonChannelType;
import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;
import com.stuartmorse.neural.ionchannel.CAMPIonChannel;
import com.stuartmorse.neural.ionchannel.VGCalciumIonChannel;
import com.stuartmorse.neural.neuron.Neuron;
import com.stuartmorse.neural.neuron.PNSNeuron;
import com.stuartmorse.neural.neuron.Synapse;
import com.stuartmorse.neural.receptor.DopamineD2Receptor;
import com.stuartmorse.neuralsim.minibeans.IonPotential;

@Controller
@RequestMapping(value = "/")
public class NeuralSimController {
	
	class OutputVoltage {
		
		public double voltage;

		public double getVoltage() {
			return voltage;
		}

		public void setVoltage(double voltage) {
			this.voltage = voltage;
		}
	}
	
	NeuralSimDefaults defaults =  new NeuralSimDefaults();
	
	ServletContext context; 
	
	@Autowired
	NeuralSimController(NeuralSimDefaults neuralSimDefaults, ServletContext context) {
		this.defaults = neuralSimDefaults;
		this.context = context;
		
		Map<String, Double> ionPotentials = new HashMap<>();
		ionPotentials.put("Na+", defaults.getSodiumPotential());
		context.setAttribute("ion-potentials", ionPotentials);
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public ModelAndView showMessage(HttpServletRequest request) {

		System.out.println("Hello Stuart Morse!");
		StuTest st = new StuTest("Hello New!", 1.111);
		st.setNum1(1.11111);
		ModelAndView response = new ModelAndView("jsp/firstone.jsp", "model", st);
		return response;
	}

	@RequestMapping(value = "marshall", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public ResponseEntity<OutputVoltage> marshallList(@RequestBody ArrayList<StuTest> input) {

		HttpHeaders responseHeaders = new HttpHeaders();
		System.out.println("Input.msg1 = " + input.get(0).getMsg1());
		OutputVoltage volt = new OutputVoltage();
		volt.setVoltage(0.084);
		return new ResponseEntity<OutputVoltage>(volt, responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value="/addneuronchain", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public ResponseEntity<OutputVoltage> addNeuronChain(@RequestBody StuTest input) throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();
		final int CHAIN_LENGTH = 1;
		OutputVoltage volt;
		Neuron prev = null;
		double level = 0;
		
		// Get external ion potentials
		Map<String, Double> ionPotentials = (Map<String, Double>) context.getAttribute("ion-potentials");
		if (ionPotentials == null) {
			level = defaults.getSodiumPotential();
		} else {
			level = ionPotentials.get("Na+");	
		}

		@SuppressWarnings("unchecked")
		List<Neuron> neuronChain = new ArrayList<Neuron>();
		
		// Create chain of neurons
		for (int idx=0; idx < CHAIN_LENGTH; idx++) {
			PNSNeuron next = new PNSNeuron(1, 8, 1, idx);
			neuronChain.add(next);
			
			Synapse synapse = new Synapse(prev, next);
			synapse.addReceptors(60, LigandType.DOPAMINE, DopamineD2Receptor.class);
			synapse.addSynapticVesicles(LigandType.DOPAMINE, 20);
			//synapse.setPreSynapticConcentration(GabaPentin.class, 0.35);
			synapse.addPreSynapticIonChannels(100, IonChannelType.VGCA_ION_CHANNEL, VGCalciumIonChannel.class);
			next.setExternalSodiumPotential(level);
			next.addCNGIonChannels(960, CAMPIonChannel.class);
			next.setHeadSynapse(synapse);
			
			if (prev != null) {
				prev.setTailSynapse(synapse);
			}
			prev = next;
		}
		
		PNSNeuron head = (PNSNeuron) neuronChain.get(0);
		Synapse rootSynapse = new Synapse(null, head);
		rootSynapse.addReceptors(60, LigandType.DOPAMINE, DopamineD2Receptor.class);
		rootSynapse.addSynapticVesicles(LigandType.DOPAMINE, 20);
		//synapse.setPreSynapticConcentration(GabaPentin.class, 0.35);
		rootSynapse.addPreSynapticIonChannels(100, IonChannelType.VGCA_ION_CHANNEL, VGCalciumIonChannel.class);

		context.setAttribute("root-synapse", rootSynapse);
		context.setAttribute("neuron-chain", neuronChain);

		head.setHeadSynapse(rootSynapse);
		rootSynapse.transduceSignal(Voltage.FIRING_THRESHOLD.getValue());
		double internalVoltage = head.epspController(rootSynapse);
		
		volt = new OutputVoltage();
		volt.setVoltage(internalVoltage);
		
		return new ResponseEntity<OutputVoltage>(volt, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/setionpotentials", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public ResponseEntity<Map<String, Double>> pushJson(@RequestBody ArrayList<IonPotential> input) throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();
		@SuppressWarnings("unchecked")
		Map<String, Double>ionPotentials = (Map<String, Double>) context.getAttribute("ion-potentials");
		
		for(IonPotential potential : input) {
			ionPotentials.put(potential.getIonType(), potential.getPotential());
		}
		
		context.setAttribute("ion-potentials", ionPotentials);
		return new ResponseEntity<Map<String, Double>>(ionPotentials, responseHeaders, HttpStatus.CREATED);		
	}
}