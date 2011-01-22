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