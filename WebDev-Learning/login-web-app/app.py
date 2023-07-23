from flask import Flask, render_template, request, redirect
from flask import session
from user_manager import UserManager
from user_manager import InvalidUsernamePasswordError, UserAlreadyExistsError

app = Flask(__name__)
# TODO: shoudl it really be this way
app.secret_key = "1021n194"


@app.route("/")
def index():
    if has_login_info():
        return render_template("index.html", username=session['username'])
    return redirect("/login")


@app.route("/create-account")
def create_account():
    if has_login_info():
        redirect("/")
    return render_template("create-account.html")


@app.route("/login", methods=["GET", "POST"])
def login():
    usermanger = UserManager()
    if request.method == 'POST':
        username = request.form.get('username')
        password = request.form.get('password')
    else:
        username = request.args.get('username')
        password = request.args.get('password')

    if not username or not password:
        if has_login_info():
            return redirect("/")
        return render_template("login.html")
    if usermanger.verify_user(username, password):
        save_login_info(username, password)
        return redirect("/")
    return render_template("error.html", msg="Invalid username and/or password")


@app.route("/add-user", methods=['POST', 'GET'])
def add_user():
    usermanger = UserManager()
    if request.method == 'POST':
        username = request.form.get('username')
        password = request.form.get('password')
    else:
        username = request.args.get('username')
        password = request.args.get('password')

    # check if valid username password
    error_msg = None
    if not str.isalnum(username) or len(username) < 6:
        error_msg = "Invalid username. Username should be alphanumeric and at least 6 characters long"
    elif not password:
        error_msg = "Password cannot be empty"
    elif len(password) < 6:
        error_msg = "Password must be at least 6 characters long"
    if error_msg:
        return render_template("error.html", msg=error_msg)

    # check if already exits
    if usermanger.does_user_exist(username):
        return render_template("error.html",
                               msg=f"User {username} already exists. Please pick another name")

    # add user
    try:
        usermanger.add_user(username, password)
    except UserAlreadyExistsError:
        return render_template("error.html", msg="User already exists")
    except InvalidUsernamePasswordError:
        return render_template("error.html",
                               msg="Invalid username and/or password")

    save_login_info(username, password)
    return redirect('/')


def save_login_info(username: str, password: str):
    session['username'] = username
    session['password'] = password


def has_login_info():
    return 'username' and 'password' in session


app.run(debug=True)
