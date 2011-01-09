package ea.containerloading

import org.uncommons.watchmaker.framework.FitnessEvaluator
import java.util.{List => jList}
import scala.collection.JavaConversions._

class PackingEvaluator(problem: ContainerProblem) extends FitnessEvaluator[jList[Int]] {

	def getFitness(candidate: jList[Int], population: jList[_ <: jList[Int]]): Double = {
		
		val boxLoadingOrder = candidate.toList.map(problem.getBox(_)) //TODO wieso .toList nötig?
		val loadingResult = ContainerLoader.loadLayer(problem.getContainer, boxLoadingOrder) 
				
		// calculate and return space utilization ratio
		val spaceUsed = loadingResult.loadedBoxes.map(loadedBox => loadedBox.box.size.volume).sum
		return (spaceUsed: Double) / (problem.getContainer.size.volume: Double)
	}
	
	def isNatural = true
}