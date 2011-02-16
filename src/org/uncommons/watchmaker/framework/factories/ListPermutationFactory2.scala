package org.uncommons.watchmaker.framework.factories

import java.util.{List => jList, ArrayList => jArrayList, Random}
import org.uncommons.maths.random.Probability

/**
 * Extension of original ListPermutationFactory
 * returns the original unchanged permutation with a given probability
 * Reason: original order might be more optimal as a start
 * 
 * results: didn't pay off much / at all
 *
 */
class ListPermutationFactory2[T](elements: jList[T], probabilityInitialPermutation: Probability) 
extends ListPermutationFactory[T](elements) {
	
	private var count = 0

	override def generateRandomCandidate(rng: Random): jList[T] = 
		if (count <= 5 && probabilityInitialPermutation.nextEvent(rng)) {
			count += 1
			new jArrayList(elements)
		} else
			super.generateRandomCandidate(rng)
		
}