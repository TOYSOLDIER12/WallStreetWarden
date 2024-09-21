import tkinter as tk
from tkinter import ttk
import requests

def create_history_window():
    def view_history():
        company = stock_combobox.get()
        ticker = stock_dict.get(company)

        if ticker:
            response = requests.get(f'http://127.0.0.1:8090/history?stock={ticker}')
            if response.status_code == 200:
                csv_data = response.text
                print(csv_data)  # Process or display CSV data
            else:
                print(f"Error: {response.status_code} - {response.text}")
        else:
            print("Please select a valid stock.")

    window = tk.Tk()
    window.title("Stock History")

    tk.Label(window, text="Select Stock").pack(pady=5)
    
    stock_dict = {}  # Load or define stock dictionary
    company_names = list(stock_dict.keys())
    
    stock_combobox = ttk.Combobox(window, values=company_names)
    stock_combobox.pack(pady=5)

    tk.Button(window, text="View History", command=view_history).pack(pady=20)

    window.mainloop()

