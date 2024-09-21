import tkinter as tk
from tkinter import ttk

def open_signup():
    import signup
    signup.create_signup_window()

def open_login():
    import login
    login.create_login_window()

def open_history():
    import history
    history.create_history_window()

def open_forecast():
    import forecast
    forecast.create_forecast_window()

window = tk.Tk()
window.title("StockBotSage")

tk.Button(window, text="Sign Up", command=open_signup).pack(pady=10)
tk.Button(window, text="Login", command=open_login).pack(pady=10)
tk.Button(window, text="View History", command=open_history).pack(pady=10)
tk.Button(window, text="View Forecast", command=open_forecast).pack(pady=10)

window.mainloop()

