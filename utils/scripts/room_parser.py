import requests
import time
from bs4 import BeautifulSoup
from selenium import webdriver

BASE_URL = 'https://plan.epfl.ch/'

'''
    Credits to OpenStreetMap for the data

    This function uses the map of EPFL's website to retrieve rooms of each building.
    For now it's only useful to retrieve the type of room (either 'Room' or 'Auditorium' so far).

    In the future, I will try to make it retrieve geolocation info as well as better infos about the type of room,
    when I figure out how to scrape data from online maps.
'''
def retrieve_rooms(building):
    url = BASE_URL + '?q=' + building
    rooms = set()
    driver = webdriver.Chrome(executable_path='/usr/bin/chromedriver') # change with your own chrome driver path, or any other browser supported by selenium
    driver.get(url)
    # I need to force a wait for the browser driver to render javascript, I don't know how to make it any faster for now
    time.sleep(3)
    page_source = driver.execute_script("return document.getElementsByTagName('html')[0].innerHTML")
    doc = BeautifulSoup(page_source, 'html.parser')

    for line in doc.find_all('div', {'class': 'gmf-search-datum ng-scope tt-suggestion tt-selectable'}):
        id = line.find('p', {'class': 'gmf-search-label'}).text
        id = id.removeprefix('Auditoire ')
        type = line.find('p', {'class': 'gmf-search-group'}).text.removesuffix('s')
        if type == 'Room' or type == 'Auditorium':
            rooms.add(id + ',' + type)
    return rooms

def retrieve_buildings():
    url = BASE_URL + 'buildings.html'
    buildings = []
    result = requests.get(url)

    if result.ok:
        doc = BeautifulSoup(result.text, 'html.parser')
        for building in doc.find_all('li'):
            buildings.append(building.text)

    return buildings

def main():
    buildings = retrieve_buildings()
    rooms = set()
    for building in buildings:
        rooms.update(retrieve_rooms(building))
    
    with open('rooms.csv', 'w') as csv_file:
        csv_file.write('rooms' + '\n')
        csv_file.write('id,type' + '\n')
        csv_file.write('string,string' + '\n')
        for room in rooms:
            csv_file.write(room + '\n')
    csv_file.close()


if __name__ == "__main__":
    main()