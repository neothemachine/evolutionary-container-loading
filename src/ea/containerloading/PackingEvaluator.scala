package ea.containerloading

import org.uncommons.watchmaker.framework.FitnessEvaluator
import scala.collection.JavaConversions._
import java.util.{List => jList}
import org.uncommons.maths.random.MersenneTwisterRNG 

class PackingEvaluator(problem: ContainerProblem) extends FitnessEvaluator[jList[Int]] {

	def getFitness(candidate: jList[Int], population: jList[_ <: jList[Int]]): Double = {
		return candidate.take(5).sum
	}
	
	def isNatural(): Boolean = true
}