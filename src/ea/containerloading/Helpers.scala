package ea.containerloading

import util.control.Breaks._

case class Dimension2D(width: Int, height: Int) {
	def area = width * height
}
case class Dimension3D(width: Int, height: Int, depth: Int) {
	def volume = width * height * depth
}
case class Position2D(x: Int, y: Int)
case class Position3D(x: Int, y: Int, z: Int) // TODO Bezugspunkt muss klar sein!

object Helpers {

	def findFlatSurfaces(matrix: Array[Array[Int]], surfaceSize: Dimension2D): List[Position2D] = {
		
		val matrixWidth = matrix(0).length
		val matrixHeight = matrix.length
		var resultPositions: List[Position2D] = Nil
		
		for (y <- 0 to matrixHeight - surfaceSize.height) {
			var x = 0
			while (x <= matrixWidth - surfaceSize.width) {
				val topLeft = matrix(y)(x)
				val topRight = matrix(y)(x + surfaceSize.width - 1)
				val bottomLeft = matrix(y + surfaceSize.height - 1)(x)
				val bottomRight = matrix(y + surfaceSize.height - 1)(x + surfaceSize.width - 1)
				// investigate further if corners are equal
				if (topLeft == bottomLeft && topLeft == topRight && topLeft == bottomRight) {
					breakable {
						for (sx <- x until x + surfaceSize.width;
						     sy <- y until y + surfaceSize.height) {
							if (matrix(sy)(sx) != topLeft) {
								x = if (x == sx) sx + 1 else sx 
								break
							}
						}
						// found one!
						resultPositions ::= Position2D(x, y)
						x += 1
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