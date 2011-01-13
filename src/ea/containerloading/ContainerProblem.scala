package ea.containerloading

case class Box(id: Int, size: Dimension3D)
case class Container(size: Dimension3D)

class ContainerProblem(val container: Container, val boxSizeFrequencies: Map[Dimension3D, Int]) {
	
	def this(containerSize: Dimension3D, boxSizeFrequencies: Map[Dimension3D, Int]) =
		this(Container(containerSize), boxSizeFrequencies)

	private val boxIds = 0 to (boxSizeFrequencies.values.sum - 1)
	
	/**
	 * weist jeder boxId eine Box zu
	 */
	private val boxIndexMapping: Map[Int, Box] = {
		
		val boxReferences: List[Int] = calculateOriginalBoxIndices
		val boxes = boxSizeFrequencies.keys.toList
		// TODO geht doch sicher auch eleganter...
		val mapping = collection.mutable.Map[Int, Box]()
		for (id <- boxIds) { 
			mapping += (id -> Box(id, boxes(boxReferences(id))))
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
		var indices: List[Int] = Nil
		
		for (boxCount <- boxSizeFrequencies) {
			val currentBoxIndex = indices match {
				case Nil        => 0
				case head::tail => head + 1
			}
			val newBoxes = List.fill(boxCount._2)(currentBoxIndex)
			indices = newBoxes ::: indices
		}
		
		return indices.reverse
	}
}