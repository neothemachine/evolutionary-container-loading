package ea.containerloading.vis

import ea.containerloading._
import com.sun.j3d.utils.universe._
import com.sun.j3d.utils.geometry.{ Box => gBox}
import com.sun.j3d.utils.behaviors.mouse._
import javax.media.j3d._
import javax.vecmath._

object CandidateViewer {

	def showCandidate(problem: ContainerProblem, loaded: LoadedContainer) = {
		
		val universe = new SimpleUniverse
		val scene = new BranchGroup
				
		setupUniverse(universe, scene)
		
		// draw container
		val norm = (problem.getContainer.size.width max 
		            problem.getContainer.size.depth max
		            problem.getContainer.size.height) * 1.5f
		
		val appearance = new Appearance
		appearance.setColoringAttributes(new ColoringAttributes(1.0f,0,0,ColoringAttributes.NICEST))
		appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f))
		
		val container = new gBox(
				problem.getContainer.size.width / norm,
				problem.getContainer.size.depth / norm, 
				problem.getContainer.size.height / norm, appearance)
		
		scene.addChild(container)
		
		val rng = new scala.util.Random
		// draw boxes
		for (box <- loaded.loadedBoxes) {
			val boxAppearance = new Appearance
			val color = new Color3f(rng.nextFloat,rng.nextFloat,rng.nextFloat) 
			boxAppearance.setColoringAttributes(new ColoringAttributes(color,ColoringAttributes.NICEST))

			val boxSize = box.box.size
			val gBox = new gBox(boxSize.width/norm, boxSize.depth/norm, boxSize.height/norm, boxAppearance)
			
			val transform = new Transform3D
			transform.setTranslation(new Vector3f(box.position.x/norm, box.position.y/norm, box.position.z/norm))
			val tg = new TransformGroup(transform)
			tg.addChild(gBox)
			
			scene.addChild(tg)
		}
		
		
		// add everything to universe
		scene.compile
		universe.addBranchGraph(scene)
		
		// reset view
		universe.getViewingPlatform.setNominalViewingTransform
	}
	
	private def setupUniverse(universe: SimpleUniverse, scene: BranchGroup) = {
		
		// set-up mouse navigation
		val vpTrans = universe.getViewingPlatform.getViewPlatformTransform
		val mouseBounds = new BoundingSphere(new Point3d, 1000.0)
		
		val mouseRotate = new MouseRotate(MouseBehavior.INVERT_INPUT)
		mouseRotate.setTransformGroup(vpTrans)
        mouseRotate.setSchedulingBounds(mouseBounds)
        scene.addChild(mouseRotate)
        
        val mouseTranslate = new MouseTranslate(MouseBehavior.INVERT_INPUT)
		mouseTranslate.setTransformGroup(vpTrans)
        mouseTranslate.setSchedulingBounds(mouseBounds)
        scene.addChild(mouseTranslate)
        
        val mouseZoom = new MouseZoom(MouseBehavior.INVERT_INPUT)
        mouseZoom.setTransformGroup(vpTrans)
        mouseZoom.setSchedulingBounds(mouseBounds)
        scene.addChild(mouseZoom)
	
		// lighten up
		val light1Color = new Color3f(1.0f, 0.1f, 0.1f)
		val bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0)

		val light1Direction = new Vector3f(4.0f, -7.0f, -12.0f)
		val light1 = new DirectionalLight(light1Color, light1Direction)
		light1.setInfluencingBounds(bounds)

		scene.addChild(light1)
	}
}