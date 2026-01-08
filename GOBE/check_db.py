import sqlite3
try:
    conn = sqlite3.connect('c:/Users/maxen/Documents/POLYTECH/INFO732/INFO732-GOBE/db/database.db')
    cursor = conn.cursor()
    cursor.execute("SELECT name FROM sqlite_master WHERE type='table'")
    tables = cursor.fetchall()
    print("Tables:", tables)
except Exception as e:
    print(e)
