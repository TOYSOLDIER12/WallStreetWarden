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
from stock_utils import get_csv

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

def get_csv_file(name):
  get_csv(name)


def test_stationarity(timeseries):
    dftest = adfuller(timeseries, autolag='AIC')
    adf_output = pd.Series(dftest[0:4], index=['Test Statistic','p-value','#Lags Used','Number of Observations Used'])
    for key, value in dftest[4].items():
        adf_output['Critical Value (%s)' % key] = value
    return adf_output

def arima(name, step):
    try:
        # Ensure the CSV is available or downloaded
        csv_file = get_csv(name)

        # Proceed with ARIMA processing if the CSV file exists
        df = pd.read_csv(csv_file)
        df['Date'] = pd.to_datetime(df['Date'])
        df.set_index('Date', inplace=True)
        df = df.asfreq('D')

        df['Close'] = df['Close'].ffill()

        model = ARIMA(df['Close'], order=(4, 1, 0))
        model_fit = model.fit()

        # Forecast the next 'step' days
        forecast = model_fit.forecast(steps=step)

        # Get forecast dates
        last_date = df.index[-1]
        forecast_dates = pd.date_range(start=last_date + pd.Timedelta(days=1), periods=step).strftime('%Y-%m-%d').tolist()

        result = {
            'forecast': forecast.tolist(),
            'forecast_dates': forecast_dates
        }

        # Only return the result, no extra messages
        return json.dumps(result)
    except Exception as e:
        return json.dumps({"error": str(e)})



# Main function
def main(stock_name, step):
    name = get_stock_symbol(stock_name)
    
    # Handle case where stock symbol is not found
    if not name:
        print(f"Stock symbol for '{stock_name}' not found.")
        return
    
    # Convert step to integer and run ARIMA
    try:
        step = int(step)
    except ValueError:
        print("Error: 'step' must be an integer.")
        return

    result = arima(name, step)
    print(result)

# Command-line execution
if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python arima.py <stock_name> <steps>")
        sys.exit(1)

    name = sys.argv[1]
    step = sys.argv[2]
    main(name, step)
