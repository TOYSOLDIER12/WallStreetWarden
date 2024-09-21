import tkinter as tk
from tkinter import filedialog
import requests
import json

# Global variables for entry widgets
username_entry = None
password_entry = None
name_entry = None
profile_picture_path = None

def signup():
    global username_entry, password_entry, name_entry, profile_picture_path

    username = username_entry.get()
    name = name_entry.get()
    password = password_entry.get()
    profile_picture = profile_picture_path.get()

    if username and name and password:
        user_data = {
            'username': username,
            'name': name,
            'password': password
        }

        user_json = json.dumps(user_data).encode('utf-8')

        files = {}
        if self.profile_pic_path:
            with open(self.profile_pic_path, 'rb') as file:
                files['profilePicture'] = ('profilePicture', file, 'image/jpeg')  # Image part
        else:
            print("No profile picture selected")

        try:
            # Send the POST request with multipart form data
            response = requests.post(url, files=files)

            # Check the response
            if response.status_code == 200:
                QMessageBox.information(self, "Success", "Sign-up successful!")
            else:
                QMessageBox.critical(self, "Error", f"Sign-up failed! Status Code: {response.status_code}\n{response.text}")

        except Exception as e:
            QMessageBox.critical(self, "Error", str(e))


def create_signup_window():
    global username_entry, password_entry, name_entry, profile_picture_path

    window = tk.Tk()
    window.title("Signup")

    tk.Label(window, text="Username:").pack(pady=5)
    username_entry = tk.Entry(window)
    username_entry.pack(pady=5)

    tk.Label(window, text="Password:").pack(pady=5)
    password_entry = tk.Entry(window, show="*")
    password_entry.pack(pady=5)

    tk.Label(window, text="Name:").pack(pady=5)
    name_entry = tk.Entry(window)
    name_entry.pack(pady=5)

    tk.Label(window, text="Profile Picture:").pack(pady=5)
    profile_picture_path = tk.StringVar()
    tk.Entry(window, textvariable=profile_picture_path).pack(pady=5)
    tk.Button(window, text="Browse", command=lambda: profile_picture_path.set(filedialog.askopenfilename())).pack(pady=5)

    tk.Button(window, text="Sign Up", command=signup).pack(pady=20)

    window.mainloop()

create_signup_window()


lass LoginWindow(QWidget):
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

        self.setLayout(layout)

    def login(self):
        username = self.username_input.text()
        password = self.password_input.text()

        if not username or not password:
            QMessageBox.warning(self, "Warning", "Please fill all fields")
            return

        url = "http://127.0.0.1:8090/login"
        data = {'username': username, 'password': password}

        try:
            response = requests.post(url, json=data)

            if response.status_code == 200:
                QMessageBox.information(self, "Success", "Login successful!")
                self.open_main_menu(response.json()["token"])
            else:
                QMessageBox.critical(self, "Error", f"Login failed! Status Code: {response.status_code}\n{response.text}")

        except Exception as e:
            QMessageBox.critical(self, "Error", str(e))

    def open_main_menu(self, token):
        self.main_menu = MainMenu(token)
        self.main_menu.show()
        self.close()


class MainMenu(QWidget):
    def __init__(self, token):
        super().__init__()
        self.token = token
        self.init_ui()

    def init_ui(self):
        self.setWindowTitle("Main Menu")
        self.setGeometry(100, 100, 500, 400)

        layout = QVBoxLayout()

        # Stock List
        self.stock_list = QListWidget()
        self.load_stocks()

        # Range ComboBox (5 to 10 days)
        self.range_combo = QComboBox()
        self.range_combo.addItems([str(i) for i in range(5, 11)])

        layout.addWidget(QLabel("Select Stock:"))
        layout.addWidget(self.stock_list)
        layout.addWidget(QLabel("Select Range (days):"))
        layout.addWidget(self.range_combo)

        # Buttons for actions
        self.history_button = QPushButton("Show History")
        self.history_button.clicked.connect(self.show_history)

        self.predict_button = QPushButton("Predict Stock Price")
        self.predict_button.clicked.connect(self.predict_stock)

        layout.addWidget(self.history_button)
        layout.addWidget(self.predict_button)

        self.setLayout(layout)

    def load_stocks(self):
        try:
            with open('stocks.txt', 'r') as f:
                stocks = f.read().splitlines()
                self.stock_list.addItems(stocks)
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Failed to load stocks: {str(e)}")

    def show_history(self):
        selected_stock = self.stock_list.currentItem()
        if not selected_stock:
            QMessageBox.warning(self, "Warning", "Please select a stock.")
            return

        stock_name = selected_stock.text()
        range_days = self.range_combo.currentText()

        url = f"http://127.0.0.1:8090/download-csv?stock={stock_name}&range={range_days}"

        try:
            response = requests.get(url, headers={'Authorization': f'Bearer {self.token}'})

            if response.status_code == 200:
                # Process CSV data and plot the graph (using matplotlib)
                csv_data = response.text
                self.plot_graph(csv_data)
            else:
                QMessageBox.critical(self, "Error", f"Failed to fetch history. Status Code: {response.status_code}")

        except Exception as e:
            QMessageBox.critical(self, "Error", str(e))

    def predict_stock(self):
        selected_stock = self.stock_list.currentItem()
        if not selected_stock:
            QMessageBox.warning(self, "Warning", "Please select a stock.")
            return

        stock_name = selected_stock.text()
        range_days = self.range_combo.currentText()

        url = f"http://127.0.0.1:8090/forecast"
        data = {'stock': stock_name, 'range': range_days}

        try:
            response = requests.post(url, json=data, headers={'Authorization': f'Bearer {self.token}'})

            if response.status_code == 200:
                # Display prediction results (response should return prediction data)
                prediction_data = response.json()
                self.display_prediction(prediction_data)
            else:
                QMessageBox.critical(self, "Error", f"Prediction failed! Status Code: {response.status_code}")

        except Exception as e:
            QMessageBox.critical(self, "Error", str(e))

    def plot_graph(self, csv_data):
        # Code to plot graph using matplotlib
        pass

    def display_prediction(self, prediction_data):
        # Code to display prediction data
        pass


if __name__ == "__main__":
    app = QApplication(sys.argv)
    login_window = LoginWindow()
    login_window.show()
    sys.exit(app.exec_())
