from bs4 import BeautifulSoup
import requests

SERVER_BASE_URL = "https://edu.epfl.ch/studyplan/en/{cycle}/{section}"

IDS = set()

def get_courses(c, s):
    url = SERVER_BASE_URL.format(cycle=c, section=s)
    result = requests.get(url)
    courses = []

    if result.ok:
        doc = BeautifulSoup(result.text, "html.parser")

        for line in doc.find_all('div', {'class': 'line'}):
            course = line.find('div', {'class': 'cours'})
            info = course.find(
                'div', {'class': 'cours-info'}).text.split(' / ')
            id = info[0]
            if not id in IDS:
                IDS.add(id)

                section = info[1].removeprefix('Section ')
                title = course.find(
                    'div', {'class', 'cours-name'}).text.split(' (')[0]
                cycle = c
                credits = line.find('div', {'data-title': 'Credits'}).text
                exam = line.find('div', {'data-title': 'Exam'}).text
                session = ''
                grading = ''
                if exam:
                    exam = exam.split(' session')
                    session = exam[0]
                    if (len(exam) > 1):
                        grading = exam[1]
                teacher = line.find('div', {'class': 'enseignement-name'}).text
                language = line.find('div', {'data-title': 'Language'}).text

                course = [id, title, cycle, section, credits, session, grading, teacher, language]
                for i in range(len(course)):
                    if ',' in course[i]:
                        course[i] = '"' + course[i] + '"'

                courses.append(course)

    return courses

def get_sections(c):
    url = SERVER_BASE_URL.format(cycle=c, section='')
    sections = set()
    result = requests.get(url)

    if (result.ok):
        doc = BeautifulSoup(result.text, "html.parser")

        for ref in doc.find_all('a'):
            link = ref.get('href')
            if link.startswith('/studyplan/en') and not link.endswith(c + '/'):
                sections.add(link.removeprefix('/studyplan/en/' + c + '/'))

    return sections

def main():
    cycles = ['propedeutics', 'bachelor', 'master', 'doctoral_school', 'minor']
    header = ['id', 'title', 'cycle', 'section', 'credits',
              'session', 'grading', 'teacher', 'language']

    with open('course_list.csv', 'w') as file:
        file.write(','.join(header) + '\n')
        for cycle in cycles:
            sections = get_sections(cycle)
            for section in sections:
                courses = get_courses(cycle, section)
                for course in courses:
                    file.write(','.join(course) + '\n')
    file.close()

if __name__ == "__main__":
    main()