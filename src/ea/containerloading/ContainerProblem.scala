package ea.containerloading

case class Box(id: Int, size: Dimension3D, constraints: BoxConstraints = BoxConstraints(true, true))
case class BoxConstraints(widthVertical: Boolean, depthVertical: Boolean) {
	
	private val widthVerticalRotations = 
		if (widthVertical) List(BoxRotation(true, false, false), BoxRotation(true, false, true))
		else List()
		
	private val depthVerticalRotations = 
		if (depthVertical) List(BoxRotation(false, true, false), BoxRotation(false, true, true))
		else List()
	
	val allowedRotations = 
		BoxRotation(false, false, false) :: BoxRotation(false, false, true) ::
		widthVerticalRotations ::: depthVerticalRotations
}
case class BoxRotation(widthVertical: Boolean, depthVertical: Boolean, rotateHorizontal: Boolean) {
	require(!(widthVertical && depthVertical))
	
	def rotateDimensions(size: Dimension3D): Dimension3D = {
		var w = size.width
		var h = size.height
		var d = size.depth
		
		if (widthVertical) {
			val foo = w
			w = h
			h = foo
		} else if (depthVertical) {
			val foo = h
			h = d
			d = foo
		}
		
		if (rotateHorizontal) {
			val foo = w
			w = d
			d = foo
		}
		Dimension3D(w,h,d)
	}
}
case class Container(size: Dimension3D)

class ContainerProblem(val container: Container, val boxSizeFrequencies: Map[Dimension3D, (Int, BoxConstraints)]) {
	
	def this(containerSize: Dimension3D, boxSizeFrequencies: Map[Dimension3D, (Int, BoxConstraints)]) =
		this(Container(containerSize), boxSizeFrequencies)
	
	val boxIds = (0 until boxSizeFrequencies.values.map(f => f._1).sum).toList
	
	/**
	 * weist jeder boxId eine Box zu
	 */
	private val boxIndexMapping: Map[Int, Box] = {
		
		val boxReferences: List[Int] = calculateOriginalBoxIndices
		val boxes = boxSizeFrequencies.keys.toList
		// TODO geht doch sicher auch eleganter...
		val mapping = collection.mutable.Map[Int, Box]()
		for (id <- boxIds) { 
			val size = boxes(boxReferences(id))
			val boxConstraints = boxSizeFrequencies(size)._2
			mapping += (id -> Box(id, size, boxConstraints))
		}
		Map(mapping.toList:_*)		
	}
	
	val boxes = boxIndexMapping.values 
	
	def boxFromId(id: Int): Box = boxIndexMapping(id)
			
	/**
	 * Boxes of same type (dimension) get same indices
	 * First index is 0
	 * 
	 * @return e.g. List(0,0,1,1,1,2,2,3,4,5,6,6)
	 */
	private def calculateOriginalBoxIndices(): List[Int] = {
		var indices = List[Int]()
		
		for (boxCount <- boxSizeFrequencies) {
			val currentBoxIndex = indices match {
				case Nil        => 0
				case head::tail => head + 1
			}
			val newBoxes = List.fill(boxCount._2._1)(currentBoxIndex)
			indices = newBoxes ::: indices
		}
		
		return indices.reverse
	}
}