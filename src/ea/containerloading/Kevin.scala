package ea.containerloading

import annotation.tailrec
import ea.containerloading._

/**
 * http://stackoverflow.com/questions/4656706/how-to-find-same-value-rectangular-areas-of-a-given-size-in-a-matrix-most-efficie/4657041#4657041
 *
 */
object Kevin {
	
	class RowOps[T](row: List[T]) {
	  def withRunLengths[U](func: (T,Int)=>U): List[U] = {
	    @tailrec def recurse(row:List[T], acc:List[U]): List[U] = row match {
	      case Nil => acc
	      case head :: tail =>
	        recurse(
	          tail,
	          func(head, row.takeWhile(head==).size) :: acc)
	    }
	    recurse(row, Nil).reverse
	  }
	  def mapRange(start: Int, len: Int)(func: T=>T): List[T] =
	    row.splitAt(start) match {
	      case (l,r) => l ::: r.take(len).map(func) ::: r.drop(len)
	    }
	}
	
	implicit def rowToOps[T](row: List[T]) = new RowOps(row)
	type Grid[T] = List[List[T]]
	
	class GridOps[T](grid: Grid[T]) {
	  def deepZip[U](other: Grid[U]) = (grid zip other) map { case (g,o) => g zip o}
	  def deepMap[U](f: (T)=>U) = grid map { _ map f}
	  def mapCols[U](f: List[T]=>List[U]) = (grid.transpose map f).transpose
	  def height = grid.size
	  def width = grid.head.size
	  def coords = List.tabulate(height,width){ case (y,x) => (x,y) }
	  def zipWithCoords = deepZip(coords)
	  def deepMapRange(x: Int, y: Int, w: Int, h: Int)(func: T=>T) =
        grid.mapRange(y,h){ _.mapRange(x,w)(func) }
	}
	
	implicit def gridToOps[T](grid: Grid[T]) = new GridOps(grid)

	def withRowRunLengths[T,U](grid: Grid[T])(func: (T,Int)=>U) =
	  grid map { _.withRunLengths(func) }
	
	def withColRunLengths[T,U](grid: Grid[T])(func: (T,Int)=>U) =
	  grid mapCols { _.withRunLengths(func) }

	case class Rect(w: Int, h: Int)
	object Rect { def empty = Rect(0,0) }

	case class Cell[T](
	  value: T,
	  coords: (Int,Int) = (0,0),
	  widest: Rect = Rect.empty,
	  tallest: Rect = Rect.empty
	)

	def findAllRectangles(input: Grid[Int]): List[(Rect, Int, (Int, Int))] = {
		val stage1w = withRowRunLengths(input) {
		  case (cell,width) => (cell,width)
		}
		
		val stage2w = withColRunLengths(stage1w) {
		  case ((cell,width),height) => Rect(width,height)
		}
		
		val stage1t = withColRunLengths(input) {
		 case (cell,height) => (cell,height)
		}
		
		val stage2t = withRowRunLengths(stage1t) {
		  case ((cell,height),width) => Rect(width,height)
		}
		
		val cellsWithCoords = input.zipWithCoords deepMap {
		  case (v,(x,y)) => Cell(value=v, coords=(x,y))
		}
		
		val cellsWithWidest = cellsWithCoords deepZip stage2w deepMap {
		  case (cell,rect) => cell.copy(widest=rect)
		}
		
		val cellsWithWidestAndTallest = cellsWithWidest deepZip stage2t deepMap {
		  case (cell,rect) => cell.copy(tallest=rect)
		}
		
		val results = (cellsWithWidestAndTallest deepMap {
		  case Cell(value, coords, widest, tallest) =>
		    List((widest, value, coords), (tallest, value, coords))
		  }
		).flatten.flatten
		return results
	}
	

}




