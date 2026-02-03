read operations benchmark
=========================


[A] sequential file reading:
read one file one time (read local file from remote)


first time reading:

flow: ls -> read file -> pulled from binary -> not visible to user what happens underneath -> display
e.g., 10MB file -> measure time to read

benchmark against scp
flow: scp to remote host from local -> ls -> read file -> pulled from fs

caching:


goal: comparable to scp (x% better)




[B] parallel file reading: opening N threads and reading N files

how does file reading scale? 8, 16, 32, 64, ......

js2 - js2
js2 - expanse
js2 - local
