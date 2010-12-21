package ea.containerloading

import org.uncommons.watchmaker.framework.FitnessEvaluator
import scala.collection.JavaConversions._
import java.util.{List => jList}

class PackingEvaluator(problem: ContainerProblem) extends FitnessEvaluator[jList[Int]] {

	def getFitness(candidate: jList[Int], population: jList[_ <: jList[Int]]): Double = {
		
		val containerSize = problem.getContainerSize
				
		// jede Box kann um 90� gedreht werden -> Orientierung mit speichern
		
		// es gibt keine L�cken zw. 2 Boxen/Wand
		// -> eine Begrenzungsfl�che kann also von 2 oder mehr Boxen geteilt werden
		
		// wenn eine Box platziert wurde, alle 8 Eckpunkte berechnen und cachen
		// -> damit ist eine leichtere Kollisionspr�fung machbar
		
		// Problem: Pr�fen aller m�glichen Positionen f�r eine Box dauert zu lang
		
		/*
		 * Bedingungen:
		 * 1. Boxen sollen immer nur von oben "drauffallen", es d�rfen keine freien Stellen
		 *    zwischen Boxen belegt werden, die man normal nicht erreichen w�rde
		 *    -> dadurch ergibt sich eine Art Layer/Tuch, was sich �ber alle Boxen legt
		 *    -> vll hilfreich f�r effiziente Berechnung, da man sich diesen Layer merken
		 *    k�nnte und bei jeder neuen Box nur nach oben erweitern m�sste
		 * 2. Boxen d�rfen nicht in der Luft schweben, sondern m�ssen immer auf dem Boden
		 *    oder anderen Boxen loegen
		 * 3. Boxen sollen h�chstm�glichen Kontakt zu Containerw�nden und anderen Boxen haben
		 * 4. Unter einer Box soll es keine Freir�ume > Fl�che F geben (geeignet vorgeben im Problem)
		 * 5. Die Positionssuche wird mit normaler Orientierung und mit einer 90� Drehung
		 *    durchgef�hrt
		 *    -> beste Position aus beiden Orientierungen suchen
		 * 6. Falls eine Box nirgends platziert werden kann, werden keine weiteren Boxen
		 *    mehr platziert (und damit eine schlechte Fitness erreicht)
		 */
		
		return candidate.take(5).sum
	}
	
	def isNatural = true
}