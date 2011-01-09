package ea.containerloading

import org.uncommons.watchmaker.framework.selection._
import org.uncommons.watchmaker.framework._
import org.uncommons.maths.random.Probability
import scala.collection.JavaConversions._

object Main {
	def main(args : Array[String]) : Unit = {

		val problem = new ContainerProblem(
			containerSize = Dimension3D(100, 100, 100),
			boxSizeFrequencies =
				Map(Dimension3D(10,10,10) -> 1,
			        Dimension3D(20,10,10) -> 10,
			        Dimension3D(30,10,20) -> 5,
			        Dimension3D(50,10,10) -> 2))
		
		val runner = new EvolutionaryContainerLoading(
			new RankSelection,
			populationSize = 30,
			eliteCount = 2,
			generationCount = 100,
			crossover = true,
			mutation = true)
		
		val bestCandidate = runner.runEvolution(problem,
			listener = Some(popData => {
				if (popData.getGenerationNumber % 1 == 0) {
					println(popData.getGenerationNumber + " " +
							popData.getBestCandidateFitness + " " + 
							popData.getBestCandidate)
				}
			})
		)
		
		val boxLoadingOrder = bestCandidate.toList.map(problem.getBox(_))
		val loadingResult = ContainerLoader.loadLayer(problem.getContainer, boxLoadingOrder)
		println(loadingResult)
	}
}
