## Efficient Hierarchical Clustering

This is an efficient implementation of hierarchical agglomerative clustering algorithm. This is more intuitive than other versions of this algorithm. This algorithm has been extracted and modified from the book chapter from Introduction to Information Retrieval by Christopher D. Manning, Prabhakar Raghavan and Hinrich Schütze ([reference here](https://nlp.stanford.edu/IR-book/)). The original description of the algorithm is given below.

```
for n = 1 to N
    for i = 1 to N
        do C[n][i] = SIM(d[n],d[i])
    I[n] = true //setting active clusters

for k = 1 to N-1
    <i,m> = argmax (I[i] && I[m] && i!= m ) C[i][m]
    mergedCluster = merge((i,m))

    for j = 1 to N
        C[i][j] = C[j][i] = SIM(mergedCluster, d[j])

    I[m] = false //deactivating
```

The distance measure chosen for this implementation is centroid based, i.e., we choose the average of all points as the representative point for a cluster.

As you can see, the algorithm takes more time and repeats the identification of `argmax` a lot of times. An efficient implementation is the use of priority queue that always keeps the max value at the top of the heap and we pick the top of the heap and rearrange the heap using an efficient heapify algorithm.

```
Q = [] //priority queue
for n = 1 to N
    for i = 1 to N
        Q.enqueue(SIM(d[i], d[n]), (i, n))

for k = 1 to N-1
    <i,m> = Q.dequeue()
    mergedCluster = merge((i,m))

    Q.remove((_,m)) //remove any similarity that includes m

    for j = 1 to N
        Q.update((i,j), SIM(mergedCluster, d[j]))
```

There is a repeated removal from `Q` when `i,m` are merged. This can be avoided with a new implementation where we maintain multiple priority queues and find the maximum of each priority queue and pick the one with max value. We can ignore the priority queue for `m`. When considering amortized analysis of this algorithm, this efficiently performs the deletion and restricts the number of calls to Heapify algorithm.
