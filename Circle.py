#!/usr/bin/env python3

from Point import Point

import math

class Circle:
    def __init__( self, c, r ):
        self.c = c
        self.r = r

    def __str__( self ):
        return '[Circle C{0}, R({1:.03f})]'.format(self.c,self.r)

    @classmethod
    def from3( cls, a, b, c ):
        mid_ab = a.midpoint(b)
        m_ab = -a.slope(b)
        b_ab = mid_ab.y - m_ab * mid_ab.x
        print("AB: y = {0:.03f} x  {0:+.03f}  %s".format(m_ab,b_ab,mid_ab))

        m_bc = -b.slope(c);
        mid_bc = b.midpoint(c)
        b_bc = mid_bc.y - m_bc * mid_bc.x

        x = (b_bc - b_ab) / (m_ab - m_bc)
        y = m_ab * x + b_ab

        return Circle(Point(x,y),a.distance(Point(x,y)))
        

    def intersect( self, c ):
        #  Based on info obtained from Wolfram
        #    (http://mathworld.wolfram.com/Circle-CircleIntersection.html)
        #  Reorient the two circles so that their centers are both on the
        #    x-axis and one center is at the origin.
        # 
        #    x^2 + y^2 = r1^2
        #    (x-d)^2 + y^2 = r2^2
        # 
        #    x = (d^2-r2^2+r1^2)/(2d)
        #    y^2 = (4 d^2 r1^2 - (d^2 - r2^2 + r1^2)^2) / (4 d^2)
        # 
        #  Once the two intersection points are found, rotate and translate
        #    them back to the original cartesian view.
        # 
        #  The amount to rotate after the calculations.
        undoAngle = math.degrees(math.atan2(c.c.y-self.c.y,
                                            c.c.x-self.c.x))

        d = self.c.distance(c.c)
        overlap = self.r+c.r - d
        if (overlap < 0):
            return []

        Q = ( d*d - c.r**2 + self.r**2 )
        x =  Q / ( 2 * d )

        y2 = ( 4*d**2*self.r**2 - Q**2 ) / (4 * d**2)
        if y2<0:
            return []

        if (abs(y2) < 0.01):
            return [
                Point(x,0).rotateTranslate(Point(0,0),
                                           undoAngle,
                                           self.c)
            ]

        y = math.sqrt(y2)

        return [
            Point(x,y).rotateTranslate(Point(0,0), undoAngle,
                                       self.c),
            Point(x,-y).rotateTranslate(Point(0,0), undoAngle,
                                        self.c),
            ]
