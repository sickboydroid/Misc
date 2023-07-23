from flask import Flask, render_template, request, session
import sqlite3

app = Flask(__name__)
app.secret_key = '1021n194'

@app.route("/")
def index():
    return render_template("login.html")


@app.route("/addtodo", methods=['POST', 'GET'])
def addTodo():
    connec = sqlite3.Connection('users.db')
    cur = connec.cursor()

    # verify user
    username = request.form.get('username')
    password = request.form.get('password')
    if not username or not password:
        if 'username' in session:
            username = session['username']
            password = session['password']
        else:
            return render_template("failure.html", failure_msg="Invalid username or password")
    userinfo = cur.execute(
        "SELECT username, password FROM users WHERE username=? AND password=?", (username, password)).fetchall()
    if not userinfo:
        return render_template("failure.html", failure_msg="User does not exist. Signup before continuing")
    print(userinfo)
    session['username'] = userinfo[0][0]
    session['password'] = userinfo[0][1]

    print(request.form)
    if request.method == 'POST':
        if 'todo' in request.form:
            todo = request.form.get('todo')
            cur.execute("INSERT INTO todos VALUES(?, ?)", (username, todo))

    # get todos
    todos = cur.execute("SELECT todo FROM todos WHERE username=?", (username,)).fetchall()
    refinedTodos = []
    for todo in todos:
        refinedTodos.append(todo[0])
    connec.commit()
    connec.close()
    return render_template("addtodo.html", todos=refinedTodos)


@app.route("/login")
def login():
    return render_template("login.html")


@app.route("/failure")
def failure():
    return render_template("failure.html")


Flask.run(app, debug=True)
