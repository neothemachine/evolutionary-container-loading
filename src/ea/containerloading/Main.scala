package ea.containerloading

import org.uncommons.watchmaker.framework.selection._
import org.uncommons.watchmaker.framework._
import org.uncommons.maths.random.Probability

object Main {
	def main(args : Array[String]) : Unit = {
				
		val problem = new ContainerProblem(
			containerSize = Dimension(1000, 1000, 1000),
			boxSizeFrequencies =
				Map(Dimension(10,10,10) -> 1,
			        Dimension(20,10,10) -> 10,
			        Dimension(30,10,20) -> 5,
			        Dimension(50,10,10) -> 2))
		
		val runner = new EvolutionaryContainerLoading(
			new RankSelection,
			populationSize = 30,
			eliteCount = 2,
			generationCount = 100,
			crossover = true,
			mutation = true)
		
		runner.runEvolution(problem,
			listener = Some(popData => {
				println(popData.getGenerationNumber + " " +
						popData.getBestCandidateFitness + " " + 
						popData.getBestCandidate)
			})
		)
		
	}
}
