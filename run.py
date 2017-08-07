import os

from multiprocessing import Pool

run_cmd = 'java -classpath $PWD/bin:$PWD/lib/bcprov-jdk15on-157.jar com.sleepysim.Main '

def run_test(x):
	res = open('result' + str(x) + '.txt', 'w')
	for i in xrange(4):
		for j in xrange(20):
			os.system(run_cmd + str(x) + ' ' + str(j + 40))
			f = open('naive_result' + str(x) + '.txt')
			for lines in f:
				res.write(lines)
			f.close()
	res.close()

os.system('./Make.sh')
pool = Pool(3)
result = pool.map(run_test, [0, 1, 2])
pool.close()
pool.join()
