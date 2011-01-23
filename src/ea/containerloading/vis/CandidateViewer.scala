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
		universe.getViewer.getView.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY)
		universe.getViewer.getView.setDepthBufferFreezeTransparent(false)
		
		val scene = new BranchGroup
		setupMouseNavigation(universe, scene)
		
		// lighten up
		val lightColor = new Color3f(0.3f, 0.3f, 0.3f)
		val bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0)

		val light1 = new AmbientLight
		light1.setInfluencingBounds(bounds)
		scene.addChild(light1)
		
		val light2Direction = new Vector3f(4.0f, -7.0f, -12.0f)
		val light2 = new DirectionalLight(lightColor, light2Direction)
		light2.setInfluencingBounds(bounds)
		scene.addChild(light2)

		// draw container
		val norm: Float = loaded.container.size.width max 
		                  loaded.container.size.depth max
		                  loaded.container.size.height
		
		val appearance = new Appearance
		appearance.setColoringAttributes(new ColoringAttributes(1.0f,1.0f,1.0f,ColoringAttributes.NICEST))
		
		val polyAttrbutes = new PolygonAttributes
		polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE)
		polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE)
		appearance.setPolygonAttributes(polyAttrbutes)
		
		val container = new gBox(
				// jeweils /2, da Box die Hälfte der Breite/Höhe/Tiefe erwartet
				// (wie bei Kreis -> statt Durchmesser, Radius)
				// außerdem minimale Vergrößerung um 0.0001, damit die äußeren Boxen
				// nicht das weiße Drahtgitter vom Container übermalen
				(loaded.container.size.width / norm) / 2  + 0.0001f,
				(loaded.container.size.height / norm) / 2 + 0.0001f, 
				(loaded.container.size.depth / norm) / 2 + 0.0001f, appearance)

		scene.addChild(container)
						
		// add everything to universe
		universe.addBranchGraph(scene)
		
		// reset view
		universe.getViewingPlatform.setNominalViewingTransform
			
		val rng = new scala.util.Random
		// draw boxes
		for (box <- loaded.loadedBoxes.reverse) {
			val boxAppearance = new Appearance
			val color = new Color3f(rng.nextFloat,rng.nextFloat,rng.nextFloat)
			val material = new Material
			material.setAmbientColor(color)
			boxAppearance.setMaterial(material)
			boxAppearance.setColoringAttributes(new ColoringAttributes(color,ColoringAttributes.NICEST))
			boxAppearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.3f))
			
			val boxPAppearance = new Appearance
			boxPAppearance.setColoringAttributes(boxAppearance.getColoringAttributes)
			boxPAppearance.setPolygonAttributes(polyAttrbutes)
			
			val boxSize = box.rotation.rotateDimensions(box.box.size)
			val gBox = new gBox((boxSize.width / norm)/2, (boxSize.height / norm)/2, (boxSize.depth / norm)/2, boxAppearance)
			val gBoxPolygon = new gBox((boxSize.width / norm)/2, (boxSize.height / norm)/2, (boxSize.depth / norm)/2, boxPAppearance)
			
			val transform = new Transform3D
			transform.setTranslation(
					new Vector3f(
							gBox.getXdimension        // move box edge to origin
							- container.getXdimension // move box edge to container edge
							+ box.position.x / norm,  // move box to final position 
							gBox.getYdimension - container.getYdimension + box.position.y / norm,
							gBox.getZdimension - container.getZdimension + box.position.z / norm))
			val tg = new TransformGroup(transform)
			tg.addChild(gBox)
			tg.addChild(gBoxPolygon)
						
			val boxScene = new BranchGroup
			boxScene.addChild(tg)
			universe.addBranchGraph(boxScene)
			Thread.sleep(500)
		}	
		println("all boxes were drawn")
	}
	
	private def setupMouseNavigation(universe: SimpleUniverse, scene: BranchGroup) = {
		
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
	}
}