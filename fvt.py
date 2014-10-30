#!/usr/bin/env python3

from Point import Point
from Circle import Circle

if __name__ == '__main__':

    tests = [
        [
            Circle(Point(20,20), 56),
            Circle(Point(120,100), 77),
            Point(50.423, 67.016),
            Point(72.547, 39.360),
        ]
    ]

    for c1,c2,er1,er2 in tests:
        gr1, gr2 = c1.intersect(c2)
        if gr1.x > gr2.x or abs(gr1.x-gr2.x)<.01 and gr1.y > gr2.y:
            gr1,gr2 = gr2,gr1

        assert er1==gr1, "Root #1 mismatch: {0} != {1}".format(er1,gr1)
        assert er2==gr2, "Root #2 mismatch: {0} != {1}".format(er2,gr2)
        print("Intersect:\n  {0}\n  {1}\n    -> {2}, {3}".format(
            c1,c2,gr1,gr2))


    tests = [
        [ Point(0,0), Point(1,1), 10, Point(7.07106,7.07106) ],
        [ Point(4,6), Point(7,10), 15, Point(13,18) ],
        [ Point(4,6), Point(7,10), -5, Point(1,2) ],
    ]

    for p1,p_to,dist,exp in tests:
        got = p1.distanceTowardPoint(p_to,dist)
        d = p1.distance(got)
        assert got == exp, "Expected point mismatch {0} != {1}".format(exp,got)
        assert abs(d-abs(dist))<abs(dist)/100.0, "Distance mismatch for ({0},{1},{2}) != {3}".format(p1,p_to,abs(dist),d)

        print("{0} distance ({1}) toward {2}: {3} ({4})".format(p1,dist,p_to,got,d))

    print("OK")

