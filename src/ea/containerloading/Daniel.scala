package ea.containerloading

import scala.actors.Futures.future

object Daniel {

	private def computeHeights(column: Array[Int]) = (
	    column
	    .reverse
	    .sliding(2)
	    .map(pair => pair(0) == pair(1))
	    .foldLeft(List(1)) ( 
	        (list, flag) => (if (flag) list.head + 1 else 1) :: list
	    )
	)

	private def getGridHeights(grid: Array[Array[Int]]) = (
	    grid
	    .transpose
	    .map(column => future(computeHeights(column)))
	    .map(_())
	    .toList
	    .transpose
	)

	
	private def computeWidths(height: Int, row: Array[Int], heightRow: List[Int]) = (
	    row
	    .sliding(2)
	    .zip(heightRow.iterator)
	    .toSeq
	    .reverse
	    .foldLeft(List(1)) { case (widths @ (currWidth :: _), (Array(prev, curr), currHeight)) =>
	        (
	            if (currHeight >= height && currWidth > 0 && prev == curr) currWidth + 1
	            else 1
	        ) :: widths
	    }
	    .toArray
	)
	
	private def getGridWidths(height: Int, grid: Array[Array[Int]]) = (
	    grid
	    .zip(getGridHeights(grid))
	    .map { case (row, heightsRow) => future(computeWidths(height, row, heightsRow)) }
	    .map(_())
	)
	
	def findRectangles(height: Int, width: Int, grid: Array[Array[Int]]) = {
	    val gridWidths = getGridWidths(height, grid)
	    for {
	        y <- gridWidths.indices
	        x <- gridWidths(y).indices
	        if gridWidths(y)(x) >= width
	    } yield Position2D(x, y)
	}

}