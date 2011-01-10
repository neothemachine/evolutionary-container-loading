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
    }

}


