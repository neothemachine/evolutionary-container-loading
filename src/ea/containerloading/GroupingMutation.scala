package ea.containerloading

import scala.collection.JavaConversions._
import java.util.{List => jList, ArrayList => jArrayList}
import java.util.Random
import org.uncommons.watchmaker.framework.EvolutionaryOperator
import org.uncommons.maths.number.{NumberGenerator, ConstantGenerator}
import org.uncommons.maths.random.Probability

/**
 * Mutation: Clusterbildung
 */
class GroupingMutation(mutationProbability: NumberGenerator[Probability]) extends EvolutionaryOperator[jList[(Box, BoxRotation)]] {

	def this(mutationProbability: Probability) = this(new ConstantGenerator(mutationProbability))
		
	def apply(selectedCandidates: jList[jList[(Box, BoxRotation)]], rng: Random): jList[jList[(Box, BoxRotation)]] = {
		
		val result = new jArrayList[jList[(Box, BoxRotation)]](selectedCandidates.size)
		for (candidate <- selectedCandidates) {
			
			val newCandidate = new jArrayList(candidate)
			
			if (mutationProbability.nextValue.nextEvent(rng)) {
				val boxIdx = rng.nextInt(candidate.size)
				
				val refBox = candidate.get(boxIdx)
				val refBoxDimension = refBox._1.size
				val refBoxRotation = refBox._2
				
				var freeIdx = -1
				for (index <- (boxIdx + 1) until newCandidate.size) {
					val box = newCandidate.get(index)
					if (box._1.size == refBoxDimension && box._2 == refBoxRotation) {
						if (freeIdx != -1) {							
							java.util.Collections.swap(newCandidate, freeIdx, index)
							freeIdx += 1
						}
					} else if (freeIdx == -1) {
						freeIdx = index
					}
				}
			}
			
			result.add(newCandidate)
		}
		
		result
	}
	
}