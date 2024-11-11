import httpx
from entities.data_request import SummaryDataRequest

async def get_posts_from_bluesky(data: SummaryDataRequest) -> str:
    term, start_date, end_date = data.term, data.start_date, data.end_date
    base_url = 'https://public.api.bsky.app/xrpc/app.bsky.feed.searchPosts?q='
    search_term = f'{term}&lang=pt'
    since_term = f'&since={start_date}T00:00:00Z'
    until_term = f'&until={end_date}T23:59:59Z'
    url = base_url + search_term + since_term + until_term
    
    async with httpx.AsyncClient() as client:
        response = await client.get(url)
    
    if response.status_code == 200:
        return (aggregate_results(response.json()), url)
    else:
        return ""

def aggregate_results(results: dict) -> str:
    texts = [
        post['record']['text']
        for post in results.get('posts', [])
        if 'record' in post and 'text' in post['record']
    ]
    return '\n'.join(texts)
