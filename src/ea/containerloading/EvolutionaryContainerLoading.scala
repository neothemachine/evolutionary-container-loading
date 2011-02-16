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
			 val islands: Option[IslandConfig],
			 selectionStrategy: SelectionStrategy[_ >: jList[(Box, BoxRotation)]],
             populationSize: Int,
             eliteCount: Int,
             crossoverProbability: Probability,
             termination: TerminationCondition*) {
	
	var listeners = List[PopulationData[_ <: jList[(Box, BoxRotation)]] => Unit]()
	var islandListeners = List[(Int, PopulationData[_ <: jList[(Box, BoxRotation)]]) => Unit]()
	
	def runEvolution(problem: ContainerProblem): Seq[(Box, BoxRotation)] = {
		
		val rng = new MersenneTwisterRNG
		
		val boxesAndRotation = problem.boxes.map(box => (box, BoxRotation(false, false, false))).toList
		val candidateFactory = new ListPermutationFactory(boxesAndRotation)
		
//		val pipelineWithRotation = new EvolutionPipeline(List(
//				new ListOrderCrossover[(Box, BoxRotation)](crossoverProbability),
//				new ListOrderMutation[(Box, BoxRotation)], // TODO parameter
//				new RotationMutation
//			))
		
//		val pipelineWithRotation = new SplitEvolution(
//				new EvolutionPipeline(List(
//					new ListOrderCrossover2[(Box, BoxRotation)](crossoverProbability),
//					new ListOrderMutation[(Box, BoxRotation)](new PoissonGenerator(1, rng), new PoissonGenerator(1, rng)),
//					new Replacement[jList[(Box, BoxRotation)]](candidateFactory, new Probability(0.1))
//					)
//				),
//				new RotationMutation,
//				0.9
//				)

		// test
		val pipelineWithRotation = new SplitEvolution(
				new EvolutionPipeline(List(
					new ListOrderCrossover2[(Box, BoxRotation)](crossoverProbability),
					new ListOrderMutation[(Box, BoxRotation)](new PoissonGenerator(1, rng), new PoissonGenerator(1, rng)),
					new RotationMutation(new Probability(0.3)),
					new StackRotationMutation(new Probability(0.2)),
					new GroupingMutation(new Probability(0.3))
					)
				),
				new Replacement[jList[(Box, BoxRotation)]](candidateFactory, new Probability(0.7)),
				0.9
			)
		
		
		val pipelineWithoutRotation = new SplitEvolution(
				new EvolutionPipeline(List(
					new ListOrderCrossover2[(Box, BoxRotation)](crossoverProbability),
					new ListOrderMutation[(Box, BoxRotation)](new PoissonGenerator(1, rng), new PoissonGenerator(1, rng)),
					new RotationMutation(new Probability(0.3))
					)
				),
				new Replacement[jList[(Box, BoxRotation)]](candidateFactory, new Probability(0.7)),
				0.9
			)
		
		// ---
		// TEST mit Mutation als Hauptoperator:
		
//		val pipelineWithRotation = new SplitEvolution(
//			new SplitEvolution(
//					new RotationMutation, // TODO parameter bei rotation adden
//					new ListOrderMutation[(Box, BoxRotation)](new PoissonGenerator(1, rng), new PoissonGenerator(1, rng))
//					, 0.5
//					),
//			new ListOrderCrossover[(Box, BoxRotation)](crossoverProbability),
//			0.1
//			)
//		
//		val pipelineWithoutRotation = new SplitEvolution(
//			new ListOrderMutation[(Box, BoxRotation)],
//			new ListOrderCrossover[(Box, BoxRotation)](crossoverProbability),
//			0.9
//			)
						

		
		val fitnessEvaluator = new CachingFitnessEvaluator(new PackingEvaluator(problem))
		
		if (islands.isDefined) {
			val islandConfig = islands.get
			
//			// multiple islands without differences
//			val islandEvolution = new IslandEvolution[jList[(Box, BoxRotation)]](
//					10,
//					new RingMigration,
//					candidateFactory,
//					pipelineWithRotation,
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

			islandEvolution.evolve(populationSize, eliteCount, islandConfig.epochLength, islandConfig.migrantCount, termination:_*)					
		} else {

			val engine = new GenerationalEvolutionEngine[jList[(Box, BoxRotation)]](
					candidateFactory,
					pipelineWithRotation,
					fitnessEvaluator,
					selectionStrategy,
					rng
					)
			
			listeners.foreach(engine.addEvolutionObserver(_))
			
			engine.evolve(populationSize, eliteCount, termination:_*)		
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