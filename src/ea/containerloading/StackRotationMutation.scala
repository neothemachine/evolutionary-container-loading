package ea.containerloading

import scala.collection.JavaConversions._
import java.util.{List => jList, ArrayList => jArrayList}
import java.util.Random
import org.uncommons.watchmaker.framework.EvolutionaryOperator
import org.uncommons.maths.number.{NumberGenerator, ConstantGenerator}
import org.uncommons.maths.random.Probability

/**
 * Mutation: Typ-Rotation
 * 
 * Selects a random box and rotates it and all boxes of the same dimension/type.
 * 
*/
class StackRotationMutation(mutationProbability: NumberGenerator[Probability]) extends EvolutionaryOperator[jList[(Box, BoxRotation)]] {
	
	def this(mutationProbability: Probability) = this(new ConstantGenerator(mutationProbability))
	
	def apply(selectedCandidates: jList[jList[(Box, BoxRotation)]], rng: Random): jList[jList[(Box, BoxRotation)]] = {
		
		val result = new jArrayList[jList[(Box, BoxRotation)]](selectedCandidates.size)
		for (candidate <- selectedCandidates) {
			
			val newCandidate = new jArrayList(candidate)
			
			if (mutationProbability.nextValue.nextEvent(rng)) {
				val box = candidate.get(rng.nextInt(candidate.size))._1
							
				val rotationsCount = box.constraints.allowedRotations.size
				val newRotation = box.constraints.allowedRotations(rng.nextInt(rotationsCount))
				
				var index = 0
				while (index <= candidate.length - 1) {
					val boxToTest = candidate.get(index)._1
					if (boxToTest.size equals box.size) {
						newCandidate.set(index, (boxToTest, newRotation))	
					}
					index += 1
				}
			}
			
			result.add(newCandidate)
		}
		
		result
	}
}