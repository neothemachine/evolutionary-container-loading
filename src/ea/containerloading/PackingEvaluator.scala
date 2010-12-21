package ea.containerloading

import org.uncommons.watchmaker.framework.FitnessEvaluator
import scala.collection.JavaConversions._
import java.util.{List => jList}

class PackingEvaluator(problem: ContainerProblem) extends FitnessEvaluator[jList[Int]] {

	def getFitness(candidate: jList[Int], population: jList[_ <: jList[Int]]): Double = {
		
		val containerSize = problem.getContainerSize
				
		// jede Box kann um 90° gedreht werden -> Orientierung mit speichern
		
		// es gibt keine Lücken zw. 2 Boxen/Wand
		// -> eine Begrenzungsfläche kann also von 2 oder mehr Boxen geteilt werden
		
		// wenn eine Box platziert wurde, alle 8 Eckpunkte berechnen und cachen
		// -> damit ist eine leichtere Kollisionsprüfung machbar
		
		// Problem: Prüfen aller möglichen Positionen für eine Box dauert zu lang
		
		/*
		 * Bedingungen:
		 * 1. Boxen sollen immer nur von oben "drauffallen", es dürfen keine freien Stellen
		 *    zwischen Boxen belegt werden, die man normal nicht erreichen würde
		 *    -> dadurch ergibt sich eine Art Layer/Tuch, was sich über alle Boxen legt
		 *    -> vll hilfreich für effiziente Berechnung, da man sich diesen Layer merken
		 *    könnte und bei jeder neuen Box nur nach oben erweitern müsste
		 * 2. Boxen dürfen nicht in der Luft schweben, sondern müssen immer auf dem Boden
		 *    oder anderen Boxen loegen
		 * 3. Boxen sollen höchstmöglichen Kontakt zu Containerwänden und anderen Boxen haben
		 * 4. Unter einer Box soll es keine Freiräume > Fläche F geben (geeignet vorgeben im Problem)
		 * 5. Die Positionssuche wird mit normaler Orientierung und mit einer 90° Drehung
		 *    durchgeführt
		 *    -> beste Position aus beiden Orientierungen suchen
		 * 6. Falls eine Box nirgends platziert werden kann, werden keine weiteren Boxen
		 *    mehr platziert (und damit eine schlechte Fitness erreicht)
		 */
		
		return candidate.take(5).sum
	}
	
	def isNatural = true
}