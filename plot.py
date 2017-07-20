import matplotlib.pyplot as plt
import math
from mpl_toolkits.mplot3d import Axes3D

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
file = open("naive_result.txt")
while True:
    line1 = file.readline()
    if not line1:
        break
    line2 = file.readline()
    line2 = line2[: -1]
    a = line1.split(' ')
    x = (float)(a[1])
    y = (float)(a[2])
    z = (float)(a[4])
    z = math.log10(z)
    result = False
    if line2 == "Yes":
        result = True
    if result:
        ax.scatter(x, y, z, marker = 'o', c = 'r')
    else:
        ax.scatter(x, y, z, marker = 'o', c = 'b')

ax.set_xlabel('Adversary Count')
ax.set_ylabel('Max Network Delay')
ax.set_zlabel('Difficulty')

ax.set_zlim([-3, 0])

plt.show()
