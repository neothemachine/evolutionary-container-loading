package ea.containerloading

import util.control.Breaks._

case class Dimension2D(width: Int, height: Int) {
	def area = (width: Long) * (height: Long)
}
case class Dimension3D(width: Int, height: Int, depth: Int) {
	def volume = (width: Long) * (height: Long) * (depth: Long)
}
case class Position2D(x: Int, y: Int)
case class Position3D(x: Int, y: Int, z: Int)

object Helpers {

	def findFlatSurfaces(matrix: Array[Array[Int]], surfaceSize: Dimension2D, maxHeight: Int): List[Position2D] = {
		
		val matrixWidth = matrix.length
		val matrixHeight = matrix(0).length
		var resultPositions: List[Position2D] = Nil
		
		for (y <- 0 to matrixHeight - surfaceSize.height) {
			var x = 0
			while (x <= matrixWidth - surfaceSize.width) {
				val topLeft = matrix(x)(y)
				val topRight = matrix(x + surfaceSize.width - 1)(y)
				val bottomLeft = matrix(x)(y + surfaceSize.height - 1)
				val bottomRight = matrix(x + surfaceSize.width - 1)(y + surfaceSize.height - 1)
				// investigate further if corners are equal
				if (topLeft <= maxHeight && 
					topLeft == bottomLeft && topLeft == topRight && topLeft == bottomRight) {
					breakable {
						for (sx <- x until x + surfaceSize.width;
						     sy <- y until y + surfaceSize.height) {
							if (matrix(sx)(sy) != topLeft) {
								x = if (x == sx) sx + 1 else sx 
								break
							}
						}
						// found one!
						// TODO stop after first is found (because of performance)
						return List(Position2D(x,y))
						
//						resultPositions ::= Position2D(x, y)
//						x += 1
					}
				} else if (topRight != bottomRight) {
					// can skip x a bit as there won't be a valid match in current row in this area
					x += surfaceSize.width 
				} else {
					x += 1
				}
			}	
		}
		
		return resultPositions
	}
	
}