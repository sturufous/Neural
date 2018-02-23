package com.stuartmorse.neuralsim;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;
import com.stuartmorse.neural.ionchannel.CAMPIonChannel;
import com.stuartmorse.neural.neuron.Neuron;
import com.stuartmorse.neural.neuron.PNSNeuron;
import com.stuartmorse.neural.neuron.Synapse;
import com.stuartmorse.neural.receptor.NicotinicReceptor;

/**
 * Servlet implementation class NeuralSim
 */
@WebServlet(name = "neuralsim", urlPatterns = { "/" })
public class NeuralSimRunner extends HttpServlet {

	// private static ResourceBundle defaults = ResourceBundle
	// .getBundle(CommonConstants.RESOURCE_PATH.getValue());
	private static final int CHAIN_LENGTH = 6;
	private static List<PNSNeuron> neuronChain = new LinkedList<>();
	private static Neuron prev = null;

	/**
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public NeuralSimRunner() {
        super();
        // TODO Auto-generated constructor stub
    }

	public synchronized void init() {
		ServletContext context = getServletContext();
		ResourceBundle bundle = ResourceBundle.getBundle("properties/defaults");
		NeuralSimDefaults defaults = new NeuralSimDefaults();

		defaults.setSodiumPotential(bundle.getString("sodium-potential"));
		defaults.setDendriteCount(bundle.getString("dendrite-count"));
		defaults.setNodeCount(bundle.getString("node-count"));
		defaults.setTerminalCount(bundle.getString("terminal-count"));
		defaults.setReceptorCount(bundle.getString("receptor-count"));
		defaults.setCngIonChannelCount(bundle.getString("cng-ion-channel-count"));
		defaults.setVesicleCount(bundle.getString("vesicle-count"));

		context.setAttribute("neuralsim-defaults", defaults);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.println("<h1>Hello World</h1>");
		
		ServletContext context = getServletContext();
		NeuralSimDefaults d = (NeuralSimDefaults) context.getAttribute("neuralsim-defaults");

		// Create chain of neurons
		for (int idx=0; idx < CHAIN_LENGTH; idx++) {
			PNSNeuron next = new PNSNeuron(d.getDendriteCount(), d.getNodeCount(), d.getTerminalCount(), idx);
			
			next.setExternalSodiumPotential(d.getSodiumPotential());
			neuronChain.add(next);
			// Skip the head. Create initial synapse for head in neuralBeat().
			if (prev != null) {
				Synapse synapse = new Synapse(prev, next);
				synapse.addReceptors(d.getReceptorCount(), LigandType.ACETYLCHOLINE, NicotinicReceptor.class);
				// synapse.addReceptors(9, LigandType.GABA,GABAAReceptor.class);
				synapse.addSynapticVesicles(LigandType.ACETYLCHOLINE, d.getVesicleCount());
				next.addCNGIonChannels(d.getCngIonChannelCount(), CAMPIonChannel.class);
				prev.setTailSynapse(synapse);
				next.setHeadSynapse(synapse);
			}
			prev = next;
		}

		neuralBeat(out);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public static void neuralBeat(PrintWriter out) {

		double internalVoltage;

		PNSNeuron head = neuronChain.get(0);
		// Create dummy synapse to get the ball rolling
		Synapse initialSynapse = new Synapse(null, head);

		initialSynapse.addSynapticVesicles(LigandType.ACETYLCHOLINE, 10);
		//initialSynapse.addSynapticVesicles(LigandType.GABA, 8);

		initialSynapse.addReceptors(56, LigandType.ACETYLCHOLINE, NicotinicReceptor.class);
		head.addCNGIonChannels(960, CAMPIonChannel.class);
		//initialSynapse.addReceptors(1, LigandType.GABA, GABAAReceptor.class);

		initialSynapse.fuseVesicles(Voltage.FIRING_THRESHOLD.getValue());
		head.setHeadSynapse(initialSynapse);
		internalVoltage = head.epspController(initialSynapse);
	}
}
