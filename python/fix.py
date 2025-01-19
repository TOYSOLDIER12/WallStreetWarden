import pandas as pd

# Load the file (force it to read properly)
csv_file = "csv/AAPL.csv"
df = pd.read_csv(csv_file)

# Print out the columns for inspection
print("Columns in CSV:", df.columns)

# Clean column names
df.columns = df.columns.str.strip().str.lower()  # Strip spaces and make lowercase
print("Cleaned Columns:", df.columns)

# Drop any unrelated columns (optional)
df = df[['date', 'close']]  # Keep only what's needed

# Check for missing values
print("Missing values:\n", df.isnull().sum())

# Save cleaned CSV
df.to_csv("cleaned_AAPL.csv", index=False)

