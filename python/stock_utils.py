import yfinance as yf
import os
import pandas as pd
import sys

def get_stock_symbol(name):
    try:
        with open("/home/toy/pfa/python/stocks.txt", "r") as file:
            lines = file.readlines()
    except Exception as e:
        print(f"Failed to read stocks.txt: {e}", file=sys.stderr)
        return None
    
    for line in lines:
        if name.lower() in line.lower():
            parts = line.split(":")
            return parts[1].strip()
    return None

# Function to get and save stock data to CSV
def get_csv(name):
    try:
        symbol = get_stock_symbol(name)
        if not symbol:
            raise ValueError(f"Stock symbol for {name} not found!")

        # File path to save the CSV
        csv_path = f"/home/toy/pfa/python/csv/{symbol}.csv"
        
        # Download the stock data using yfinance
        stock_data = yf.download(symbol, period="1y", interval="1d")
        
        # Save it to a CSV file
        stock_data.reset_index(inplace=True)
        if isinstance(stock_data.columns, pd.MultiIndex):
            stock_data.columns = stock_data.columns.get_level_values(0)

        stock_data.columns = ['Date'] + [col.replace(' ', '_').lower() for col in stock_data.columns[1:]]
        stock_data.to_csv(csv_path, index=False)

        # Check if the file was actually saved
        if os.path.exists(csv_path):
            print(f"{symbol}.csv successfully written!", file=sys.stderr)
            return csv_path
        else:
            raise FileNotFoundError(f"{symbol}.csv was not written.")
    
    except Exception as e:
        print(f"Failed to download or write {symbol}.csv: {e}", file=sys.stderr)
        raise


if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        print("Usage: python stock_utils.py <stock_name>")
    else:
        stock_name = sys.argv[1]
        get_csv(stock_name)
