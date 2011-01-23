package ea.containerloading

import org.uncommons.watchmaker.framework._
import org.uncommons.watchmaker.framework.operators._
import org.uncommons.watchmaker.framework.factories._
import org.uncommons.watchmaker.framework.termination._
import org.uncommons.watchmaker.framework.islands._
import org.uncommons.maths.random._
import org.uncommons.maths.random.Probability
import org.uncommons.maths.number.ConstantGenerator
import ea.watchmaker.Implicits._
import scala.collection.JavaConversions._
import java.util.{List => jList, ArrayList => jArrayList}

case class IslandConfig(epochLength: Int, migrantCount: Int)

class EvolutionaryContainerLoading(
			 islands: Option[IslandConfig],
			 selectionStrategy: SelectionStrategy[_ >: jList[(Box, BoxRotation)]],
             populationSize: Int,
             eliteCount: Int,
             termination: TerminationCondition,
             crossoverProbability: Probability) {
	
	var listeners = List[PopulationData[_ <: jList[(Box, BoxRotation)]] => Unit]()
	var islandListeners = List[(Int, PopulationData[_ <: jList[(Box, BoxRotation)]]) => Unit]()
	
	def runEvolution(problem: ContainerProblem): Seq[(Box, BoxRotation)] = {
		
		val rng = new MersenneTwisterRNG
		
//		val pipelineWithRotation = new EvolutionPipeline(List(
//				new ListOrderCrossover[(Box, BoxRotation)](crossoverProbability),
//				new ListOrderMutation[(Box, BoxRotation)], // TODO parameter
//				new RotationMutation
//			))
		
		val pipelineWithRotation = new SplitEvolution(
				new EvolutionPipeline(List(
					new ListOrderCrossover[(Box, BoxRotation)](crossoverProbability),
					new ListOrderMutation[(Box, BoxRotation)] // TODO parameter
					)
				),
				new RotationMutation,
				0.9
				)
		
		val pipelineWithoutRotation = new EvolutionPipeline(List(
				new ListOrderCrossover[(Box, BoxRotation)](crossoverProbability),
				new ListOrderMutation[(Box, BoxRotation)] // TODO parameter
			))
						
		val boxesAndRotation = problem.boxes.map(box => (box, BoxRotation(false, false, false))).toList
		val candidateFactory = new ListPermutationFactory(boxesAndRotation)
		
		val fitnessEvaluator = new CachingFitnessEvaluator(new PackingEvaluator(problem))
		
		if (islands.isDefined) {
			val islandConfig = islands.get
			
			// multiple islands without differences
//			val islandEvolution = new IslandEvolution[jList[(Box, BoxRotation)]](
//					islandConfig.islandCount,
//					new RingMigration,
//					candidateFactory,
//					pipeline,
//					fitnessEvaluator,
//					selectionStrategy,
//					rng
//					)
			
			// 2 islands: one with rotation, one without
			val islandWithRotation = new GenerationalEvolutionEngine[jList[(Box, BoxRotation)]](
					candidateFactory,
					pipelineWithRotation,
					fitnessEvaluator,
					selectionStrategy,
					rng)
					
			val islandWithoutRotation = new GenerationalEvolutionEngine[jList[(Box, BoxRotation)]](
					candidateFactory,
					pipelineWithoutRotation,
					fitnessEvaluator,
					selectionStrategy,
					rng)
					
			val islandEvolution = new IslandEvolution[jList[(Box, BoxRotation)]](
				List(islandWithRotation, islandWithoutRotation),
				new RingMigration,
				true, rng)
			
			islandEvolution.addEvolutionObserver(new IslandEvolutionObserver[jList[(Box, BoxRotation)]] {
				def islandPopulationUpdate(islandIndex: Int, data: PopulationData[_ <: jList[(Box, BoxRotation)]]) = {
					islandListeners.foreach(_(islandIndex, data))
				}
				def populationUpdate(data: PopulationData[_ <: jList[(Box, BoxRotation)]]) = {
					listeners.foreach(_(data))
				}
			})

			islandEvolution.evolve(populationSize, eliteCount, islandConfig.epochLength, islandConfig.migrantCount, termination)					
		} else {

			val engine = new GenerationalEvolutionEngine[jList[(Box, BoxRotation)]](
					candidateFactory,
					pipelineWithRotation,
					fitnessEvaluator,
					selectionStrategy,
					rng
					)
			
			listeners.foreach(engine.addEvolutionObserver(_))
			
			engine.evolve(populationSize, eliteCount, termination)		
		}
	
	}
	
	def addListener(listener: PopulationData[_ <: jList[(Box, BoxRotation)]] => Unit) = {
		this.listeners ::= listener
	}
	
	def removeListener(listener: PopulationData[_ <: jList[(Box, BoxRotation)]] => Unit) = {
		this.listeners = this.listeners.filterNot(_ == listener)
	}
	
	def addIslandListener(listener: (Int, PopulationData[_ <: jList[(Box, BoxRotation)]]) => Unit) = {
		this.islandListeners ::= listener
	}
	
	def removeIslandListener(listener: (Int, PopulationData[_ <: jList[(Box, BoxRotation)]]) => Unit) = {
		this.islandListeners = this.islandListeners.filterNot(_ == listener)
	}
	
}