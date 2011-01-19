package ea.containerloading

import scala.actors.Futures.future

/**
 * http://stackoverflow.com/questions/4656706/how-to-find-same-value-rectangular-areas-of-a-given-size-in-a-matrix-most-efficie/4697998#4697998
 * efficient functional version
 * not possible on Scala 2.8.1 -> scanLeft on Iterator missing
 */
object Daniel29 {
	
//	def getGridHeights(grid: Array[Array[Int]]) = (
//	    grid
//	    .sliding(2)
//	    .scanLeft(Array.fill(grid.head.size)(1)) { case (currHeightArray, Array(prevRow, nextRow)) =>
//	        (prevRow, nextRow, currHeightArray)
//	        .zipped
//	        .map { case (x, y, currHeight) =>  if (x == y) currHeight + 1 else 1 }
//	    }
//	)
//	
//	def computeWidths(height: Int, row: Array[Int], heightRow: Array[Int]) = (
//	    row
//	    .sliding(2)
//	    .map { case Array(x, y) => x == y }
//	    .zip(heightRow)
//	    .scanLeft(1) { case (currWidth , (isConsecutive, currHeight)) =>
//	        if (currHeight >= height && currWidth > 0 && isConsecutive) currWidth + 1
//	        else 1
//	    }
//	    .toArray
//	)
//
//	def getGridWidths(height: Int, grid: Array[Array[Int]]) = (
//	    grid
//	    .iterator
//	    .zip(getGridHeights(grid))
//	    .map { case (row, heightsRow) => future(computeWidths(height, row, heightsRow)) }
//	    .map(_())
//	    .toArray
//	)
//
//	def findRectangles(height: Int, width: Int, grid: Array[Array[Int]]) = {
//	    val gridWidths = getGridWidths(height, grid)
//	    for {
//	        y <- gridWidths.indices
//	        x <- gridWidths(y).indices
//	        if gridWidths(y)(x) >= width
//	    } yield Position2D(x, y)
//	}
}