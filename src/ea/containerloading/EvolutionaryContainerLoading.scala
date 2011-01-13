package ea.containerloading

import org.uncommons.watchmaker.framework._
import org.uncommons.watchmaker.framework.operators._
import org.uncommons.watchmaker.framework.factories._
import org.uncommons.watchmaker.framework.termination._
import org.uncommons.maths.random._
import ea.watchmaker.Implicits._
import scala.collection.JavaConversions._
import java.util.{List => jList, ArrayList => jArrayList}

class EvolutionaryContainerLoading(
			 selectionStrategy: SelectionStrategy[_ >: jList[Int]],
             populationSize: Int,
             eliteCount: Int,
             generationCount: Int,
             crossover: Boolean,
             mutation: Boolean) {
	
	assert(crossover || mutation)
	
	def runEvolution(problem: ContainerProblem, listener: Option[(PopulationData[_ <: jList[Int]]) => Unit] = None): jList[Int] = {
		
		var operators = (crossover, mutation) match {
			case (true, false) => List(new ListOrderCrossover[Int])
			case (false, true) => List(new ListOrderMutation[Int]) // TODO params
			case (true, true) => List(new ListOrderCrossover[Int],
			                          new ListOrderMutation[Int])
		}
		
		val pipeline = new EvolutionPipeline(operators)
		
		val candidateFactory = new ListPermutationFactory(problem.boxes.map(_.id).toList)
		
		val engine = new GenerationalEvolutionEngine[jList[Int]](
				candidateFactory,
				pipeline,
				new CachingFitnessEvaluator(new PackingEvaluator(problem)),
				selectionStrategy,
				new MersenneTwisterRNG
				)
		
		if (listener.isDefined) {
			engine.addEvolutionObserver(listener.get)
		}
		
		return engine.evolve(populationSize, eliteCount, new GenerationCount(generationCount))
	}
	
}