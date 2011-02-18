package ea.containerloading

import javax.media.j3d._
import javax.vecmath._
import util.control.Breaks._
import scala.math._

case class LoadedContainer(container: Container, loadedBoxes: Seq[LoadedBox], skippedBoxes: Seq[Box])
case class LoadedBox(box: Box, rotation: BoxRotation, position: Position3D)

object ContainerLoader {
	
	/**
	 * Beladung vom Container mit Layer-Ansatz (relativ langsam und speicherintensiv)
	 * 
	 * Eine Höhenkarte (Layer) wird als 2-Dimensionales Array gepflegt, sodass immer
	 * Positionen für Kisten gesucht werden, die von oben "drauffallen" können.
	 * Eine gültige Position für eine Kiste ist eine Fläche in der Karte mit der selben Höhe
	 * und mit Größe der Box.
	 * 
	 * Vorteile:
	 * - Kisten haben keine Hohlräume unter sich
	 * - Kisten werden immer von oben beladen
	 * 
	 * Nachteile:
	 * - keine Kriterien für Bevorzugung "guter" Plätze, z.B. maximale Berührungsfläche
	 */
	def loadLayer(container: Container, boxLoadingOrder: Seq[(Box, BoxRotation)]): LoadedContainer = {
				
		val layer = Array.ofDim[Int](container.size.depth, container.size.width)
		
		val surfaceFinder = new SurfaceFinder(layer, isFlat = true)

		var loadedBoxes: List[LoadedBox] = Nil
		var skippedBoxes: List[Box] = Nil
		var stopLoading = false
		for ((box, rotation) <- boxLoadingOrder) {
			if (!stopLoading) {
				
				val rotatedBoxSize = rotation.rotateDimensions(box.size)
				val maxHeight = container.size.height - rotatedBoxSize.height
								
				val possiblePositions = 
					surfaceFinder.findFlatSurfaces(rotatedBoxSize.width, rotatedBoxSize.depth, maxHeight)
				
				if (possiblePositions.isEmpty) {
					stopLoading = true
					skippedBoxes ::= box
				} else {		
					
					val firstPosition = possiblePositions(0)
					val x = firstPosition.x
					val z = firstPosition.y
					val y = layer(z)(x)
										
					loadedBoxes ::= LoadedBox(box, rotation, Position3D(x,y,z))

					surfaceFinder.updateArea(
							x, z, rotatedBoxSize.width, rotatedBoxSize.depth, y + rotatedBoxSize.height)
				}
			} else {
				skippedBoxes ::= box
			}
		}
		
		new LoadedContainer(container, loadedBoxes, skippedBoxes)
	}

	
		/*
		 * Speicheraufwand:
		 * a) Layer: 2-dimensionales Int-Array mit width x depth vom Container
		 *    Jeder Wert stellt die Höhe des Layers an der jeweiligen Position dar.
		 *    10Mb Java Heap bei 1000x1000
		 *    450Mb Java Heap bei 10000x10000
		 *    Bei http://people.brunel.ac.uk/~mastjjb/jeb/orlib/files/thpack1.txt
		 *    ist es im Bereich von 500x500, beim größten Beispiel bis 6000x2800
		 *    -> immer noch relativ viel, problematisch bei paralleler Fitnessberechnung
		 *    
		 */

}


