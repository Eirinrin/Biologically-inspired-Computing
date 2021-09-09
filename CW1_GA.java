import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static java.lang.Math.*;

import io.jenetics.DoubleGene;
import io.jenetics.SinglePointCrossover;
import io.jenetics.UniformCrossover;
import io.jenetics.MeanAlterer;
import io.jenetics.TournamentSelector;
import io.jenetics.EliteSelector;
import io.jenetics.Mutator;
import io.jenetics.GaussianMutator;
import io.jenetics.Optimize;
import io.jenetics.Phenotype;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.DoubleRange;

public class CW1_GA {
	private static final double A = 10;
	private static final double R = 5.12;
	private static final int N = 10;
    /**
     *** OPTIMISED PARAMETERS ***
	 *
	 ** Scneario 1: 1M calls **
	 * 		popSize=100
	 * 		numSurvivors = 3
	 *  	tournamentSize = 4
	 * 		probMutation = 0.04
	 * 		probCrossover = 1
	 * 		numIters = 10000
	 * 		Alterers: Both mutators & Mean Crossover
	 *
	 ** Scneario 2: 10k calls **
	 * 	  	popSize=50
	 * 	  	numSurvivors = 3
	 * 	    tournamentSize = 4
	 * 	  	probMutation = 0.04
	 * 	  	probCrossover = 1
	 * 	  	numIters = 200
	 * 	  	Alterers: Both mutators & Mean Crossover
	 *
     */
	private int popSize = 50;
	private int numSurvivors = 3;
	private int tournamentSize = 4;
	private double probMutation = 0.04;
	private double probCrossover = 1;
	private int numIters = 200;
	private static double fitness(final double[] x) {
		double value = A*N;
		for (int i = 0; i < N; ++i) {
			value += x[i]*x[i] - A*cos(2.0*PI*x[i]);
		}

		return value;
	}

	public void parseParams(String paramFile) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(paramFile));

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
	
				if(key.equals("popSize")) {
					popSize = Integer.parseInt(value);
				} else if(key.equals("numSurvivors")) {
					numSurvivors = Integer.parseInt(value);
				} else if(key.equals("tournamentSize")) {
					tournamentSize = Integer.parseInt(value);
				} else if(key.equals("probMutation")) {
					probMutation = Double.parseDouble(value);
				} else if(key.equals("probCrossover")) {
					probCrossover = Double.parseDouble(value);
				} else if(key.equals("numIters")) {
					numIters = Integer.parseInt(value);
				} else {
					System.out.println("Unknown parameter "+key);
				} 
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		final Engine<DoubleGene, Double> engine = Engine
			.builder(
				CW1_GA::fitness,
				// Codec for 'x' vector.
				Codecs.ofVector(DoubleRange.of(-R, R), N))
			.populationSize(popSize)
			.optimize(Optimize.MINIMUM)
			.survivorsSize(numSurvivors)
			.survivorsSelector(new EliteSelector<>(numSurvivors))
			.offspringSelector(new TournamentSelector<>(tournamentSize))
			.alterers(
				new Mutator<>(probMutation),
				new GaussianMutator<>(probMutation),
				//new UniformCrossover<>(probCrossover))
				//new SinglePointCrossover<>(probCrossover))
				new MeanAlterer<>(probCrossover))
			.build();

		final EvolutionStatistics<Double, ?>
			statistics = EvolutionStatistics.ofNumber();


		final Phenotype<DoubleGene, Double> best = engine.stream()
				.limit(numIters)
				.peek(statistics)
				//.peek(r -> System.out.println("Generation : "+r.getGeneration()+", average fitness : "+((DoubleMomentStatistics)statistics.getFitness()).getMean()))
				//.peek(r -> System.out.println(r.getGeneration()))
				//.peek(r -> System.out.println(r.getBestFitness()))
				.collect(toBestPhenotype());

		//System.out.println(statistics);
		//System.out.println(best);
		System.out.println(best.getFitness());

	}


	public static void main(final String[] args) {
		CW1_GA alg = new CW1_GA();



		if(args.length>0) {
			alg.parseParams(args[0]);
		}
		/*for(int n=20;n>=20;n/=10){
            alg.popSize=n;
            System.out.println("Population: "+alg.popSize);
        */
        /*for(double k=0.02;k<=1;k*=2){
            alg.probMutation=k;
            System.out.println("mutator:" +alg.probMutation);
        */
        /*for(double l=0.4;l<=1;l+=0.1){
            alg.probCrossover=l;
            System.out.println("crossover:" +alg.probCrossover);
        */
        /*for(int m=1;m<=10;m++){
            alg.numSurvivors=m;
            System.out.println("number of survivors: "+alg.numSurvivors);
        */
        /*for(int o=3;o<=10;o++){
            alg.tournamentSize=o;
            System.out.println("tournament size: "+alg.tournamentSize);
        */
                 for(int j=200;j<=200;j+=50){
                    alg.numIters=j;
                    System.out.println("number of iterations: "+alg.numIters);
                    for(int i=0;i<30;i++){
                         alg.run();
                    }
                    System.out.println("**************************");



             // }
           // }

		}
	}
}
