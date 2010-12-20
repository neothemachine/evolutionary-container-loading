package ea.containerloading

import org.uncommons.watchmaker.framework.selection._
import org.uncommons.watchmaker.framework._

object Main {
	def main(args : Array[String]) : Unit = {
		val problem = new ContainerProblem()
		val runner = new EvolutionaryContainerLoading(
				new RankSelection,
				50, 2, 1000, true, true)
		runner.runEvolution(problem, Some(
			new EvolutionObserver[java.util.List[Int]] {
				def populationUpdate(data: PopulationData[_ <: java.util.List[Int]]) = {
					println(data.getGenerationNumber)
				}
			}
		))
		
	}
}
