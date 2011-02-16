package ea.containerloading

import org.uncommons.watchmaker.framework.FitnessEvaluator
import java.util.{List => jList}
import scala.collection.JavaConversions._

class PackingEvaluator(problem: ContainerProblem) extends FitnessEvaluator[jList[(Box, BoxRotation)]] {

	def getFitness(candidate: jList[(Box, BoxRotation)], population: jList[_ <: jList[(Box, BoxRotation)]]): Double = {
		
		val loadingResult = ContainerLoader.loadLayer(problem.container, candidate)
				
		// calculate and return space utilization ratio
		val spaceUsed = loadingResult.loadedBoxes.map(_.box.size.volume).sum
		val ratio = (spaceUsed: Double) / (problem.container.size.volume: Double)
		return ratio
	}
	
	def isNatural = true
}