import numpy as np
import sys
import pandas as pd 
import matplotlib.pyplot as plt
from pandas.plotting import lag_plot
import requests
import datetime
import time
from statsmodels.tsa.arima.model import ARIMA
from sklearn.metrics import mean_squared_error
import json
import warnings
from statsmodels.tsa.stattools import adfuller
import itertools

warnings.filterwarnings("ignore")

def get_stock_symbol(name):
    try:
        with open("/home/toy/pfa/python/stocks.txt","r") as file:
            lines = file.readlines()
    except Exception as e:
        print(f"Failed to read stocks.txt: {e}")
    for line in lines:
        if name.lower() in line.lower():
            parts = line.split(":")
            return parts[1].strip()
    return None

def date_to_unix_timestamp(date):
    return int(time.mktime(date.timetuple()))

def get_csv(name):
    end_date = datetime.datetime.now()
    start_date = end_date - datetime.timedelta(days=365)
    period1 = date_to_unix_timestamp(start_date)
    period2 = date_to_unix_timestamp(end_date)

    url = f'https://query1.finance.yahoo.com/v7/finance/download/{name}?period1={period1}&period2={period2}&interval=1d&events=history&includeAdjustedClose=true'
    headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"}
    response = requests.get(url, headers=headers)
    if response.status_code == 200:
        try:
            with open(f"/home/toy/pfa/python/{name}.csv", 'wb') as file:
                file.write(response.content)
        except Exception as e:
            print(f"Failed to write {name}.csv: {e}")
    else:
        print(f"Failed to download {name}.csv: Status code {response.status_code}")

def test_stationarity(timeseries):
    dftest = adfuller(timeseries, autolag='AIC')
    adf_output = pd.Series(dftest[0:4], index=['Test Statistic','p-value','#Lags Used','Number of Observations Used'])
    for key, value in dftest[4].items():
        adf_output['Critical Value (%s)' % key] = value
    return adf_output

def arima(name):
    get_csv(name)
    df = pd.read_csv("/home/toy/pfa/python/"+name + ".csv")
    df['Date'] = pd.to_datetime(df['Date'])
    df.set_index('Date', inplace=True)

    # Check stationarity and difference if necessary
    result = test_stationarity(df['Close'])
    d = 0
    while result['p-value'] > 0.05 and d < 3:
        df['Close'] = df['Close'].diff().dropna()
        d += 1
        result = test_stationarity(df['Close'])
    
    # Find the best ARIMA parameters
    p = q = range(0, 5)
    pdq = list(itertools.product(p, [d], q))
    best_aic = float('inf')
    best_pdq = None
    for param in pdq:
        try:
            model = ARIMA(df['Close'], order=param)
            model_fit = model.fit()
            if model_fit.aic < best_aic:
                best_aic = model_fit.aic
                best_pdq = param
        except:
            continue
    
    # Fit the best model
    model = ARIMA(df['Close'], order=best_pdq)
    model_fit = model.fit()

    forecast = model_fit.forecast(steps=10)

    # Restore the original scale if differencing was done
    if d > 0:
        forecast = df['Close'].iloc[-1] + forecast.cumsum()

    forecast_dates = pd.date_range(start=df.index[-1], periods=11).strftime('%Y-%m-%d').tolist()

    result = {
        'forecast': forecast.tolist(),
        'forecast_dates': forecast_dates[1:]  # exclude the first date since it's the last date of training set
    }

    return json.dumps(result)

def main(stock_name):
    name = get_stock_symbol(stock_name)
    if name is None:
        print(f"Stock symbol for {stock_name} not found.")
        return
    result = arima(name)
    print(result)

if __name__ == "__main__":
    if len(sys.argv) > 1:
        name = sys.argv[1]
        main(name)
    else:
        print("Please provide a stock name as an argument.")

