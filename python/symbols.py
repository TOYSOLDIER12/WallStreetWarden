import requests
from bs4 import BeautifulSoup

def get_wikipedia_stock_symbols():
    url = 'https://en.wikipedia.org/wiki/List_of_S%26P_500_companies'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')
    table = soup.find('table', {'id': 'constituents'})
    symbols = {}
    for row in table.find_all('tr')[1:]:
        columns = row.find_all('td')
        if columns:
            symbol = columns[0].text.strip()
            name = columns[1].text.strip()
            symbols[name] = symbol
    return symbols

symbols = get_wikipedia_stock_symbols()

for name, symbol in symbols.items():
    print(f"{name}: {symbol}")
