import scala.collection.mutable


object HierarchicalClustering {

    /**
      * Merge two clusters and create a new centroid for the cluster and return it
      * @param indices the indices of points in the new cluster
      * @param dataPoints map of all data points.
      * @return centroid of the merged cluster
      */

    def merge(indices: Array[Int], dataPoints: mutable.Map[Int, Array[Double]]): Array[Double] ={
        dataPoints.filter(x => indices.contains(x._1)).values.reduce (
            (x:Array[Double], y: Array[Double]) =>
                Array.range(0,x.length).map(a => x{a} + y{a})
        ).map( x => x/indices.length)
    }


    /**
      * Calculates euclidean distance between two points a and b.
      * @param a vector a
      * @param b vector b
      * @return Distance between a and b
      */
    def euclideanDistance(a:Array[Double], b: Array[Double]): Double ={
        val sum = Array.range(0,a.length).map(x => (a{x}-b{x})*(a{x}-b{x})).sum
        math.sqrt(sum)
    }

    /**
      * Runs hierarchical clustering until it creates k clusters
      * @param similarities N priority queues with each of storing similarity value with every other point
      * @param dataPoints Actual data points
      * @param clusterAssignments list of points in a given cluster. It is an inverted map of clusterLabels
      * @param k number of clusters to be generated
      * @return
      */

    def runClustering(similarities: mutable.Map[Int, mutable.PriorityQueue[Neighbor]],
                      dataPoints: mutable.Map[Int, Array[Double]],
                      clusterAssignments: mutable.Map[Int, Array[Int]],
                      k: Int): (mutable.Map[Int, Array[Int]], mutable.Map[Int, Array[Double]]) ={

        val mergedPoints: collection.mutable.Set[Int] = collection.mutable.Set()
        val numPoints = similarities.size
        for (_ <- 0 until (numPoints - k)){
            var minValue: Double = Double.MaxValue
            var minIndex1: Int = -1
            var minIndex2: Int = -1
            for (point <- similarities.keys) {
                val element = similarities(point).dequeue()
                if (element.distance < minValue) {
                    minIndex1 = point
                    minIndex2 = element.index
                    minValue = element.distance
                }
                similarities(point).enqueue(element)
            }


            mergedPoints.add(minIndex2)
            clusterAssignments(minIndex1) ++= clusterAssignments(minIndex2)
            val merged = merge(clusterAssignments(minIndex1), dataPoints)
            similarities.remove(minIndex2)
            clusterAssignments.remove(minIndex2)
            dataPoints.put(minIndex1, merged)
            similarities(minIndex1).clear()
            similarities(minIndex1).enqueue(new Neighbor(minIndex2,Float.MaxValue))

            for(point <- dataPoints.keys) {
                if(point != minIndex1 && !mergedPoints.contains(point)){
                    val distance = euclideanDistance(merged, dataPoints(point))
                    similarities(minIndex1).enqueue(new Neighbor(point,distance))
                    similarities(point).find (_.index == minIndex1) match {
                        case Some(x : Neighbor) => x.distance = distance
                        case None => print("")
                    }
                    similarities(point).find (_.index == minIndex2) match {
                        case Some(x : Neighbor) => x.distance = Float.MaxValue
                        case None => print("")
                    }
                }
            }
        }

        (clusterAssignments, dataPoints)
    }
}

