#!/usr/bin/env python3

from Point import Point
from Circle import Circle

import numpy as np
import matplotlib.pyplot as plt
import itertools
import math
import sys

if __name__ == '__main__':

    data = np.genfromtxt(sys.argv[1], delimiter=',')

    h = data[:,-3] # antepenultimate column
    x = data[:,-2] # penultimate column
    y = data[:,-1] # ultimate column

    # throw away acceleration points
    # (They're no longer saved.)
    #x = _x[8:]
    #y = _y[8:]

    # find the period
    h0 = h[0]
    period = -1
    for i in range(1,len(x)):
        if (abs(h[i]-h0) < 0.1):
            period = i
    assert period!=-1, "No periodicity found!\n"
    periods = math.floor(len(x) / period)
    print("Period: {0}/{1}".format(period,len(x)))

    i_q = int(period / 3)
    c_points = [ Point(x[i],y[i]) for i in (0,i_q,i_q*2) ]
    print(' '.join(map(str,c_points)))
    circle = Circle.from3(*c_points)
    print("(Est) 3P circle: ({0:.02f},{1:.02f}) {2:.02f}\n".format(
        circle.c.x,circle.c.y,circle.r));

    # calculate r and c
    diameter = np.max(x)-np.min(x)

    center_x = np.mean(x)
    center_y = np.mean(y)
    print("(Est) circle: ({0:.02f},{1:.02f}) {2:.02f}\n".format(
        center_x,center_y,diameter/2));
    #center_y += 26

#    d_circle = plt.Circle([287.24,405.11],114.7,color='r',alpha=.2)
#    d_circle = plt.Circle([circle.c.x,circle.c.y],circle.r,color='r',alpha=.2)
    d_circle = plt.Circle([center_x,center_y],diameter/2,color='r',alpha=.2)
    p1_point = plt.Circle([c_points[0].x,c_points[0].y],3,color='k')
    p2_point = plt.Circle([c_points[1].x,c_points[1].y],3,color='k')
    p3_point = plt.Circle([c_points[2].x,c_points[2].y],3,color='k')
    plt.gcf().gca().add_artist(d_circle)
    plt.gcf().gca().add_artist(p1_point)
    plt.gcf().gca().add_artist(p2_point)
    plt.gcf().gca().add_artist(p3_point)

    plt.scatter(x,y)#data[:,6],data[:,7])
    plt.axes().set_aspect('equal')
    plt.show()
