import os
from dotenv import load_dotenv
from bs4 import BeautifulSoup
import httpx
from entities.data_request import SummaryDataRequest

os.environ.pop('APIKEY_NEWS', None)

load_dotenv(override=True)
APIKEY_NEWS = os.getenv('APIKEY_NEWS')


async def get_posts_from_newsapi(data: SummaryDataRequest) -> tuple:
    term, start_date, end_date = data.term, data.start_date, data.end_date
    url = (
        'https://newsapi.org/v2/everything?'
        f'q={term}&'
        f'from={start_date}&'
        f'to={end_date}&'
        'sortBy=popularity&'
        'language=pt&'
        f'apiKey={APIKEY_NEWS}'
    )

    async with httpx.AsyncClient() as client:
        response = await client.get(url)
        response.raise_for_status()
        data = response.json().get('articles', [])

    return await aggregate_results(data)


async def aggregate_results(data: list) -> tuple:
    result = ""
    links = []
    for article in data:
        source = article['source']['name']
        title = article['title']
        url = article['url']

        article_content = await fetch_article_text(url)
        result += f"{source} - {title}\n\n{article_content}\n\n"

        links.append(url)

    return result, links


async def fetch_article_text(url: str) -> str:
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get(url)
            response.raise_for_status()
            soup = BeautifulSoup(response.content, 'html.parser')
            paragraphs = soup.find_all(['p', 'span'])
            article_text = '\n'.join(el.get_text()
                                     for el in paragraphs if el.get_text())
            return article_text if article_text else "Texto completo não disponível"
    except Exception as e:
        return "Erro ao obter texto completo"
