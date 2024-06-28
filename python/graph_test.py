import matplotlib.pyplot as plt
import datetime

# Data
values = [203.65714632845953, 210.85397700949417, 219.28507152924436, 226.52435400176137, 235.00535933363798,
          245.3442285363644, 255.79048247393555, 266.6652753712271, 278.3877203372799, 291.2539675754655]
dates = ["2024-06-27", "2024-06-28", "2024-06-29", "2024-06-30", "2024-07-01", "2024-07-02", "2024-07-03",
         "2024-07-04", "2024-07-05", "2024-07-06"]

# Convert date strings to datetime objects
dates = [datetime.datetime.strptime(date, '%Y-%m-%d') for date in dates]

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
