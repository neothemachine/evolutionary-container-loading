package ea.containerloading

class ContainerProblem(containerSize: Dimension, boxes: List[(Int /*count*/, Dimension)]) {
	
	def getContainerSize = containerSize
	
	def getBoxes = boxes
	
	def getBoxIndices = 0 to boxes.map(_._1).sum
	
	def getBoxDimension(index: Int): Dimension = {
		return this.boxes(calculateOriginalBoxIndices()(index))._2
	}
	
	/**
	 * Boxes of same type (dimension) get same indices
	 * First index is 0
	 * 
	 * @return e.g. List(0,0,1,1,1,2,2,3,4,5,6,6)
	 */
	private def calculateOriginalBoxIndices(): List[Int] = {
		var indices: List[Int] = Nil
		val boxCounts = this.boxes.map(_._1)
		
		boxCounts foreach { boxCount => 
			val currentBoxIndex = indices match {
				case Nil        => 0
				case head::tail => head + 1
			}
			val newBoxes = List.fill(boxCount)(currentBoxIndex)
			indices = newBoxes ::: indices
		}
		
		return indices.reverse
	}
	

}