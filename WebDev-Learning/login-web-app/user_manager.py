import sqlite3
from sqlite3 import Cursor

class UserManager:
    def __init__(self, db='users.db'):
        self.con = sqlite3.connect(db)
        self.cur = self.con.cursor()

    def does_user_exist(self, username: str):
        return self.cur.execute("SELECT username FROM users WHERE username = ?", (username,)).fetchone() is not None

    def verify_user(self, username: str, password: str) -> bool:
        if not self.does_user_exist(username):
            return False
        user = self.cur.execute(
            "SELECT password FROM users WHERE username = ? AND password = ?", (username, password)).fetchone()
        return user is not None

    def add_user(self, username: str, password: str):
        if self.does_user_exist(username):
            raise UserAlreadyExistsError
        if not username.strip() or not password.strip():
            raise InvalidUsernamePasswordError
        self.cur.execute("INSERT INTO users VALUES(?, ?)", (username, password))
        self.con.commit()
        return True

    def remove_user(self, username: str, password: str):
        if not self.verify_user(username, password):
            return
        self.cur.execute("DELETE FROM users WHERE username = ? AND password = ?", (username, password))
        self.con.commit()
        return True

    def close_db(self):
        self.con.close()

class Username:
    def is_valid_name(self, username: str):
        pass

class UserAlreadyExistsError(Exception):
    "User already exists"
    pass

class InvalidUsernamePasswordError(Exception):
    "Invalid username and/or password"
    pass