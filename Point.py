#!/usr/bin/env python3

import math

class Point:
    def __init__( self, x, y ):
        self.x = x
        self.y = y

    def __str__( self ):
        return "({0:.03f},{1:.03f})".format(self.x,self.y)

    def __eq__( self, p ):
        return (abs(self.x-p.x) < 0.02 and 
                abs(self.y-p.y) < 0.02)

    def distance( self, p ):
        return math.sqrt((self.x-p.x)**2+(self.y-p.y)**2)

    def midpoint( self, p ):
        return Point((self.x+p.x)/2,(self.y+p.y)/2)

    def slope( self, p ):
        return (p.y-self.y)/(p.x-self.x)

    def rotateTranslate( self, p, angle, offset ):
        sin_a = math.sin(math.radians(angle))
        cos_a = math.cos(math.radians(angle))

        # translate p to the origin
        x,y = self.x - p.x,self.y - p.y

        x,y = cos_a * x - sin_a * y, sin_a * x + cos_a * y

        # translate back to p + offset
        return Point( x + p.x + offset.x,
                      y + p.y + offset.y, )

    def distanceTowardPoint( self, p, dist ):
        # Translate myself to the origin and rotate the frame of reference
        #   so that the two points lie on the x-axis.

        return Point(dist,0).rotateTranslate(
            Point(0,0),
            math.degrees(math.atan2(p.y-self.y,p.x-self.x)),
            self,
        )
                                             
    def coords( self ):
        return (self.x,self.y)
