#!/usr/bin/env python3

import matplotlib.pyplot as plt
import numpy as np
import math

def radius( maxSpeed, p=1.0 ):
    return (360*maxSpeed)/(math.pi*p*(40-3*maxSpeed))

def maxSpeed( radius ):
    return 40*radius/(4+3*radius)


data = {
    p: np.array([ [v_max/10,radius(v_max/10,p/100)]
                  for v_max in range(1,81) ])
    for p in (10,25,50,75,100)
}

plt.plot(data[100][:,0],data[100][:,1],'r',
         data[75][:,0],data[75][:,1],'g',
         data[50][:,0],data[50][:,1],'b',
         data[25][:,0],data[25][:,1],'y',
         data[10][:,0],data[10][:,1],'k',
     )

#plt.axes().set_aspect('equal')
plt.show()

