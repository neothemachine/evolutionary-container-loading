package ea.containerloading

object ContainerLoader {
	
	def load(container: Container, boxLoadingOrder: List[Box]): LoadedContainer = {
				
		new LoadedContainer(Set(), Set())
		
	}
	
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
		
		/*
		 * Speicheraufwand:
		 * a) Layer: 2-dimensionales Int-Array mit width x depth vom Container
		 *    Jeder Wert stellt die H�he des Layers an der jeweiligen Position dar.
		 *    10Mb Java Heap bei 1000x1000
		 *    450Mb Java Heap bei 10000x10000
		 *    Bei http://people.brunel.ac.uk/~mastjjb/jeb/orlib/files/thpack1.txt
		 *    ist es im Bereich von 500x500, beim gr��ten Beispiel bis 6000x2800
		 *    -> immer noch relativ viel, problematisch bei paralleler Fitnessberechnung
		 *    
		 */
	
}

case class LoadedContainer(loadedBoxes: Set[LoadedBox], skippedBoxes: Set[Box])

case class LoadedBox(box: Box, position: Position, isRotated: Boolean)

// TODO Bezugspunkt muss klar sein!
case class Position(x: Int, y: Int, z: Int)
