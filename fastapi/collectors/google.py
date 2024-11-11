from GoogleNews import GoogleNews
from datetime import datetime
from selenium import webdriver
from selenium.webdriver.firefox.options import Options
import asyncio
from typing import Tuple, List
from entities.data_request import SummaryDataRequest
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC


async def get_posts_from_google(data: SummaryDataRequest) -> Tuple[str, List]:
    term, start_date, end_date = data.term, data.start_date, data.end_date
    googlenews = GoogleNews(lang='pt', region='BR')
    start_date = convert_date(start_date)
    end_date = convert_date(end_date)
    googlenews.set_time_range(start_date, end_date)
    googlenews.get_news(term)

    article_content, links = await asyncio.get_running_loop().run_in_executor(
        None, fetch_article_content, googlenews.results()
    )

    return (article_content, links)


def fetch_article_content(data):
    options = Options()
    options.add_argument("--headless")

    driver = webdriver.Firefox(options=options)
    result = []
    links = []
    for item in data:
        title = item.get('title', 'Título não disponível')
        date = item.get('date', 'Data não disponível')
        link = item.get('link', '')

        if not link:
            continue
        links.append(link)
        try:
            driver.get(link)

            body_text = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.TAG_NAME, "body"))
            ).text
            result.append(
                f"Título: {title}\nData: {date}\nTexto do link: {body_text}\n")

        except Exception as e:
            print(f"Erro ao acessar {link}: {e}")
            result.append(
                f"Título: {title}\nData: {date}\nTexto do link: Não foi possível extrair o texto\n")

    driver.quit()
    return "\n".join(result), links


def convert_date(date_str):
    date = datetime.strptime(date_str, '%Y-%m-%d')
    return date.strftime('%m/%d/%Y')
