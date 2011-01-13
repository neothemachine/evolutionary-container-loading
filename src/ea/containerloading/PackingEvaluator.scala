package ea.containerloading

import org.uncommons.watchmaker.framework.FitnessEvaluator
import java.util.{List => jList}
import scala.collection.JavaConversions._

class PackingEvaluator(problem: ContainerProblem) extends FitnessEvaluator[jList[Int]] {

	def getFitness(candidate: jList[Int], population: jList[_ <: jList[Int]]): Double = {
		
		val boxLoadingOrder = candidate.toList.map(problem.boxFromId(_)) //TODO wieso .toList nötig?
		val loadingResult = ContainerLoader.loadLayer(problem.container, boxLoadingOrder) 
				
		// calculate and return space utilization ratio
		val spaceUsed = loadingResult.loadedBoxes.map(loadedBox => loadedBox.box.size.volume).sum
		val ratio = (spaceUsed: Double) / (problem.container.size.volume: Double)
		return ratio
	}
	
	def isNatural = true
}