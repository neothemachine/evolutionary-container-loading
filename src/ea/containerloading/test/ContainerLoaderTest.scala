package ea.containerloading.test

import ea.containerloading._
import ea.containerloading.vis._

import javax.vecmath._
import javax.media.j3d._
import scala.collection.JavaConversions._

import org.junit._
import Assert._

class ContainerLoaderTest {

	@Test
    def loadWithLayerApproach() = {
		
		val container = Container(Dimension3D(10,10,10))
		val r = BoxRotation(false, false, false)
		val boxLoadingOrder = List(
			(Box(1, Dimension3D(5,5,5)), r),
			(Box(2, Dimension3D(5,5,5)), r),
			(Box(3, Dimension3D(5,5,5)), r),
			(Box(4, Dimension3D(5,5,5)), r),
			(Box(5, Dimension3D(5,5,5)), r),
			(Box(6, Dimension3D(5,5,5)), r),
			(Box(7, Dimension3D(5,5,5)), r),
			(Box(8, Dimension3D(5,5,5)), r)
			)
		
		val loadingResult = ContainerLoader.loadLayer(container, boxLoadingOrder)
		println(loadingResult.loadedBoxes.map(b => b.position))
		assertSame(0, loadingResult.skippedBoxes.length)
				
		val boxes: Seq[Bounds] = loadingResult.loadedBoxes.map(box => 
			new BoundingBox(
					new Point3d(box.position.x, box.position.y, box.position.z),
					new Point3d(
							box.position.x + box.box.size.width,
							box.position.y + box.box.size.height,
							box.position.z + box.box.size.depth)
			))
		
		for (box <- boxes) {
			val otherBoxes = (boxes filterNot (_ == box)).toArray
			assertFalse(box.intersect(otherBoxes))
		}
				
		CandidateViewer.showCandidate(loadingResult)
	}
	
}