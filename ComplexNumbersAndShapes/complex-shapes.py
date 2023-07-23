import turtle
import math

# NOTE: Every angle in this script is in radians

def adjust_tom_position(position=complex(0, 0)):
    tom.setpos(position.real, position.imag)

tom = turtle.Turtle()
tom.pensize(3)

# complex number that represents the position of tom in coordinate system
# real(tom_position) represents x-coordinate and imaginary(tom_position) represents y-coordinate
tom_position = complex(0, 0)

tom.penup()
tom_position = complex(-100, 100)
adjust_tom_position(tom_position)
tom.pendown()

# get number of sides
num_sides = int(turtle.textinput("Complex Shapes" ,"Enter number of sides: "))
angle = 2 * math.pi / num_sides
side_length = angle * 100
tom.color("blue", "pink")

# determine speed of turtle on the basis of sides
if num_sides < 10:
    speed = 1
elif num_sides < 20:
    speed = 4
elif num_sides < 40:
    speed = 7
elif num_sides < 50:
    speed = 9
else:
    speed = 10

# draw polygon
tom.begin_fill()
rotate_by_an_angle = 0 # angle up to which we have drawn our polygon
while num_sides != 0:
    rotate_by_an_angle += angle
    rotator = complex(math.cos(rotate_by_an_angle), math.sin(rotate_by_an_angle))
    # multiplying complex number by rotator will rotate it in anti-clockwise direction
    # dividing complex number by rotator will rotate it in clockwise direction
    tom_position += complex(side_length, 0) / rotator
    adjust_tom_position(tom_position)
    num_sides -= 1
tom.end_fill()
turtle.done()