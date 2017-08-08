import tensorflow as tf
import numpy as np
import numpy.polynomial.polynomial as poly
import time

from random import randint

tf.reset_default_graph()
num_neurons = 5000

indices = []
values = []
for i in range(num_neurons):
    for j in range(num_neurons):
        x  = 3
        if i != j:
            number = randint(0, 99)
            if number < 5:
                indices.append([i, j])
                values.append(1.0/5)

connections = tf.SparseTensor(indices=indices, values=values, dense_shape=[num_neurons, num_neurons])

neuron_values = tf.Variable(np.ones(num_neurons), dtype=tf.float32)

mul_product = tf.sparse_tensor_dense_matmul(connections, tf.reshape(neuron_values, shape=(num_neurons, 1)))

sess = tf.Session()
sess.run(tf.global_variables_initializer())

temp = time.time()
output = sess.run(mul_product)
print(time.time()-temp)