package ea.containerloading

import javax.media.j3d._
import javax.vecmath._
import util.control.Breaks._

object ContainerLoader {
	
	/**
	 * Beladung vom Container mit Layer-Ansatz (relativ langsam und speicherintensiv)
	 * 
	 * Eine H�henkarte (Layer) wird als 2-Dimensionales Array gepflegt, sodass immer
	 * Positionen f�r Kisten gesucht werden, die von oben "drauffallen" k�nnen.
	 * Eine g�ltige Position f�r eine Kiste ist eine Fl�che in der Karte mit der selben H�he
	 * und mit Gr��e der Box.
	 * 
	 * Vorteile:
	 * - Kisten haben keine Hohlr�ume unter sich
	 * - Kisten werden immer von oben beladen
	 * 
	 * Nachteile:
	 * - schon ab 100x100x100 ist der Algorithmus zu langsam und zu speicherintensiv
	 * - Kistenrotation wird nicht durchgef�hrt, da zu langsam
	 * - keine Kriterien f�r Bevorzugung "guter" Pl�tze, z.B. maximale Ber�hrungsfl�che
	 */
	def loadLayer(container: Container, boxLoadingOrder: List[Box]): LoadedContainer = {
				
		val layer: Array[Array[Int]] = Array.ofDim(container.size.width, container.size.depth)
		// FIXME boxes �berschneiden sich, irgendwo ist noch ein Fehler!
		var loadedBoxes: List[LoadedBox] = Nil
		var skippedBoxes: List[Box] = Nil
		var stopLoading = false
		for (box <- boxLoadingOrder) {
			if (!stopLoading) {
				val maxHeight = container.size.height - box.size.height
				val possiblePositions = 
					Helpers.findFlatSurfaces(layer, new Dimension2D(box.size.width, box.size.depth), maxHeight)
				
//				val possiblePositions =	positions.filter(pos => 
//					layer(pos.x)(pos.y) + box.size.height <= container.size.height)
					
				if (possiblePositions.isEmpty) {
					stopLoading = true
					skippedBoxes ::= box
				} else {
					val firstPosition = possiblePositions(0)
					val x = firstPosition.x
					val z = firstPosition.y
					val y = layer(x)(z)
										
					loadedBoxes ::= LoadedBox(box, Position3D(x,y,z))
					// adjust layer -> add box height to surface
					for (layerX <- x until x + box.size.width;
					     layerY <- z until z + box.size.depth) {
						layer(layerX)(layerY) += box.size.height
					}
				}
			} else {
				skippedBoxes ::= box
			}
		}
		
		new LoadedContainer(container, loadedBoxes, skippedBoxes)
	}
	
//	def load(container: Container, boxLoadingOrder: List[Box]): LoadedContainer = {
//				
//		//val layer = Array.ofDim(container.size.width, container.size.depth)
//		
//		
//		 
//		val box = new BoundingBox(new Point3d(0,0,0), new Point3d(10,10,10))
//		val box2 = new BoundingBox(new Point3d(0,0,9), new Point3d(20,20,20))
//		val i = box.intersect(box2)
//		
//		// TODO wie kann die Fl�che der angrenzenden Boxen ermittelt werden??
//		
//		new LoadedContainer(Set(), Set())
//		
//	}
	
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

case class LoadedContainer(container: Container, loadedBoxes: List[LoadedBox], skippedBoxes: List[Box])

case class LoadedBox(box: Box, position: Position3D) // TODO isRotated reicht nicht