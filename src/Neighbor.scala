/**
  * Neighbour class, acts as a helper for defining objects in the priority queue
  * @param index neighbour point index
  * @param distance distance value
  */
class Neighbor(var index: Int, var distance: Double){
    override def hashCode(): Int = index

    override def equals(obj: scala.Any): Boolean = {
        obj match{
            case i: Neighbor => i.index.equals(this.index)
            case _ => false
        }
    }

    override def toString: String = "("+index+","+distance+")"

}