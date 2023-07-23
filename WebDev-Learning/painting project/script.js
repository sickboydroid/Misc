let btnRed = document.getElementById("red")
let btnGreen = document.getElementById("green")
let btnBlue = document.getElementById("blue")
let drawingBoard = document.getElementById("drawing-board")
let context = drawingBoard.getContext("2d")
let color = "red"

btnRed.addEventListener("click", function () {
    color = "red"
})
btnGreen.addEventListener("click", function () {
    color = "green"
})
btnBlue.addEventListener("click", function () {
    color = "blue"
})
document.onmousemove = function (event) {
    console.log(event.clientX + ", " + event.clientY)
    context.strokeStyle = color
    context.lineTo(event.clientX, event.clientY)
    context.stroke()
}