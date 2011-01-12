package ea.containerloading.test

import ea.containerloading._

import org.junit._
import Assert._


class HelpersTest {

    @Test
    def findFlatSurfaces() = {
    	
    	val layer = Array(
    			Array(0,0,0,0,0,0,0),
    			Array(4,0,2,0,0,0,0),
    			Array(0,0,0,0,0,0,0),
    			Array(0,1,1,5,0,0,0))
    			    	
    	val positions = Helpers.findFlatSurfaces(layer, Dimension2D(3,3), 0)
    			
    	assertSame(3, positions.length)
    	assert(Set(Position2D(3,0), Position2D(4,0), Position2D(4,1)).subsetOf(positions.toSet))
    }
    
    @Test
    def findFlatSurfaces2() = {
    			
    	val layer = Array(
    			Array(0,0,0,0,0,0,0),
    			Array(4,4,2,2,2,0,0),
    			Array(4,4,2,2,2,0,0),
    			Array(0,0,2,2,2,1,1),
    			Array(0,0,0,0,0,1,1))
    	
    	val positions = Helpers.findFlatSurfaces(layer, Dimension2D(2,3), 5)
    			
    	assertSame(3, positions.length)
    	assert(Set(Position2D(2,1), Position2D(3,1), Position2D(5,0)).subsetOf(positions.toSet))
    }
    
//    @Test
//    def findFlatSurfacesKevin() = {
//    	
//    	val layer = List(
//    			List(0,0,0,0,0,0,0),
//    			List(4,0,2,0,0,0,0),
//    			List(0,0,0,0,0,0,0),
//    			List(0,1,1,5,0,0,0))
//    	
//    	val positions = findRectangles(layer)
//    	println
////    	assertSame(3, positions.length)
////    	assert(Set(Position2D(3,0), Position2D(4,0), Position2D(4,1)).subsetOf(positions.toSet))
//    	
//    }
//
//	type Grid[T] = List[List[T]]
//	
//	def runLengths[T](row:List[T]) : List[(T,Int)] = row match {
//	  case Nil => Nil
//	  case h :: t => (h -> row.takeWhile(_ == h).size) :: runLengths(t)
//	}
//	
//	/**
//	 * Kevin's algo, nicht geeignet für überlappende surfaces!
//	 * @param grid
//	 */
//	def findRectangles[T](grid: Grid[T]) = {
//	  val step1 = (grid map runLengths)
//	  val step2 = (step1.transpose map runLengths).transpose
//	  
//	  val step1b = (grid.transpose map runLengths).transpose
//	  val step2b = (step1 map runLengths)
//	  
//	  val a = step2 map { _ map { case ((a,b),c) => (a,(b,c)) } }
//	  val b = step2b map { _ map { case ((a,b),c) => (a,(b,c)) } }
//	   val c = a zip b
//	  //val r = List.flatten(c)
//	}



    
}


