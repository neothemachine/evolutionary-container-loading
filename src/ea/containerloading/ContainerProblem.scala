package ea.containerloading

case class Dimension(width: Int, height: Int, depth: Int)
case class Box(id: Int, size: Dimension)
case class Container(size: Dimension)

class ContainerProblem(container: Container, boxSizeFrequencies: Map[Dimension, Int]) {
	
	def this(containerSize: Dimension, boxSizeFrequencies: Map[Dimension, Int]) =
		this(Container(containerSize), boxSizeFrequencies)
		
	private val boxIds = 0 to (boxSizeFrequencies.values.sum - 1)
	
	/**
	 * weist jeder boxId eine Box zu
	 */
	private val boxIndexMapping: Map[Int, Box] = {
		
		val boxReferences: List[Int] = calculateOriginalBoxIndices
		val boxes = boxSizeFrequencies.keys.toList
		// TODO geht doch sicher auch eleganter...
		val mapping = scala.collection.mutable.Map[Int, Box]()
		boxIds foreach {id => 
			mapping += (id -> Box(id, boxes(boxReferences(id))))
		}
		Map(mapping.toList:_*)		
	}
	
	private val boxes = boxIndexMapping.values 
	
	def getContainer = container
	def getBoxSizeFrequencies = boxSizeFrequencies
	def getBoxes = boxes
	def getBox(id: Int): Box = boxIndexMapping(id)
			
	/**
	 * Boxes of same type (dimension) get same indices
	 * First index is 0
	 * 
	 * @return e.g. List(0,0,1,1,1,2,2,3,4,5,6,6)
	 */
	private def calculateOriginalBoxIndices(): List[Int] = {
		var indices: List[Int] = Nil
		
		this.boxSizeFrequencies.foreach { boxCount => 
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