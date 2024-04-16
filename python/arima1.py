import numpy as np
import sys
import pandas as pd 
import matplotlib.pyplot as plt
from pandas.plotting import lag_plot
import requests
import datetime
from statsmodels.tsa.arima.model import ARIMA
from sklearn.metrics import mean_squared_error

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
    plt.plot(df["Date"], df["Close"])
    plt.xticks(np.arange(0, 1259, 200))
    plt.title("TESLA stock price over time")
    plt.xlabel("Time")
    plt.ylabel("Price")
    plt.show()

    train_data, test_data = df[0:int(len(df)*0.7)], df[int(len(df)*0.7):]
    training_data = train_data['Close'].values
    test_data = test_data['Close'].values
    history = [x for x in training_data]
    model_predictions = []
    N_test_observations = len(test_data)
    for time_point in range(N_test_observations):
        model = ARIMA(history, order=(4,2,0))
        model_fit = model.fit()
        output = model_fit.forecast()
        yhat = output[0]
        model_predictions.append(yhat)
        true_test_value = test_data[time_point]
        history.append(true_test_value)
    MSE_error = mean_squared_error(test_data, model_predictions)
    print('Testing Mean Squared Error is {}'.format(MSE_error))

    test_set_range = df[int(len(df)*0.7):].index
    plt.plot(test_set_range, model_predictions, color='blue', marker='o', linestyle='dashed', label='Predicted Price')
    plt.plot(test_set_range, test_data, color='red', label='Actual Price')
    plt.title('TESLA Prices Prediction')
    plt.xlabel('Date')
    plt.ylabel('Prices')
    plt.xticks(np.arange(881, 1259, 50), df.Date[881:1259:50])
    plt.legend()
    plt.show()

def main(stock_name):
    arima(stock_name)

if __name__ == "__main__":
    name = sys.argv[1]
    main(name)

