package ea.containerloading

import org.uncommons.watchmaker.framework._
import org.uncommons.watchmaker.framework.operators._
import org.uncommons.watchmaker.framework.factories._
import org.uncommons.watchmaker.framework.termination._
import org.uncommons.maths.random._
import org.uncommons.maths.random.Probability
import org.uncommons.maths.number.ConstantGenerator
import ea.watchmaker.Implicits._
import scala.collection.JavaConversions._
import java.util.{List => jList, ArrayList => jArrayList}

class EvolutionaryContainerLoading(
			 selectionStrategy: SelectionStrategy[_ >: jList[Int]],
             populationSize: Int,
             eliteCount: Int,
             generationCount: Int,
             crossoverProbability: Probability) {
	
	def runEvolution(problem: ContainerProblem, listener: Option[(PopulationData[_ <: jList[Int]]) => Unit] = None): Seq[(Box, BoxRotation)] = {
		
		val rng = new MersenneTwisterRNG
		
		var operators = List(
				new ListOrderCrossover[Int](crossoverProbability),
				new ListOrderMutation[Int] // TODO parameter
				)
		
		val pipeline = new EvolutionPipeline(operators)
				
		val boxIdMapper = new BoxIdRotationMapper(problem)
		
		val candidateFactory = new ListPermutationFactory(boxIdMapper.mappedBoxIds)
		
		val engine = new GenerationalEvolutionEngine[jList[Int]](
				candidateFactory,
				pipeline,
				new CachingFitnessEvaluator(new PackingEvaluator(problem, boxIdMapper)),
				selectionStrategy,
				rng
				)
		
		if (listener.isDefined) {
			engine.addEvolutionObserver(listener.get)
		}
		
		val bestCandidate = engine.evolve(populationSize, eliteCount, new GenerationCount(generationCount))
		boxIdMapper.distinctBoxes(bestCandidate)
	}
	
}

class BoxIdRotationMapper(problem: ContainerProblem) {
	
	/**
	 * neue Id -> (box, rotation)
	 */
	private val mapping: Map[Int, (Box, BoxRotation)] = createMapping
	
	val mappedBoxIds = mapping.keys.toSeq
	
	private def createMapping: Map[Int, (Box, BoxRotation)] = {
		
		val idMap = collection.mutable.Map[Int, (Box, BoxRotation)]()
		
		var currentId = 0
		for (box <- problem.boxes) {
			for (allowedRotation <- box.constraints.allowedRotations) {
				idMap += currentId -> (box, allowedRotation)
				currentId += 1
			}
		}
		
		Map(idMap.toList:_*)
	}
	
	/**
	 * wenn Ids auf selbe gemappte box zeigen, wird nur die erste mit
	 * der jeweiligen Rotation übernommen
	 * @param boxIds
	 * @return
	 */
	def distinctBoxes(boxIds: Seq[Int]): Seq[(Box, BoxRotation)] = {
		
		val boxesAlreadyAdded = collection.mutable.Set[Box]()
		var distinctBoxes = List[(Box, BoxRotation)]()
		
		for (boxId <- boxIds) {
			val boxAndRotation = mapping(boxId)
			if (!boxesAlreadyAdded.contains(boxAndRotation._1)) {
				distinctBoxes ::= boxAndRotation
				boxesAlreadyAdded += boxAndRotation._1
			}
		}
		
		distinctBoxes.reverse
	}
}