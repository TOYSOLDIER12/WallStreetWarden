import tkinter as tk
from tkinter import ttk
import requests
import json
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime

def create_forecast_window():
    def get_forecast(ticker, days):
        url = f"http://127.0.0.1:8090/forecast?stock={ticker}&steps={days}"
        try:
            response = requests.get(url)
            if response.status_code == 200:
                return json.loads(response.text)
            else:
                print(f"Error: {response.status_code}")
        except Exception as e:
            print(f"Request failed: {e}")
        return None

    def plot_forecast(data):
        if data:
            dates = data['dates']
            values = data['values']

            if len(dates) > len(values):
                dates = dates[:len(values)]

            dates = [datetime.strptime(date.strip('"'), "%Y-%m-%d") for date in dates]

            fig, ax = plt.subplots(figsize=(10, 6))
            ax.plot(dates, values, marker='o')
            ax.set_xlabel("Date")
            ax.grid(True)
            ax.set_ylabel("Price")
            ax.set_title(f"Stock Prediction for {stock_combobox.get()}")

            ax.xaxis.set_major_locator(mdates.MonthLocator())
            ax.xaxis.set_major_formatter(mdates.DateFormatter('%b %d'))

            plt.xticks(rotation=45, ha='right')

            for i, date in enumerate(dates):
                ax.annotate(date.strftime('%Y-%m-%d'), 
                            (date, values[i]), 
                            textcoords="offset points", 
                            xytext=(0,10), 
                            ha='center', 
                            fontsize=8)

            if canvas:
                canvas.get_tk_widget().destroy()

            canvas = FigureCanvasTkAgg(fig, master=window)
            canvas.draw()
            canvas.get_tk_widget().pack(side="bottom", pady=20)
        else:
            print("No data to display.")

    def submit():
        company = stock_combobox.get()
        ticker = stock_dict.get(company)
        days = range_combobox.get()
        
        if ticker and days:
            data = get_forecast(ticker, days)
            plot_forecast(data)
        else:
            print("Please select a valid stock and range.")

    window = tk.Tk()
    window.title("Stock Forecast")

    tk.Label(window, text="Select Stock").pack(pady=5)
    
    stock_dict = {}  # Load or define stock dictionary
    company_names = list(stock_dict.keys())
    
    stock_combobox = ttk.Combobox(window, values=company_names)
    stock_combobox.pack(pady=5)

    tk.Label(window, text="Prediction Range (Days)").pack(pady=5)
    
    range_combobox = ttk.Combobox(window, values=[5, 10, 15, 30])
    range_combobox.pack(pady=5)

    tk.Button(window, text="Get Forecast", command=submit).pack(pady=20)

    window.mainloop()

