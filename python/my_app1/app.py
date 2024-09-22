import os
import pandas as pd
import csv
from io import StringIO
import sys
import requests
import json
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
from datetime import datetime
from PyQt5.QtWidgets import (
    QApplication, QWidget, QVBoxLayout, QLabel, QLineEdit,
    QPushButton, QFileDialog, QFormLayout, QMessageBox, QComboBox,QSizePolicy,QLayout,
    QDesktopWidget
)





class SignUpWindow(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        self.setWindowTitle("Sign Up")
        self.setGeometry(100, 100, 300, 200)

        layout = QFormLayout()

        self.username_input = QLineEdit()
        self.password_input = QLineEdit()
        self.password_input.setEchoMode(QLineEdit.Password)
        self.profile_pic_path = ""

        layout.addRow(QLabel("Username:"), self.username_input)
        layout.addRow(QLabel("Password:"), self.password_input)

        self.upload_button = QPushButton("Upload Profile Picture")
        self.upload_button.clicked.connect(self.upload_picture)
        layout.addRow(self.upload_button, QLabel(self.profile_pic_path))

        self.signup_button = QPushButton("Sign Up")
        self.signup_button.clicked.connect(self.signup)
        layout.addRow(self.signup_button)

        self.setLayout(layout)

    def upload_picture(self):
        options = QFileDialog.Options()
        file_name, _ = QFileDialog.getOpenFileName(self, "Select Profile Picture", "", "Images (*.png *.jpg *.jpeg);;All Files (*)", options=options)
        if file_name:
            self.profile_pic_path = file_name
            self.upload_button.setText(f"Profile Picture: {file_name.split('/')[-1]}")

    def signup(self):


        username = self.username_input.text()
        password = self.password_input.text()
    
        if not username or not password:
            QMessageBox.warning(self, "Warning", "Please fill all fields")
            return

        url = "http://127.0.0.1:8090/signup"

        user_data = json.dumps({
        'username': username,
        'password': password
    })

        # Prepare files and user data in multipart form-data format
        files = {
            'user': ('user', user_data, 'application/json')  # Include user data as a part of files
    }


 

        try:
        # Make the POST request
            response = requests.post(url, files=files)

            if response.status_code == 200:
                QMessageBox.information(self, "Success", "Sign-up successful!")
                self.open_login_window()
            else:
                QMessageBox.critical(self, "Error", f"Sign-up failed! Status Code: {response.status_code}\n{response.text}")

        except Exception as e:
            QMessageBox.critical(self, "Error", str(e))
    
    def open_login_window(self):
        self.login_window = LoginWindow()
        self.login_window.show()
        self.close()  # Close signup window




class LoginWindow(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        self.setWindowTitle("Login")
        self.setGeometry(100, 100, 300, 200)

        layout = QFormLayout()

        self.username_input = QLineEdit()
        self.password_input = QLineEdit()
        self.password_input.setEchoMode(QLineEdit.Password)

        layout.addRow(QLabel("Username:"), self.username_input)
        layout.addRow(QLabel("Password:"), self.password_input)

        self.login_button = QPushButton("Login")
        self.login_button.clicked.connect(self.login)

        layout.addRow(self.login_button)

        self.signup_button = QPushButton("Sign Up")
        self.signup_button.clicked.connect(self.open_signup_window)
        layout.addRow(self.signup_button)

        self.setLayout(layout)

    def login(self):
        username = self.username_input.text()
        password = self.password_input.text()

        if not username or not password:
            QMessageBox.warning(self, "Warning", "Please fill all fields")
            return

        url = "http://127.0.0.1:8090/login"
        payload = {'username': username, 'password': password}

        try:
            response = requests.post(url, json=payload)

            if response.status_code == 200:
                
                data = response.json()
                token = data.get("token")

                if token:
                    # Proceed to Main Menu if login is successful
                    self.open_main_menu(token)

                else:
                    QMessageBox.critical(self, "Login Error", "Token not received. Login failed.")
            else:
                QMessageBox.warning(self, "Login Error", f"Login failed: {response.status_code}")
            
        except Exception as e:
            QMessageBox.critical(self, "Request Error", f"Login request failed: {e}")


    def open_signup_window(self):
        self.signup_window = SignUpWindow()
        self.signup_window.show()
        self.close()  # Close login window

    def open_main_menu(self, token):
        # Hide the login window and show the main menu
        self.hide()
        self.main_menu = MainMenu(token)  # Pass the token to MainMenu
        self.main_menu.show()
    
    


class MainMenu(QWidget):
    def __init__(self, token):
        super().__init__()
        self.token = token  # Save the token for authenticated requests
        
        self.profile_data = self.fetch_user_profile()  # Fetch profile data
        self.username = self.profile_data.get('username', 'User') if self.profile_data else "User"

        

        # Load stock names and tickers from the text file
        self.stock_dict = {}
        self.load_stocks()
        self.canvas = None
        self.setMinimumSize(800, 600)

        self.initUI()

    def load_stocks(self):
        # Load stock data from file
        with open('stocks.txt', 'r') as f:
            lines = f.readlines()
            for line in lines:
                company, ticker = line.strip().split(": ")
                self.stock_dict[company] = ticker

        self.company_names = list(self.stock_dict.keys())  # Store company names for search

    def initUI(self):
        self.setWindowTitle("Stock Prediction & History")
        self.setGeometry(100, 100, 800, 600)
        self.center()


        # Display username and profile details
        username_label = QLabel(f"Welcome, {self.username}!", self)
        if self.profile_data:
            self.load_user_profile()

        # Search Bar
        self.search_bar = QLineEdit(self)
        self.search_bar.setPlaceholderText("Search for a stock...")
        self.search_bar.textChanged.connect(self.update_stock_list)

        # Stock Dropdown (ComboBox)
        self.stock_combobox = QComboBox(self)
        self.stock_combobox.addItems(self.company_names)  # Initially fill with all stocks

        # Range Dropdown for Forecast (1 to 10)
        self.range_combobox = QComboBox(self)
        self.range_combobox.addItems([str(i) for i in range(1, 11)])  # Range between 1 and 10
        self.range_combobox.currentIndexChanged.connect(self.update_forecast)

        # Forecast Button
        self.forecast_button = QPushButton("Forecast", self)
        self.forecast_button.clicked.connect(self.get_forecast)

        # Show History Button
        self.history_button = QPushButton("Show History", self)
        self.history_button.clicked.connect(self.show_history)



        
        # Layout
        layout = QVBoxLayout()
        layout.addWidget(QLabel(f"Token: {self.token}", self))
        layout.addWidget(QLabel("Search for a Stock:"))
        layout.addWidget(self.search_bar)
        layout.addWidget(QLabel("Select a Stock:"))
        layout.addWidget(self.stock_combobox)
        layout.addWidget(QLabel("Select Forecast Range (Days):"))
        layout.addWidget(self.range_combobox)
        layout.addWidget(self.forecast_button)
        layout.addWidget(self.history_button)

      

        self.setLayout(layout)
        self.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        
    def update_stock_list(self):
        # Get the search term and filter stock list
        search_term = self.search_bar.text().lower()
        filtered_stocks = [company for company in self.company_names if search_term in company.lower()]
        self.stock_combobox.clear()
        self.stock_combobox.addItems(filtered_stocks)

    def get_forecast(self):
        # Get the selected stock and range
        selected_stock = self.stock_combobox.currentText()
        ticker = self.stock_dict.get(selected_stock)
        days = self.range_combobox.currentText()

        if ticker:
            url = f"http://127.0.0.1:8090/forecast?stock={ticker}&steps={days}"
            headers = {'Authorization': f'Bearer {self.token}'}

            try:
                response = requests.get(url, headers=headers)
                if response.status_code == 200:
                    data = response.json()
                    self.plot_forecast(data)
                else:
                    QMessageBox.warning(self, "Error", f"Failed to get forecast: {response.status_code}")
            except Exception as e:
                QMessageBox.critical(self, "Error", f"Request failed: {e}")
        else:
            QMessageBox.warning(self, "Error", "Invalid stock selected.")

    def show_history(self):
        # Get the selected stock
        selected_stock = self.stock_combobox.currentText()
        ticker = self.stock_dict.get(selected_stock)

        if ticker:
            url = f"http://127.0.0.1:8090/download-csv?fileName={ticker}"
            headers = {'Authorization': f'Bearer {self.token}'}

            try:
                response = requests.get(url, headers=headers)
                if response.status_code == 200:
                  
                    content_type = response.headers.get('Content-Type')

                    if 'text/csv' in content_type:  # If it’s CSV
                        csv_data = response.text  # Get the CSV data as plain text
                        self.display_history(csv_data)
                    else:
                        QMessageBox.warning(self, "Error", "Unexpected content type received.")


                else:
                    QMessageBox.warning(self, "Error", f"Failed to get history: {response.status_code}")
            except Exception as e:
                QMessageBox.critical(self, "Error", f"Request failed: {e}")
        else:
            QMessageBox.warning(self, "Error", "Invalid stock selected.")

    def plot_forecast(self, data):
        if data:
            dates = data['dates']
            values = data['values']

            
            if len(dates) > len(values):
                dates = dates[:len(values)]

            dates = [datetime.strptime(date.strip('"'), "%Y-%m-%d") for date in dates]

            # Create a matplotlib figure
            fig, ax = plt.subplots(figsize=(10, 6))
            ax.plot(dates, values, marker='o')
            ax.set_xlabel("Date")
            ax.grid(True)
            ax.set_ylabel("Price")
            ax.set_title(f"Stock Prediction for {self.stock_combobox.currentText()}")

            ax.xaxis.set_major_locator(mdates.MonthLocator())
            ax.xaxis.set_major_formatter(mdates.DateFormatter('%b %d'))

            plt.xticks(rotation=45, ha='right')

            for i, date in enumerate(dates):
                ax.annotate(date.strftime('%Y-%m-%d'),
                            (date, values[i]),
                            textcoords="offset points",
                            xytext=(0, 10),
                            ha='center',
                            fontsize=8)

            # Display the plot in the PyQt5 window
            if self.canvas:
                self.canvas.deleteLater()
                self.canvas = None

            self.canvas = FigureCanvas(fig)
            self.layout().addWidget(self.canvas)
            self.canvas.draw()
        else:
            QMessageBox.warning(self, "Error", "No data to display.")    


    def display_history(self, csv_data):
        try:
            df = pd.read_csv(StringIO(csv_data))

        # Make sure to adjust the column names based on your CSV
            dates = pd.to_datetime(df['Date'])  # Adjust this if your date column has a different name
            values = df['Close']  # Adjust for the actual value column

            self.plot_history(dates, values)
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Failed to parse CSV data: {e}")


    def plot_history(self, dates, values):
        # Create a matplotlib figure
        fig, ax = plt.subplots(figsize=(10, 6))
        ax.plot(dates, values, marker='o')
        ax.set_xlabel("Date")
        ax.set_ylabel("Price")
        ax.set_title(f"Stock History for {self.stock_combobox.currentText()}")
        ax.grid(True)

    # Format the x-axis
        ax.xaxis.set_major_locator(mdates.MonthLocator())
        ax.xaxis.set_major_formatter(mdates.DateFormatter('%b %d'))

        plt.xticks(rotation=45, ha='right')

    # Display the plot in the PyQt5 window
        if self.canvas:
            self.canvas.deleteLater()

        self.canvas = FigureCanvas(fig)
        self.layout().addWidget(self.canvas)
        self.canvas.draw()

    def center(self):
        qr = self.frameGeometry()
        cp = QDesktopWidget().availableGeometry().center()
        qr.moveCenter(cp)
        self.move(qr.topLeft())

    def update_forecast(self):
        """Update the forecast when the range changes."""
        self.get_forecast()  # Re-fetch forecast data

    def fetch_user_profile(self):
        url = "http://127.0.0.1:8090/profile"  # Adjust URL as necessary
        headers = {'Authorization': f'Bearer {self.token}'}

        try:
            response = requests.get(url, headers=headers)
            if response.status_code == 200:
                profile_data = response.json()
                return profile_data
            else:
                QMessageBox.warning(self, "Error", f"Failed to get profile: {response.status_code}")
                return None
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Request failed: {e}")
            return None


    def display_user_profile(self, user_data):
        username = user_data.get('username', 'Unknown User')
        profile_picture_url = user_data.get('profilePictureUrl', '')  # Adjust based on your API response

    # Display username
        username_label = QLabel(f"Username: {username}", self)

        if profile_picture_url:
            response = requests.get(profile_picture_url)
            img = QImage()
            img.loadFromData(response.content)
            profile_picture_label = QLabel(self)
            profile_picture_label.setPixmap(QPixmap.fromImage(img).scaled(100, 100))  # Resize as needed

            layout.addWidget(profile_picture_label)  # Add to your existing layout
        else:
            profile_picture_label = QLabel("No profile picture available.", self)
    
        layout.addWidget(username_label)

        
if __name__ == "__main__":
    app = QApplication(sys.argv)

    # Start with the Login window (or directly jump to MainMenu for testing)
    # login_window = LoginWindow()  # Uncomment when integrating with the login flow
    # login_window.show()

    # Direct MainMenu test
    login_window = LoginWindow()
    login_window.show()

    sys.exit(app.exec_())
