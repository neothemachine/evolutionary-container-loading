package ea.containerloading.vis

import ea.containerloading._
import com.sun.j3d.utils.universe._
import com.sun.j3d.utils.geometry.{ Box => gBox}
import com.sun.j3d.utils.behaviors.mouse._
import javax.media.j3d._
import javax.vecmath._

object CandidateViewer {

	def showCandidate(loaded: LoadedContainer) = {
		
		val universe = new SimpleUniverse
		val scene = new BranchGroup
				
		setupUniverse(universe, scene)
		
		// draw container
		val norm = (loaded.container.size.width max 
		            loaded.container.size.depth max
		            loaded.container.size.height) * 1.2f
		
		val appearance = new Appearance
		appearance.setColoringAttributes(new ColoringAttributes(1.0f,1.0f,1.0f,ColoringAttributes.NICEST))
		
		val polyAttrbutes = new PolygonAttributes
		polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE)
		polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE)
		appearance.setPolygonAttributes(polyAttrbutes)
		
		val container = new gBox(
				// jeweils /2, da Box die Hälfte der Breite/Höhe/Tiefe erwartet
				// (wie bei Kreis -> statt Durchmesser, Radius)
				// außerdem minimale Vergrößerung um 0.001, damit die äußeren Boxen
				// nicht das weiße Drahtgitter vom Container übermalen
				(loaded.container.size.width / norm) / 2  + 0.001f,
				(loaded.container.size.height / norm) / 2 + 0.001f, 
				(loaded.container.size.depth / norm) / 2 + 0.001f, appearance)
		
		val transform = new Transform3D
		transform.setTranslation(
				new Vector3f(container.getXdimension, container.getYdimension, container.getZdimension))
		val tg = new TransformGroup(transform)
		tg.addChild(container)
		
		scene.addChild(tg)
		
		// add everything to universe
		universe.addBranchGraph(scene)
		
		// reset view
		universe.getViewingPlatform.setNominalViewingTransform
			
		val rng = new scala.util.Random
		// draw boxes
		for (box <- loaded.loadedBoxes.reverse) {
			val boxAppearance = new Appearance
			val color = new Color3f(rng.nextFloat,rng.nextFloat,rng.nextFloat) 
			boxAppearance.setColoringAttributes(new ColoringAttributes(color,ColoringAttributes.NICEST))
			boxAppearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.05f))
			
			val boxPAppearance = new Appearance
			boxPAppearance.setColoringAttributes(boxAppearance.getColoringAttributes)
			boxPAppearance.setPolygonAttributes(polyAttrbutes)
			
			val boxSize = box.box.size
			val gBox = new gBox((boxSize.width / norm)/2, (boxSize.height / norm)/2, (boxSize.depth / norm)/2, boxAppearance)
			val gBoxPolygon = new gBox((boxSize.width / norm)/2, (boxSize.height / norm)/2, (boxSize.depth / norm)/2, boxPAppearance)
			
			val transform = new Transform3D
			transform.setTranslation(
					new Vector3f(
							gBox.getXdimension + box.position.x / norm,
							gBox.getYdimension + box.position.y / norm,
							gBox.getZdimension + box.position.z / norm))
			val tg = new TransformGroup(transform)
			tg.addChild(gBox)
			tg.addChild(gBoxPolygon)
						
			val boxScene = new BranchGroup
			boxScene.addChild(tg)
			universe.addBranchGraph(boxScene)
			Thread.sleep(1000)
		}
		
		
//		// add everything to universe
//		scene.compile
//		universe.addBranchGraph(scene)
//		
//		// reset view
//		universe.getViewingPlatform.setNominalViewingTransform
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