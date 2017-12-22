
import java.io.{File, PrintWriter}

import scala.collection.mutable
import scala.io.Source

object Utilities {

    /**
      * Reads the input data and creates a similarity map.
      * @param filename input filename
      * @return
      */
    def readData(filename: String): (mutable.Map[Int, mutable.PriorityQueue[Neighbor]],mutable.Map[Int, Array[Double]], mutable.Map[Int, Array[Int]]) ={
        val similarities:mutable.Map[Int, mutable.PriorityQueue[Neighbor]] = mutable.Map()
        val dataPoints: mutable.Map[Int, Array[Double]] = mutable.Map()
        val clusterAssignments: mutable.Map[Int, Array[Int]] = mutable.Map()
        var i = 0
        val lines = Source.fromFile(filename).getLines()

        for(line <- lines){
            val tokens = line.split(",")
            val data = tokens.slice(0,tokens.length-1).map(x => x.toDouble)
            clusterAssignments.put(i, Array(i))
            similarities.put(i, mutable.PriorityQueue.empty(Ordering.by((_: Neighbor).distance).reverse))
            dataPoints.put(i, data)
            for(point <- dataPoints.keys){
                if(i != point){
                    val distance = HierarchicalClustering.euclideanDistance(data, dataPoints(point))
                    similarities(point).enqueue(new Neighbor(i, distance))
                    similarities(i).enqueue(new Neighbor(point, distance))
                }
            }

            i+=1
        }
        (similarities, dataPoints, clusterAssignments)
    }

    /**
      * Write the results of clustering into a given file.
      * @param filename output filename
      * @param clusterAssignments merged clusters. keys are representative labels, they don't have any special label
      * @param dataPoints original data points.
      */

    def writeResults(filename: String,clusterAssignments: mutable.Map[Int, Array[Int]], dataPoints: mutable.Map[Int, Array[Double]]):Unit ={

        val pw: PrintWriter = new PrintWriter(new File(filename))
        for(cluster <- clusterAssignments.keys){
            val clusterPoints = clusterAssignments(cluster)
            pw.write("cluster root: "+cluster+"\n")
            clusterPoints.foreach{
                x =>
                    pw.write("[" + dataPoints(x).map(x=> "%1.1f".format(x)).mkString(", ")+"']\n")
            }
            pw.write("Number of points in this cluster:"+clusterPoints.length+"\n\n")

        }
        pw.close()

    }

}
