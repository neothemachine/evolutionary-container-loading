package ea.containerloading

case class Dimension2D(width: Int, height: Int) {
	val area = (width: Long) * (height: Long)
}
case class Dimension3D(width: Int, height: Int, depth: Int) {
	val volume = (width: Long) * (height: Long) * (depth: Long)
	override def toString = width.toString + "x" + height.toString + "x" + depth.toString
}
case class Position2D(x: Int, y: Int)
case class Position3D(x: Int, y: Int, z: Int)

case class Rotation3D(x: Boolean, y: Boolean, z: Boolean) {
	
	def rotateCuboid(size: Dimension3D): Dimension3D = {
		var w = size.width 
		var h = size.height
		var d = size.depth
		
		if (x) {
			val foo = h
			h = d
			d = foo
		}
		if (y) {
			val foo = w
			w = d
			d = foo
		}
		if (z) {
			val foo = w
			w = h
			h = foo
		}
		Dimension3D(w,h,d)
	}
}