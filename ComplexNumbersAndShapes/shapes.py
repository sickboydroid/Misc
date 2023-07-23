import turtle

tom = turtle.Turtle()

# Get number of sides of polygon to be drawn
num_sides = int(turtle.textinput("Shapes", "Enter number of sides: "))
angle = 360 / num_sides

# determine speed
if num_sides < 10:
    speed = 1
elif num_sides < 20:
    speed = 3
elif num_sides < 50:
    speed = 6
else:
    speed = 10
tom.speed(speed)

# move tom a little bit up and to the left
tom.penup()
tom.setpos(-100, 100)
tom.pendown()

# make it pretty
tom.color("blue", "orange")
tom.pensize(2.5)

# scale side according to angle
side_length = angle * 2

# draw polygon
tom.begin_fill()
while num_sides != 0:
    tom.forward(side_length)
    tom.right(angle)
    num_sides -= 1
tom.end_fill()
turtle.done()