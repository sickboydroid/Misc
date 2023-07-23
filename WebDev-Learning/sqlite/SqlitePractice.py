import sqlite3
import random

connection = sqlite3.connect("books.db")
cur = connection.cursor()
# cur.execute("CREATE TABLE books (id INTEGER PRIMARY KEY, name TEXT, pages INTEGER, rating REAL)")
while True:
    id = random.randint(0, 1000)
    name = input("Book Name: ")
    pages = input("Pages: ")
    rating = input("Rating: ")
    try:
        cur.execute("INSERT INTO books VALUES(?, ?, ?, ?)", (id, name, pages, rating))
        connection.commit()
    except Exception as e:
        print(f'Something wrong happend {e}')
    for row in cur.fetchall():
        print(row)
    choice = input("Do you wanna exit?")
    if choice not in ['', 'n', 'no', 'NO', 'No']:
        break
connection.commit()
connection.close()
