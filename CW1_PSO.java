import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.Swarm;
import java.lang.Math;

public class CW1_PSO {
	private static double R = 5.12;
	/**
	 *** OPTIMISED PARAMETERS ***
	 *
	 ** Scneario 1: 1M calls **
	 * 	 numParticles = 10000;
	 * 	 numIters = 100;
	 * 	 neighWeight = 0.6;
	 * 	 inertiaWeight = 3.5;
	 * 	 personalWeight = 2.7;
	 * 	 globalWeight = 0.1;
	 * 	 maxMinVelocity = 0.000001;
	 *
	 ** Scneario 2: 10k calls **
	 * 	 numParticles = 200;
	 * 	 numIters = 50;
	 * 	 neighWeight = 0.5;
	 * 	 inertiaWeight = 0.5;
	 * 	 personalWeight = 2.6;
	 * 	 globalWeight = 0.4;
	 * 	 maxMinVelocity = 0.000001;
	 *
	 */
	private int numParticles = 200;
	private int numIters = 50;
	private double neighWeight = 0.5;
	private double inertiaWeight = 0.5;
	private double personalWeight = 2.6;
	private double globalWeight = 0.4;
	private double maxMinVelocity = 0.000001;

	public void parseParams(String paramFile) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(paramFile));

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);

				if (key.equals("numParticles")) {
					numParticles = Integer.parseInt(value);
				} else if (key.equals("neighWeight")) {
					neighWeight = Double.parseDouble(value);
				} else if (key.equals("inertiaWeight")) {
					inertiaWeight = Double.parseDouble(value);
				} else if (key.equals("personalWeight")) {
					personalWeight = Double.parseDouble(value);
				} else if (key.equals("globalWeight")) {
					globalWeight = Double.parseDouble(value);
				} else if (key.equals("maxMinVelocity")) {
					maxMinVelocity = Double.parseDouble(value);
				} else if (key.equals("numIters")) {
					numIters = Integer.parseInt(value);
				} else {
					System.out.println("Unknown parameter " + key);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		// Create a swarm (using 'MyParticle' as sample particle 
		// and 'MyFitnessFunction' as finess function)
		Swarm swarm = new Swarm(numParticles
				, new MyParticle()
				, new MyFitnessFunction());
		// Set position (and velocity) constraints. 
		// i.e.: where to look for solutions

		// Use neighborhood
		Neighborhood1D neigh = new Neighborhood1D(numParticles / 10, true);
		swarm.setNeighborhood(neigh);
		swarm.setNeighborhoodIncrement(neighWeight);

		// Set weights of velocity update formula
		swarm.setInertia(inertiaWeight); // Previous velocity weight
		swarm.setParticleIncrement(personalWeight); // Personal best weight
		swarm.setGlobalIncrement(globalWeight); // Global best weight

		// Set limits to velocity value
		swarm.setMaxMinVelocity(maxMinVelocity);

		// Set max and min positions
		swarm.setMaxPosition(+R);
		swarm.setMinPosition(-R);

		// Optimize a few times
		for (int i = 0; i < numIters; i++) {
			swarm.evolve();
			//System.out.println(swarm.toStringStats());
		}
		//System.out.println(swarm.toStringStats());
		System.out.println(swarm.getBestFitness());

	}

	public static void main(final String[] args) {
		CW1_PSO alg = new CW1_PSO();
		if (args.length > 0) {
			alg.parseParams(args[0]);
		}
		/*for (double n = 0.4; n <= 0.6; n += .1) {
			alg.neighWeight = n;
			for (double i = 0.4; i <= 0.6; i += .1) {
				alg.inertiaWeight = i;
				for (double p = 2.4; p <= 2.6; p += .1) {
					alg.personalWeight = p;
					for (double g = 0.4; g <= 0.6; g += .1) {
						alg.globalWeight = g;
						System.out.println("neighWeight: " + alg.neighWeight);
						System.out.println("inertiaWeight: " + alg.inertiaWeight);
						System.out.println("personalWeight: " + alg.personalWeight);
						System.out.println("globalWeight: " + alg.globalWeight);*/
		for (double v = 1; v >= 1E-10; v /= 10) {
			alg.maxMinVelocity = v;
			System.out.println("maxMinVelocity: " + alg.maxMinVelocity);

			for (int j = 50; j <= 50; j += 10) {
				alg.numIters = j;
				System.out.println("number of iterations: " + alg.numIters);
				for (int h = 0; h < 30; h++) {
					alg.run();
				}
				System.out.println("**************************");
			}
		}
	}
}
/*
			}
		}
	}
}
*/

