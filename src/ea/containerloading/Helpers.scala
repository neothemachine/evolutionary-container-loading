package ea.containerloading

import util.control.Breaks._

object Helpers {

	/**
	 * O(n^2), with the help of Nikita Rybak, see link:
	 * http://stackoverflow.com/questions/4656706/how-to-find-same-value-rectangular-areas-of-a-given-size-in-a-matrix-most-efficie/4657191#4657191
	 */
	def findFlatSurfaces(matrix: Array[Array[Int]], surfaceSize: Dimension2D, maxHeight: Int): List[Position2D] = {
		
		val matrixHeight = matrix.length
		val matrixWidth = matrix(0).length
		var resultPositions: List[Position2D] = Nil
		
		val a = Array.ofDim[Int](matrixHeight, matrixWidth)
		
		for {
			y <- matrixHeight - 2 to 0 by -1
		    x <- 0 until matrixWidth
		} {
			if (matrix(y)(x) > maxHeight) {
				a(y)(x) = -1
			} else if (matrix(y)(x) == matrix(y+1)(x)) {
				a(y)(x) = a(y+1)(x) + 1
			}
		}
				
		for (y <- 0 until matrixHeight) {
			var current_width = 0
			for (x <- 0 until matrixWidth) {
				if (a(y)(x) == -1 || a(y)(x) < surfaceSize.height - 1) {
					// this column has different numbers in it, no game
					current_width = 0
				} else if (current_width > 0 && matrix(y)(x) != matrix(y)(x-1)) {
					// this column should consist of the same numbers as the one before
					current_width = 0
				} else {
					current_width += 1
					if (current_width >= surfaceSize.width) {
						// TODO im Moment wird first fit benutzt -> nur 1 Ergebnis notwendig!
						//resultPositions ::= Position2D(x - surfaceSize.width + 1, y)
						return List(Position2D(x - surfaceSize.width + 1, y))
					}
				}
			}
		}

		return resultPositions
	}
	
//	/**
//	 * direkte funktionale Umsetzung von Nikita's Algo, trotzdem zu langsam 
//	 */
//	def findFlatSurfacesDaniel(matrix: Array[Array[Int]], surfaceSize: Dimension2D, maxHeight: Int) = {
//		val rectangles = Daniel.findRectangles(surfaceSize.height, surfaceSize.width , matrix)
//    	rectangles filter (r => matrix(r.y)(r.x) <= surfaceSize.height )
//	}
	
//	/**
//	 * optimierte funktionale Umsetzung von Nikita's Algo
//	 */
//	def findFlatSurfacesDaniel29(matrix: Array[Array[Int]], surfaceSize: Dimension2D, maxHeight: Int) = {
//		val rectangles = Daniel29.findRectangles(surfaceSize.height, surfaceSize.width , matrix)
//    	rectangles filter (r => matrix(r.y)(r.x) <= surfaceSize.height )
//	}
	
//	
//	/**
//	 * pure functional approach, but too slow without parallelization using Scala 2.9 and GPU support
//	 */
//	def findFlatSurfacesKevin(matrix: Kevin.Grid[Int], surfaceSize: Dimension2D, maxHeight: Int) = {
//		val rectangles = Kevin.findAllRectangles(matrix)
//    	val filtered = rectangles filter { 
//			case (rect,value,pos) => 
//				value <= maxHeight && rect.w >= surfaceSize.width && rect.h >= surfaceSize.height 
//		}
//    	filtered.distinct map { case (rect,value,pos) => (Position2D(pos._1, pos._2), value) }
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