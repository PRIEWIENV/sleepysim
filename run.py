import os

from multiprocessing import Pool

run_cmd = 'java -classpath $PWD/bin:$PWD/lib/bcprov-jdk15on-157.jar com.sleepysim.Main '

def run_test(x):
	res = open('result' + str(x) + '.txt', 'w')
	for j in range(40):
		os.system(run_cmd + str(x) + ' ' + str(j))
		f = open('naive_result' + str(x) + '.txt')
		for lines in f:
			res.write(lines)
		res.flush()
		f.close()
	res.close()

os.system('./Make.sh')
pool = Pool(36)
result = pool.map(run_test, range(100))
pool.close()
pool.join()
