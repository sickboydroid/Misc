from flask import Flask, render_template, request
from flask.wrappers import Response
import sqlite3
import json

app = Flask(__name__)

@app.route("/")
def index() -> str:
    return render_template("index.html")

@app.route("/search")
def search() -> Response:
    con = sqlite3.connect('shows.db')
    cur = con.cursor()
    query = request.args.get('q', '')
    search_result = cur.execute("SELECT title FROM shows WHERE title LIKE '%{}%' LIMIT 50".format(query)).fetchall()
    return app.make_response({"result": search_result})

@app.route("/open")
def open() -> str:
    con = sqlite3.connect("shows.db")
    cur = con.cursor()
    title = request.args.get('title', '')
    return str(cur.execute("SELECT * FROM shows WHERE title = ?", (title,)).fetchall())
app.run(debug=True)
