from entities.collected_data import CollectedData
from entities.summarized_data import SummarizedData
from entities.data_request import GenerateAudioRequest
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver import Firefox
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from dotenv import load_dotenv
import httpx
import json
import time
import os

prompt = """**Prompt**:
"Analise e filtre as informações mais relevantes deste material para criar um resumo em formato de newsletter, que cubra os pontos principais e destaque dados essenciais e insights críticos sobre o tema. A resposta deve estar em JSON válido, conforme o exemplo a seguir:

```json
{
    "summary": "Texto principal do resumo, com foco em informações valiosas e essenciais.",
    "description": "Descrição breve que sintetiza o conteúdo e objetivo do resumo.",
    "categorias": "categoria1, categoria2, categoria3..."
}
```

**Importante**:
- Garanta que o JSON seja válido e evite aspas duplas dentro dos campos de string. 
- Retorne o texto no formato JSON sem caracteres indesejados para manter a integridade do JSON.
- Não insira as referências dentro do prompt, por favor
"""

personalizacao_podcast = """Crie um episódio do podcast 'InSight' somente em português do Brasil sobre [tema do material]. Na introdução, citem o nome do podcast e slogan ‘Descubra novas perspectivas para transformar seus pensamentos’ com entusiasmo. Todos os diálogos devem ser apenas em português do Brasil. Mantenham um tom acolhedor e acessível, explicando conceitos de forma simples e com exemplos. Finalizem agradecendo aos ouvintes reforçando que 'InSight' é uma oportunidade para novas ideias, com mensagem inspiradora"""

os.environ.pop("GOOGLE_ACCOUNT", None)
os.environ.pop("GOOGLE_PASSWORD", None)
os.environ.pop("ACCESS_UNSPLASH", None)
load_dotenv(override=True)

GOOGLE_ACCOUNT = os.getenv("GOOGLE_ACCOUNT")
GOOGLE_PASSWORD = os.getenv("GOOGLE_PASSWORD")
ACCESS_UNSPLASH = os.getenv("ACCESS_UNSPLASH")


def login_notebook_driver(driver: Firefox):
    driver.get("https://notebooklm.google.com/")
    login_input = driver.find_element(By.NAME, "identifier")
    login_input.send_keys(GOOGLE_ACCOUNT)
    login_input.send_keys(Keys.ENTER)
    password_input = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.NAME, "Passwd"))
    )
    password_input.send_keys(GOOGLE_PASSWORD)
    password_input.send_keys(Keys.ENTER)


async def get_summary(term: str, data: CollectedData, driver: Firefox) -> SummarizedData:
    driver.get("https://notebooklm.google.com/")
    create_new_notebook_button = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.CSS_SELECTOR, ".create-new-button"))
    )
    create_new_notebook_button.click()

    texto_concatenado = tratar_collected(data)
    print(len(texto_concatenado))
    caminho_abs_file = os.path.abspath("./temp/temporary_file.txt")
    try:
        botao_upload = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located(
                (By.CSS_SELECTOR, ".dropzone__icon"))
        )
    except:
        try:
            botao_upload = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located(
                    (By.CLASS_NAME, "dropzone__icon mdc-icon-button mat-mdc-icon-button gmat-mdc-button-with-prefix mat-unthemed mat-mdc-button-base gmat-mdc-button ng-star-inserted"))
            )
        except:
            botao_upload = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located(
                    By.CSS_SELECTOR, '#mat-mdc-dialog-5 > div > div > upload-dialog > div > div.content > upload-main-screen > div.dropzone.ng-star-inserted > button'
                )
            )
    botao_upload.click()

    enviar_arquivo_input = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located(
            (By.XPATH, "/html/body/div[6]/div[2]/div/mat-dialog-container/div/div/upload-dialog/div/div[2]/upload-main-screen/div[1]/input"))
    )
    enviar_arquivo_input.send_keys(caminho_abs_file)

    time.sleep(1.5)

    WebDriverWait(driver, 45).until_not(
        EC.presence_of_element_located((By.TAG_NAME, "circle"))
    )

    personalizar_button = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable(
            (By.XPATH,
             "/html/body/labs-tailwind-root/div/notebook/div/div[2]/div/div/div[2]/chat-layout/div/omnibar/div[1]/notebook-guide/div/div[2]/div[1]/div[2]/div[2]/audio-overview/div/div/div[2]/div[1]/button",
             ))
    )

    personalizar_button.click()
    time.sleep(2)
    campo_personalizacao = driver.find_element(
        By.XPATH, '/html/body/div[6]/div[2]/div/mat-dialog-container/div/div/producer-audio-dialog/div/mat-dialog-content/div/form/mat-form-field/div[1]/div/div[2]/textarea')
    campo_personalizacao.send_keys(personalizacao_podcast)

    produce_container = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.CLASS_NAME, "producer-container"))
    )

    time.sleep(2)
    personalizacao_confirmar_button = produce_container.find_element(
        By.CLASS_NAME, "generate-button")
    personalizacao_confirmar_button.click()
    time.sleep(5)

    campo_prompt = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located(
            (By.XPATH,
             "/html/body/labs-tailwind-root/div/notebook/div/div[2]/div/div/div[2]/chat-layout/div/omnibar/div[2]/div[1]/div[2]/query-box/div/div/form/textarea",
             ))
    )
    campo_prompt.send_keys(prompt)
    campo_prompt.send_keys(Keys.ENTER)

    time.sleep(2)

    WebDriverWait(driver, 60).until_not(
        EC.presence_of_element_located((By.TAG_NAME, "loading-component"))
    )

    try:
        message = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located(
                (By.CLASS_NAME,
                 "mat-mdc-card-content message-content to-user-message-inner-content")
            )
        )
    except Exception as e:
        message = driver.find_element(
            By.CLASS_NAME, "to-user-message-inner-content")

    id_notebook = driver.current_url.replace(
        "https://notebooklm.google.com/notebook/",
        "",
    )

    code_text = message.text
    conteudo_dict = json.loads(code_text)

    summary = conteudo_dict.get("summary", "")
    categories = conteudo_dict.get("categorias", "")
    description = conteudo_dict.get("description", "")

    input_name_notebook = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.CLASS_NAME, "title-input"))
    )
    input_name_notebook.click()
    input_name_notebook.clear()
    input_name_notebook.send_keys(id_notebook)
    time.sleep(1)
    input_name_notebook.send_keys(Keys.ENTER)

    query_unsplash = f"{term}+{categories}".replace(" ", "+")
    url_unsplash = f"https://api.unsplash.com/search/photos?page=1&query={query_unsplash}&client_id={ACCESS_UNSPLASH}&orientation=squarish&per_page=1"

    async with httpx.AsyncClient() as client:
        response = await client.get(url_unsplash)
        unsplash_data = response.json()

    small_s3_url = get_image_small_s3_url(unsplash_data)
    if not small_s3_url:
        small_s3_url = "imagem não encontrada"

    time.sleep(2)

    turn_audio_public(driver=driver)

    return SummarizedData(
        id=id_notebook,
        summary=summary,
        categories=categories,
        description=description,
        image_url=small_s3_url,
        audio_url=f"https://notebooklm.google.com/notebook/{id_notebook}/audio",
        references=data.references
    )


def tratar_collected(data: CollectedData):
    texto_concatenado = f"Twitter:\n {data.twitter_data}\n Bluesky:\n {data.bluesky_data}\nGoogle:\n {data.google_data}\nReddit:\n {data.reddit_data}\nNewsAPI:\n {data.newsapi_data}"

    os.makedirs("./temp", exist_ok=True)

    with open("./temp/temporary_file.txt", "w", encoding="utf-8") as file:
        file.write(texto_concatenado)

    return texto_concatenado


def get_image_small_s3_url(data: dict) -> str | None:
    if data.get("results"):
        first_image = data["results"][0]
        small_s3_url = first_image["urls"].get("small_s3")
        return small_s3_url
    return None


def turn_audio_public(driver: Firefox) -> bool:
    try:
        guia_notebook_btn = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located(
                (By.CLASS_NAME, "notebook-guide-button"))
        )
    except:
        guia_notebook_btn = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located(
                (By.CLASS_NAME, "header-container header-container-mobile"))
        )

    guia_notebook_btn.click()

    time.sleep(2)

    WebDriverWait(driver, 300).until_not(
        EC.presence_of_element_located((By.TAG_NAME, "circle"))
    )

    try:
        compartilhar_btn = WebDriverWait(driver, 60).until(
            EC.presence_of_element_located(
                (By.XPATH,
                 "/html/body/labs-tailwind-root/div/notebook/div/div[2]/div/div/div[2]/chat-layout/div/omnibar/div[1]/notebook-guide/div/div[2]/div[1]/div[2]/div[2]/audio-overview/div/div/div/audio-player/div/div[1]/div[2]/button[3]")
        ))

        compartilhar_btn.click()
        try:
            try:
                change_to_public_btn = WebDriverWait(driver, 5).until(
                    EC.presence_of_element_located(
                        (By.CLASS_NAME, 'mdc-switch mdc-switch--unselected'))
                )
            
            except:
                try:
                    change_to_public_btn = WebDriverWait(driver, 5).until(
                        EC.presence_of_element_located(
                            (By.ID, 'mat-mdc-slide-toggle-0-button'))
                    )
                except:
                    try:
                        change_to_public_btn = WebDriverWait(driver, 5).until(
                            EC.presence_of_element_located(
                                (By.CSS_SELECTOR, '[aria-checked="false"]'))
                        )
                    except:
                        change_to_public_btn = WebDriverWait(driver, 5).until(
                            EC.presence_of_element_located(
                                (By.CSS_SELECTOR, '[aria-labelledby="mat-mdc-slide-toggle-0-label"]'))
                        )
                
            change_to_public_btn.click()
        except:
            return False

        return True

    except Exception as e:
        print(e)
        return False
