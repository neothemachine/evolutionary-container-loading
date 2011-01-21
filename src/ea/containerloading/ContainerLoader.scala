package ea.containerloading

import javax.media.j3d._
import javax.vecmath._
import util.control.Breaks._
import scala.math._

import Kevin._

case class LoadedContainer(container: Container, loadedBoxes: Seq[LoadedBox], skippedBoxes: Seq[Box])
case class LoadedBox(box: Box, rotation: Rotation3D, position: Position3D)

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
	 * - schon ab 100x100x100 ist der Algorithmus zu langsam
	 * - Kistenrotation wird nicht durchgeführt, da zu langsam
	 * - keine Kriterien für Bevorzugung "guter" Plätze, z.B. maximale Berührungsfläche
	 */
	def loadLayer(container: Container, boxLoadingOrder: Seq[Box]): LoadedContainer = {
				
		val layer = Array.ofDim[Int](container.size.depth, container.size.width)

		var loadedBoxes: List[LoadedBox] = Nil
		var skippedBoxes: List[Box] = Nil
		var stopLoading = false
		for (box <- boxLoadingOrder) {
			if (!stopLoading) {
				val maxHeight = container.size.height - box.size.height
				var possiblePositions = 
					Helpers.findFlatSurfaces(layer, new Dimension2D(box.size.width, box.size.depth), maxHeight)
				
				var rotation = Rotation3D(false, false, false)
					
				if (possiblePositions.isEmpty) {
					// try to rotate the box to fit it in
					breakable {
						val rotations = boolVariations
						val allowedRotations = rotations.filter { case (x,y,z) =>
							if (!x && x == y && y == z) {
								false
							} else if (x && !box.constraints.allowedRotations.x) {
								false
							} else if (y && !box.constraints.allowedRotations.y) {
								false
							} else if (z && !box.constraints.allowedRotations.z) {
								false
							} else {
								true
							}
						}

						for ((rotateX, rotateY, rotateZ) <- allowedRotations) {
							rotation = Rotation3D(rotateX, rotateY, rotateZ) 
							val rotatedBoxSize = rotation.rotateCuboid(box.size)
							val maxHeight = container.size.height - rotatedBoxSize.height
							
							possiblePositions = 
								Helpers.findFlatSurfaces(
										layer, 
										new Dimension2D(rotatedBoxSize.width, rotatedBoxSize.depth),
										maxHeight)
										
							if (!possiblePositions.isEmpty) {
								break
							}
						}
					}
				}
					
				if (possiblePositions.isEmpty) {
					stopLoading = true
					skippedBoxes ::= box
				} else {
//					val minHeightPosition = possiblePositions.min(new Ordering[Position2D] {
//						def compare(first: Position2D, second: Position2D): Int = {
//							layer(first.y)(first.x) compare layer(second.y)(second.x)
//						}
//					})
					val firstPosition = possiblePositions(0)
					//val firstPosition = minHeightPosition
					val x = firstPosition.x
					val z = firstPosition.y
					val y = layer(z)(x)
					
					/*
					 * TODO
					 * Die Berührungsfläche lässt sich leicht berechnen mit Hilfe des Layers!
					 * Es müssen einfach alle um die Boxposition umliegenden Höhenwerte betrachtet
					 * werden -- mit Sonderbehandlung für die Containerwände.
					 * Dafür müssen natürlich alle surfaces gefunden werden, statt nur einem. 
					 * => findFlatSurfaces ist der Flaschenhals hier -> muss schneller werden!
					 * 
					 * Rotation könnte in den Fällen angewandt werden, in denen es nicht möglich ist,
					 * eine Box zu positionieren.
					 */
										
					loadedBoxes ::= LoadedBox(box, rotation, Position3D(x,y,z))
					// adjust layer -> add box height to surface
					val rotatedBoxSize = rotation.rotateCuboid(box.size)
					for (layerX <- x until x + rotatedBoxSize.width;
					     layerY <- z until z + rotatedBoxSize.depth) {
						layer(layerY)(layerX) += rotatedBoxSize.height
					}
				}
			} else {
				skippedBoxes ::= box
			}
		}
		
		new LoadedContainer(container, loadedBoxes, skippedBoxes)
	}
	
//	/**
//	 * Kevin
//	 */
//	def loadLayer(container: Container, boxLoadingOrder: List[Box]): LoadedContainer = {
//				
//		var layer: Kevin.Grid[Int] = List.fill(container.size.depth, container.size.width)(0)
//
//		var loadedBoxes: List[LoadedBox] = Nil
//		var skippedBoxes: List[Box] = Nil
//		var stopLoading = false
//		for (box <- boxLoadingOrder) {
//			if (!stopLoading) {
//				val maxHeight = container.size.height - box.size.height
//				val possiblePositions = 
//					Helpers.findFlatSurfacesKevin(layer, new Dimension2D(box.size.width, box.size.depth), maxHeight)
//									
//				if (possiblePositions.isEmpty) {
//					stopLoading = true
//					skippedBoxes ::= box
//				} else {
//					val firstPosition = possiblePositions(0)
//					val x = firstPosition._1.x
//					val z = firstPosition._1.y
//					val y = firstPosition._2
//					
//					loadedBoxes ::= LoadedBox(box, Position3D(x,y,z))
//					
//					// adjust layer -> add box height to surface
//					layer = layer.deepMapRange(x, y, box.size.width, box.size.depth){ h => y + box.size.height }
//				}
//			} else {
//				skippedBoxes ::= box
//			}
//		}
//		
//		new LoadedContainer(container, loadedBoxes, skippedBoxes)
//	}
	
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
//		// TODO wie kann die Fläche der angrenzenden Boxen ermittelt werden??
//		
//		new LoadedContainer(Set(), Set())
//		
//	}
	
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
	
	private val boolVariations: List[(Boolean, Boolean, Boolean)] = {
		var list = List[(Boolean, Boolean, Boolean)]()
		for {
			b1 <- List(true, false)
			b2 <- List(true, false)
			b3 <- List(true, false)
		} {
			list ::= (b1, b2, b3)
		}
		list
	}
}


