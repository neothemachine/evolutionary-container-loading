package ea.containerloading

import org.uncommons.watchmaker.framework.selection._
import org.uncommons.watchmaker.framework._
import org.uncommons.maths.random.Probability
import scala.collection.JavaConversions._
import ea.containerloading.vis._

object Main {
	def main(args : Array[String]) : Unit = {

//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(50, 50, 50),
//			boxSizeFrequencies =
//				Map(Dimension3D(10,10,10) -> 5,
//			        Dimension3D(20,10,10) -> 15,
//			        Dimension3D(30,10,20) -> 10,
//			        Dimension3D(50,10,10) -> 5))
	
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(10, 10, 10),
//			boxSizeFrequencies =
//				Map(Dimension3D(10,10,5) -> 2))
		
//		// thpack9 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(6, 10, 10),
//			boxSizeFrequencies =
//				Map(Dimension3D(6,8,2) -> 20,
//			        Dimension3D(4,10,8) -> 50))
		
		// thpack9 - 47
		val problem = new ContainerProblem(
			containerSize = Dimension3D(60, 43, 25),
			boxSizeFrequencies =
				Map(Dimension3D(13,11,21) -> 25,
			        Dimension3D(13,19,11) -> 20,
			        Dimension3D(14,6,10)  -> 20,
			        Dimension3D(13,5,8)   -> 34))		
		
//		// thpack1 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> 40,
//			        Dimension3D(43,25,110) -> 33,
//			        Dimension3D(81,55,92)  -> 39))
			
//		// thpack2 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> 24,
//			        Dimension3D(43,25,110) -> 7,
//			        Dimension3D(81,55,92)  -> 22,
//			        Dimension3D(33,28,81)  -> 13,
//			        Dimension3D(99,73,120) -> 15))
		
//		// thpack3 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> 24,
//			        Dimension3D(43,25,110) -> 9,
//			        Dimension3D(81,55,92)  -> 8,
//			        Dimension3D(33,28,81)  -> 11,
//			        Dimension3D(99,73,120) -> 11,
//			        Dimension3D(70,48,111) -> 10,
//			        Dimension3D(72,46,98)  -> 12,
//			        Dimension3D(66,31,95)  -> 9))
		
//		// thpack7 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(233, 220, 587),
//			boxSizeFrequencies =
//				Map(Dimension3D(76,30,108) -> 10,
//			        Dimension3D(43,25,110) -> 6,
//			        Dimension3D(81,55,92)  -> 5,
//			        Dimension3D(33,28,81)  -> 5,
//			        Dimension3D(99,73,120) -> 6,
//			        Dimension3D(70,48,111) -> 4,
//			        Dimension3D(72,46,98)  -> 7,
//			        Dimension3D(66,31,95)  -> 6,
//			        Dimension3D(84,30,85)  -> 5,
//			        Dimension3D(32,25,71)  -> 5,
//			        Dimension3D(34,25,36)  -> 4,
//			        Dimension3D(67,62,97)  -> 8,
//			        Dimension3D(25,23,33)  -> 2,
//			        Dimension3D(27,26,95)  -> 5,
//			        Dimension3D(81,44,94)  -> 8,
//			        Dimension3D(39,38,41)  -> 4,
//			        Dimension3D(74,65,104) -> 4,
//			        Dimension3D(41,36,52)  -> 4,
//			        Dimension3D(78,34,104) -> 6,
//			        Dimension3D(77,46,83)  -> 6))
		
//		// thpack8 - 1
//		val problem = new ContainerProblem(
//			containerSize = Dimension3D(2000, 1000, 3000),
//			boxSizeFrequencies =
//				Map(Dimension3D(375,300,400)  -> 24,
//			        Dimension3D(400,150,400)  -> 10,
//			        Dimension3D(150,200,300)  -> 11,
//			        Dimension3D(200,275,900)  -> 14,
//			        Dimension3D(150,275,800)  -> 6,
//			        Dimension3D(150,200,1500) -> 20,
//			        Dimension3D(200,200,900)  -> 15))
		
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
		println("skipped: " + loadingResult.skippedBoxes.length)
		
		CandidateViewer.showCandidate(loadingResult)
	}
}
