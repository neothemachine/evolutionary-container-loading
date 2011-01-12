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
	
	/**
	 * O(n^2), with the help of Nikita Rybak, see link:
	 * http://stackoverflow.com/questions/4656706/how-to-find-same-value-rectangular-areas-of-a-given-size-in-a-matrix-most-efficie/4657191#4657191
	 */
	def findFlatSurfaces(matrix: Array[Array[Int]], surfaceSize: Dimension2D, maxHeight: Int): List[Position2D] = {
		
		val matrixHeight = matrix.length
		val matrixWidth = matrix(0).length
		var resultPositions: List[Position2D] = Nil
		
		val a: Array[Array[Int]] = Array.ofDim(matrixHeight, matrixWidth)
		
		for (y <- matrixHeight - 2 to 0 by -1;
		     x <- 0 until matrixWidth)
		{
			val yx = matrix(y)(x)
			if (yx > maxHeight) {
				a(y)(x) = -1
			} else if (yx == matrix(y+1)(x)) {
				a(y)(x) = a(y+1)(x) + 1
			}
		}
				
		for (y <- 0 until matrixHeight) {
			var current_width = 0
			for (x <- 0 until matrixWidth) {
				val ayx = a(y)(x)
				if (ayx == -1 || ayx < surfaceSize.height - 1) {
					// this column has different numbers in it, no game
					current_width = 0
				} else if (current_width > 0 && matrix(y)(x) != matrix(y)(x-1)) {
					// this column should consist of the same numbers as the one before
					current_width = 0
				} else {
					current_width += 1
					if (current_width >= surfaceSize.width) {
						resultPositions ::= Position2D(x - surfaceSize.width + 1, y)
					}
				}
			}
		}

		return resultPositions
	}
	
//	/**
//	 * Ansatz von Daniel C. Sobral mit Hilfe von dynamic programming
//	 * http://stackoverflow.com/questions/4656706/how-to-find-same-value-rectangular-areas-of-a-given-size-in-a-matrix-most-efficie/4662246#4662246
//	 * 
//	 * allerdings sehr langsam und sehr hoher Speicherverbrauch
//	 */
//	def findFlatSurfaces(matrix: Array[Array[Int]], surfaceSize: Dimension2D, maxHeight: Int): Seq[Position2D] = {
//		rectanglesOf(surfaceSize.width, surfaceSize.height, matrix)
//	}
//	
//	def computeSquareSize[T](grid: Array[Array[T]]) = {
//	    val output = Array.fill[Set[Dimension2D]](grid.length, grid(0).length)(Set(Dimension2D(1, 1)))
//	
//	    def addLeft(x: Int, y: Int) = if (x > 0 && grid(y)(x) == grid(y)(x - 1)) 
//	        output(y)(x) ++= output(y)(x - 1) map (squareSize => squareSize copy (width = squareSize.width + 1))
//	
//	    def addUp(x: Int, y: Int) = if (y > 0 && grid(y)(x) == grid(y - 1)(x))
//	        output(y)(x) ++= output(y - 1)(x) map (squareSize => squareSize copy (height = squareSize.height + 1))
//	
//	    def delLeft(x: Int, y: Int) = if (x > 0 && grid(y)(x) != grid(y)(x - 1))
//	        output(y)(x) = output(y)(x) filter (_.width == 1)
//	
//	    def delUp(x: Int, y: Int) = if (y > 0 && grid(y)(x) != grid(y - 1)(x))
//	        output(y)(x) = output(y)(x) filter (_.height == 1)
//	
//	    for {
//	        y <- grid.indices
//	        x <- grid(y).indices
//	    } {
//	        addLeft(x, y)
//	        addUp(x, y)
//	        delLeft(x, y)
//	        delUp(x, y)
//	    }
//	
//	    output
//	}
//
//	def rectanglesOf[T](width: Int, height: Int, grid: Array[Array[T]]): Seq[Position2D] = {
//	    val squareSizes = computeSquareSize(grid)
//	    val minSquare = Dimension2D(height = height, width = width)
//	    val result = for {
//	        y <- squareSizes.indices
//	        x <- squareSizes(y).indices
//	        sq <- squareSizes(y)(x) filter (minSquare ==)
//	    } yield Position2D(x - width + 1, y - height + 1)
//	    result
//	}

	
	
//	/**
//	 * Original approach with O(n^4)
//	 */
//	def findFlatSurfaces(matrix: Array[Array[Int]], surfaceSize: Dimension2D, maxHeight: Int): List[Position2D] = {
//		
//		val matrixHeight = matrix.length
//		val matrixWidth = matrix(0).length
//		var resultPositions: List[Position2D] = Nil
//		
//		for (y <- 0 to matrixHeight - surfaceSize.height) {
//			var x = 0
//			while (x <= matrixWidth - surfaceSize.width) {
//				val topLeft = matrix(y)(x)
//				val topRight = matrix(y)(x + surfaceSize.width - 1)
//				val bottomLeft = matrix(y + surfaceSize.height - 1)(x)
//				val bottomRight = matrix(y + surfaceSize.height - 1)(x + surfaceSize.width - 1)
//				// investigate further if corners are equal
//				if (topLeft <= maxHeight && 
//					topLeft == bottomLeft && topLeft == topRight && topLeft == bottomRight) {
//					breakable {
//						for (sx <- x until x + surfaceSize.width;
//						     sy <- y until y + surfaceSize.height) {
//							if (matrix(sy)(sx) != topLeft) {
//								x = if (x == sx) sx + 1 else sx 
//								break
//							}
//						}
//						// found one!
//						resultPositions ::= Position2D(x, y)
//						x += 1
//					}
//				} else if (topRight != bottomRight) {
//					// can skip x a bit as there won't be a valid match in current row in this area
//					x += surfaceSize.width 
//				} else {
//					x += 1
//				}
//			}	
//		}
//		
//		return resultPositions
//	}
	
}