from entities.data_request import SummaryDataRequest
from entities.summarized_data import SummarizedData
from collectors.bluesky import get_posts_from_bluesky
from collectors.google import get_posts_from_google
from collectors.twitter import get_posts_from_twitter
from collectors.reddit import get_posts_from_reddit
from collectors.newsapi import get_posts_from_newsapi
from entities.collected_data import CollectedData
import asyncio


async def safe_collect(collector_func, data):
    try:
        result = await collector_func(data)
        if isinstance(result, tuple):
            return result
        return result, []
    except Exception as e:
        print(f"Erro ao coletar dados de {collector_func.__name__}: {e}")
        return "", []


async def collect_data(data: SummaryDataRequest):
    bluesky_result, google_result, twitter_result, reddit_result, newsapi_result = await asyncio.gather(
        safe_collect(get_posts_from_bluesky, data),
        safe_collect(get_posts_from_google, data),
        safe_collect(get_posts_from_twitter, data),
        safe_collect(get_posts_from_reddit, data),
        safe_collect(get_posts_from_newsapi, data)
    )

    references = set()

    bluesky_data, bluesky_links = bluesky_result
    references.update(bluesky_links)

    google_data, google_links = google_result
    references.update(google_links)

    twitter_data, twitter_links = twitter_result
    references.update(twitter_links)

    reddit_data, reddit_links = reddit_result
    references.update(reddit_links or ["https://www.reddit.com"])

    newsapi_data, newsapi_links = newsapi_result
    references.update(newsapi_links)
    
    references = {link for link in references if len(link) > 10}

    return CollectedData(
        twitter_data=twitter_data or "Dados do Twitter indisponíveis",
        bluesky_data=bluesky_data or "Dados do Bluesky indisponíveis",
        google_data=google_data or "Dados do Google indisponíveis",
        reddit_data=reddit_data or "Dados do Reddit indisponíveis",
        newsapi_data=newsapi_data or "Dados do NewsAPI indisponíveis",
        references=list(references)
    )
