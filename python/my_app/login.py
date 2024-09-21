import tkinter as tk
import requests

def create_login_window():
    def login():
        username = username_entry.get()
        password = password_entry.get()

        if username and password:
            response = requests.post('http://127.0.0.1:8090/login', 
                                     json={'username': username, 'password': password})
            
            if response.status_code == 200:
                print("Login successful!")
            else:
                print(f"Error: {response.status_code} - {response.text}")
        else:
            print("Please fill in all fields.")

    window = tk.Tk()
    window.title("Login")

    tk.Label(window, text="Username:").pack(pady=5)
    username_entry = tk.Entry(window)
    username_entry.pack(pady=5)

    tk.Label(window, text="Password:").pack(pady=5)
    password_entry = tk.Entry(window, show="*")
    password_entry.pack(pady=5)

    tk.Button(window, text="Login", command=login).pack(pady=20)

    window.mainloop()

