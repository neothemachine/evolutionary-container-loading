package ea.containerloading.test

import ea.containerloading._
//import ea.containerloading.Kevin._
import ea.containerloading.Daniel

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
    	
    	//val positions = Helpers.findFlatSurfaces(layer, Dimension2D(2,3), 5)
    	val positions = Daniel.findRectangles(3, 2, layer)
    			
    	assertSame(3, positions.length)
//    	assert(Set(Position2D(2,1), Position2D(3,1), Position2D(5,0)).subsetOf(positions.toSet))
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
//    	val positions = Helpers.findFlatSurfacesKevin(layer, Dimension2D(2,3), 5)
//    	val coordSet = (positions map (_._1)).toSet
//    	
//    	assertSame(5, positions.length)
//    	assert(Set(Position2D(3,0), Position2D(4,0), Position2D(5,0), Position2D(4,1), Position2D(5,1)).subsetOf(coordSet))
//    	
//    }
}


