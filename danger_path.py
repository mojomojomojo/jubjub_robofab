#!/usr/bin/env python3

from Point import Point
from Circle import Circle

import numpy as np
import matplotlib.pyplot as plt
import itertools
import math

def coords(p):
    return p.x,p.y

if __name__ == '__main__':
    tank1 = Point(0,0)
    tank2 = Point(500,0) # shooter

    # Danger: [Circle C(424.255,742.446), R(628.000)]

    b_power = 3
    b_speed = 20-3*b_power

    t_speed = 8

    distance = tank1.distance(tank2)
    t_min = int(math.ceil(distance / (t_speed + b_speed)))
    t_max = int(math.ceil(distance / abs(b_speed - t_speed)))

    d_min = tank2.distanceTowardPoint(tank1,t_min*t_speed)
    d_max = tank2.distanceTowardPoint(tank1,-t_max*t_speed)
    danger = Circle(d_min.midpoint(d_max),t_speed*(t_min+t_max)/2)
    print("Danger: {0}".format(danger));
    
    bad_loci = []
    loci_param = []
    for t in range(t_min,t_max+1):
        try:
            for isect in Circle(tank1,b_speed*t).intersect(Circle(tank2,t_speed*t)):
                bad_loci.append(coords(isect))
                loci_param.append([t,#/t_max,
                                   (math.atan2(isect.y-danger.c.y,isect.x-danger.c.x)) - 
                                   0#(loci_param[-1][1] if len(loci_param)>0 else 0)
                ])
        except:
            pass
    bad_loci = np.array(bad_loci)
    loci_param = np.array(loci_param)

    #print(bad_loci)
    print(loci_param)
    d_circle = plt.Circle(danger.c.coords(),danger.r,color='r',alpha=.2)
    plt.figure(1)
    plt.gcf().gca().add_artist(d_circle)
    plt.scatter(bad_loci[:,0],bad_loci[:,1])
    plt.scatter([tank1.x],[tank1.y], s=10, c='red')
    plt.scatter([tank2.x],[tank2.y], s=10, c='green')
    plt.axes().set_aspect('equal')

    plt.figure(2)
    plt.scatter(loci_param[:,1],loci_param[:,0])
    #plt.axes().set_aspect('equal')


    plt.show()

