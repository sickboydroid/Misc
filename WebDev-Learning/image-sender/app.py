from flask import Flask
app = Flask(__name__)

@app.route("/")
def index():
    with open('static/img.png', 'rb') as file_image:
        img = file_image.read()
    response = app.make_response(img)
    response.headers.set("Content-type", 'image/png')
    return response

app.run(debug=True)
