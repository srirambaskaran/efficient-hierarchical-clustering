import java.io.{File, PrintWriter}

import HierarchicalClustering.runClustering

object Task {
    def main(args: Array[String]): Unit = {
        if (args.length != 2) {
            println("You have provided too many or too little arguments. Provide <input_file> <num_clusters>.")
            System.exit(0)
        }

        val tuple = Utilities.readData(args {0})
        val outputFile = "output.txt"
        val k = args {1}.toInt
        if(k <= 0){
            val pw: PrintWriter = new PrintWriter(new File(outputFile))
            pw.write("")
            pw.close()
            System.exit(0)
        }

        val returnedValues = runClustering(tuple._1, tuple._2, tuple._4, k)

        Utilities.writeResults(outputFile, returnedValues._1, returnedValues._2)
    }
}
