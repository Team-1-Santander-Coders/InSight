from datetime import datetime
from typing import List, Dict
from entities.data_request import SummaryDataRequest
import os
import praw
from dotenv import load_dotenv
import asyncio
from concurrent.futures import ThreadPoolExecutor

os.environ.pop('CLIENT_ID_REDDIT', None)
os.environ.pop('CLIENT_SECRET_REDDIT', None)
os.environ.pop('USER_AGENT_REDDIT', None)
load_dotenv(override=True)

CLIENT_ID_REDDIT = os.getenv('CLIENT_ID_REDDIT')
CLIENT_SECRET_REDDIT = os.getenv('CLIENT_SECRET_REDDIT')
USER_AGENT_REDDIT = os.getenv('USER_AGENT_REDDIT')

reddit = praw.Reddit(
    client_id=CLIENT_ID_REDDIT,
    client_secret=CLIENT_SECRET_REDDIT,
    user_agent=USER_AGENT_REDDIT
)

async def get_posts_from_reddit(data: SummaryDataRequest) -> str:
    term, start_date, end_date = data.term, data.start_date, data.end_date
    
    with ThreadPoolExecutor() as executor:
        return await asyncio.get_running_loop().run_in_executor(
            executor, lambda: buscar_posts_reddit(term, start_date, end_date)
        )

def buscar_posts_reddit(term: str, start_date: str, end_date: str) -> str:
    try:
        subreddit = reddit.subreddit('all')
        posts = []
        periodo_busca = definir_periodo_de_busca(start_date, end_date)

        for post in subreddit.search(query=term, sort="relevance", time_filter=periodo_busca, limit=50):
            post.comment_sort = 'best'
            post.comments.replace_more(limit=0)

            comentarios = extrair_comentarios(post.comments[:5])
            posts.append({
                "titulo": post.title,
                "autor": post.author.name if post.author else "N/A",
                "texto": post.selftext,
                "criado_em": datetime.fromtimestamp(post.created_utc).strftime('%Y-%m-%d %H:%M:%S'),
                "comentarios": comentarios
            })
        return filtrar_e_formatar_posts(posts, start_date, end_date)

    except Exception as e:
        print(f"Erro ao buscar posts: {e}")
        return "Erro ao buscar posts."

def extrair_comentarios(comentarios_reddit) -> List[Dict]:
    comentarios = []
    for comment in comentarios_reddit:
        comentarios.append({
            "autor": comment.author.name if comment.author else "N/A",
            "texto": comment.body
        })
    return comentarios

def filtrar_e_formatar_posts(posts: List[dict], data_inicial: str, data_final: str) -> str:
    data_inicial_dt = datetime.strptime(data_inicial, "%Y-%m-%d")
    data_final_dt = datetime.strptime(data_final, "%Y-%m-%d").replace(hour=23, minute=59, second=59)
    conteudo_formatado = []
    
    for post in posts:
        post_data = datetime.strptime(post['criado_em'], "%Y-%m-%d %H:%M:%S")
        
        if data_inicial_dt <= post_data <= data_final_dt:
            conteudo_formatado.append(f"Título do post: {post['titulo']}\nTexto do post: {post.get('texto', '')}")
            conteudo_formatado.append(f"Data: {post['criado_em']}")
            
            for comentario in post.get('comentarios', []):
                conteudo_formatado.append(f"Comentário do post acima: {comentario['texto']}")
    
    return "\n".join(conteudo_formatado)

def calcular_diferenca_dias(start_date: str, end_date: str) -> int:
    date_format = "%Y-%m-%d"
    
    start = datetime.strptime(start_date, date_format)
    end = datetime.strptime(end_date, date_format)
    
    diferenca = end - start
    
    return diferenca.days

def definir_periodo_de_busca(start_date: str, end_date: str) -> str:
    days = calcular_diferenca_dias(start_date, end_date)
    if days > 7:
        return "month"
    else:
        return "week"
