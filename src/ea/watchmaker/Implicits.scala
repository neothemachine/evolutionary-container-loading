package ea.watchmaker

import org.uncommons.watchmaker.framework._

object Implicits {

	implicit def closure2Observer[T](listener: (PopulationData[_ <: T]) => Unit): EvolutionObserver[T] =
		new EvolutionObserver[T] {
			def populationUpdate(data: PopulationData[_ <: T]) = {
				listener(data)
			}
		}
	
}