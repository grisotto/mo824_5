package problems.qbf.solvers;

import java.io.IOException;
import metaheuristics.ga.AbstractGA;
import problems.qbf.QBF;
import solutions.Solution;

/**
 * Metaheuristic GA (Genetic Algorithm) for
 * obtaining an optimal solution to a QBF (Quadractive Binary Function --
 * {@link #QuadracticBinaryFunction}). 
 * 
 * @author ccavellucci, fusberti
 */
public class GA_QBF extends AbstractGA<Integer, Integer> {

	/**
	 * Constructor for the GA_QBF class. The QBF objective function is passed as
	 * argument for the superclass constructor.
	 * 
	 * @param generations
	 *            Maximum number of generations.
	 * @param popSize
	 *            Size of the population.
	 * @param mutationRate
	 *            The mutation rate.
	 * @param filename
	 *            Name of the file for which the objective function parameters
	 *            should be read.
	 * @throws IOException
	 *             Necessary for I/O operations.
	 */
	public GA_QBF(Integer generations, Integer popSize, Double mutationRate, String filename, Double fitRate, Double sizeRate) throws IOException {
		super(new QBF(filename), generations, popSize, mutationRate, fitRate, sizeRate);
		System.out.println("Mutacao: %"+ mutationRate * 100);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This createEmptySol instantiates an empty solution and it attributes a
	 * zero cost, since it is known that a QBF solution with all variables set
	 * to zero has also zero cost.
	 */
	@Override
	public Solution<Integer> createEmptySol() {
		Solution<Integer> sol = new Solution<Integer>();
		sol.cost = 0.0;
		return sol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see metaheuristics.ga.AbstractGA#decode(metaheuristics.ga.AbstractGA.
	 * Chromosome)
	 */
	@Override
	protected Solution<Integer> decode(Chromosome chromosome) {

		Solution<Integer> solution = createEmptySol();
		for (int locus = 0; locus < chromosome.size(); locus++) {
			if (chromosome.get(locus) == 1) {
				solution.add(new Integer(locus));
			}
		}

		ObjFunction.evaluate(solution);
		return solution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see metaheuristics.ga.AbstractGA#generateRandomChromosome()
	 */
	@Override
	protected Chromosome generateRandomChromosome() {

		Chromosome chromosome = new Chromosome();
		for (int i = 0; i < chromosomeSize; i++) {
			chromosome.add(rng.nextInt(2));
		}

		return chromosome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see metaheuristics.ga.AbstractGA#fitness(metaheuristics.ga.AbstractGA.
	 * Chromosome)
	 */
	@Override
	protected Double fitness(Chromosome chromosome) {

		return decode(chromosome).cost;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * metaheuristics.ga.AbstractGA#mutateGene(metaheuristics.ga.AbstractGA.
	 * Chromosome, java.lang.Integer)
	 */
	@Override
	protected void mutateGene(Chromosome chromosome, Integer locus) {

		chromosome.set(locus, 1 - chromosome.get(locus));

	}

	/**
	 * A main method used for testing the GA metaheuristic.
	 * 
	 */
	public static void main(String[] args) throws IOException {
		/**
		 * The parameters for the GA Diversity Maintenance.
		 * 
		 * @param instanceSize
		 *            The size of the instance
		 * @param popSize
		 *            Population size.
		 * @param logL
		 *            Number of generations to be executed.
		 * @param mutationRate
		 *            The mutation rate.
		 * @param fitRate
		 *            The % difference between the best fitness
		 * @param sizeRate
		 *            The % difference between the population members
		 */
		double instanceSize = 100.0;
		
		int logL = 7 * (int)(Math.log(instanceSize)/ Math.log(2) );
		
		int popSize = ((logL % 2 == 0)?logL:logL+1);
		
		double mutationRate = 1.0 / instanceSize;
		
		double fitRate = 0.98; 
		
		double sizeRate = 0.005;

		long startTime = System.currentTimeMillis();
		GA_QBF ga = new GA_QBF(10000, popSize, mutationRate , "instances/qbf100", fitRate, sizeRate);
		Solution<Integer> bestSol = ga.solve();
		System.out.println("maxVal = " + bestSol);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");

	}

}
