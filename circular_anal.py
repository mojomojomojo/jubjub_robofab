#!/usr/bin/env python3

from Point import Point
from Circle import Circle
import math
import numpy as np

# This data was made by Excel.
def experimental( p ):
    return ( 2E-5 * p**6
             -  0.002 * p**5
             +  0.105 * p**4
             -  2.8517 * p**3
             + 43.524 * p**2
            - 387.6   * p
           + 2164.8 )


if __name__ == '__main__':
    # Attempt to simulate (with greater precision) the PWM-style
    #   circular movement.


    p = 1
    turnA = 4
    turnB = (2*p-1)*4
    
    start = Point(2500,4500);
    h = 90;
    
    path = []

    # turn, move
    currp = start
    turn = 0
    while True:
        path.append(currp);
        print("{0:d},{1:.03f},{2:.03f},{3:.03f}".format(
            turn, h, currp.x, currp.y ))
    
        if turn % 2 == 0:
            h += turnA
        else:
            h += turnB
        h %= 360
        turn += 1
        currp = Point( currp.x + 8*math.cos(math.radians(90-h)),
                       currp.y + 8*math.sin(math.radians(90-h)) )

        if currp == start:
            break
        
    print("{0:d},{1:.03f},{2:.03f},{3:.03f}".format(
        turn, h, currp.x, currp.y ))

    x = np.array([ p.x for p in path ])
    y = np.array([ p.y for p in path ])

    center = Point(np.mean(x),np.mean(y))
    radius = np.mean([ center.distance(p) for p in path ])

    print("C{0}, r({1:.02f})".format(center,radius))
