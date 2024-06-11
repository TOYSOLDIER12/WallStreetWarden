import numpy as np
import sys
import pandas as pd 
import matplotlib.pyplot as plt
from pandas.plotting import lag_plot
import requests
import datetime
from statsmodels.tsa.arima.model import ARIMA
from sklearn.metrics import mean_squared_error
import json

def get_csv(name):
    url = f'https://query1.finance.yahoo.com/v7/finance/download/{name}?period1=1681551861&period2=1713170661&interval=1d&events=history&includeAdjustedClose=true'
    headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"}
    response = requests.get(url, headers = headers)
    if response.status_code == 200:
        try:
            with open(f"{name}.csv", 'wb') as file:
                file.write(response.content)
            print(f"{name}.csv has been successfully downloaded.")
        except Exception as e:
            print(f"Failed to write {name}.csv: {e}")
    else:
        print(f"Failed to download {name}.csv: Status code {response.status_code}")

def arima(name):
    get_csv(name)
    df = pd.read_csv(name + ".csv")

    model = ARIMA(df['Close'], order=(4, 3, 0))
    model_fit = model.fit()

    forecast = model_fit.forecast(steps=10)

    forecast_dates = pd.date_range(start=df['Date'].iloc[-1], periods=11, closed='right').strftime('%Y-%m-%d').tolist()

    result = {
        'forecast': forecast.tolist(),
        'forecast_dates': forecast_dates
    }

    return json.dumps(result)

def main(stock_name):
    result = arima(stock_name)
    print(result)

if __name__ == "__main__":
    name = sys.argv[1]
    main(name)

