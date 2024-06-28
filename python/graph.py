import requests
import matplotlib.pyplot as plt
import datetime

def get_forecast(stock_name):
    url = f"http://localhost:8090/forecast?stock={stock_name}"
    try:
        response = requests.get(url)
        response.raise_for_status()  # This will raise an HTTPError for bad responses
        return response.json()
    except requests.exceptions.HTTPError as http_err:
        print(f"HTTP error occurred: {http_err}")
        print(f"Response content: {response.content}")
    except Exception as err:
        print(f"Other error occurred: {err}")
    return None

def plot_forecast(data):
    if not data:
        print("No data to plot")
        return
    
    # Assuming the data is received as a dictionary with 'values' and 'dates' keys
    values = data['values']
    dates = data['dates']

    # Remove extra quotation marks and convert date strings to datetime objects
    dates = [datetime.datetime.strptime(date.strip('"'), '%Y-%m-%d') for date in dates]

    # Ensure dates and values have the same length
    if len(dates) > len(values):
        dates = dates[:len(values)]
    elif len(values) > len(dates):
        values = values[:len(dates)]

    # Plotting
    plt.figure(figsize=(10, 6))
    plt.plot(dates, values, marker='o', linestyle='-', color='b')

    # Formatting the plot
    plt.title('Predicted Stock Prices for the Next 10 Days')
    plt.xlabel('Date')
    plt.ylabel('Predicted Price')
    plt.grid(True)
    plt.xticks(rotation=45)
    plt.tight_layout()

    # Show the plot
    plt.show()

if __name__ == "__main__":
    stock_name = "TSLA"  # Replace with the stock name you want to query
    forecast_data = get_forecast(stock_name)
    plot_forecast(forecast_data)

