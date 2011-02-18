package ea.containerloading

import scala.collection.JavaConversions._
import java.util.{List => jList, ArrayList => jArrayList}
import java.util.Random
import org.uncommons.watchmaker.framework.EvolutionaryOperator
import org.uncommons.maths.number.{NumberGenerator, ConstantGenerator}
import org.uncommons.maths.random.Probability

/**
 * Mutation: Einzel-Rotation
 */
class RotationMutation(mutationProbability: NumberGenerator[Probability]) extends EvolutionaryOperator[jList[(Box, BoxRotation)]] {

	def this(mutationProbability: Probability) = this(new ConstantGenerator(mutationProbability))
	
	def apply(selectedCandidates: jList[jList[(Box, BoxRotation)]], rng: Random): jList[jList[(Box, BoxRotation)]] = {
		
		val result = new jArrayList[jList[(Box, BoxRotation)]](selectedCandidates.size)
		for (candidate <- selectedCandidates) {
			val newCandidate = new jArrayList(candidate)
			
			if (mutationProbability.nextValue.nextEvent(rng)) {
				// mutate just one box's rotation for now
				val boxIndex = rng.nextInt(newCandidate.size)
				val box = newCandidate.get(boxIndex)._1
				
				val rotationsCount = box.constraints.allowedRotations.size
				val rotationIndex = rng.nextInt(rotationsCount)
				val newRotation = box.constraints.allowedRotations(rotationIndex)
	
				newCandidate.set(boxIndex, (box, newRotation))
			}
			
			result.add(newCandidate)
		}
		
		result
	}
	
}