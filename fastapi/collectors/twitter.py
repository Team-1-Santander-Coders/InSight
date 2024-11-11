from dotenv import load_dotenv
import os
from entities.data_request import SummaryDataRequest
from selenium.webdriver import Firefox
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from factories.driver import setup_driver
import asyncio
import time

os.environ.pop('USER_TWITTER', None)
os.environ.pop('EMAIL_TWITTER', None)
os.environ.pop('PASSWORD_TWITTER', None)
load_dotenv(override=True)

USER_TWITTER = os.getenv('USER_TWITTER')
EMAIL_TWITTER = os.getenv('EMAIL_TWITTER')
PASSWORD_TWITTER = os.getenv('PASSWORD_TWITTER')

def login(driver: Firefox, user: str, email: str, password: str):
    login = driver.find_element(By.CSS_SELECTOR, ".r-30o5oe")
    login.send_keys(email)
    time.sleep(0.2)
    login.send_keys(Keys.ENTER)
    time.sleep(2)

    try:
        password_input = driver.find_element(By.NAME, "password")
        password_input.send_keys(password)
        password_input.send_keys(Keys.ENTER)
        time.sleep(0.8)
    except:
        login = driver.find_element(By.CSS_SELECTOR, ".r-30o5oe")
        login.send_keys(user)
        time.sleep(0.2)
        login.send_keys(Keys.ENTER)
        time.sleep(2)
        password_input = driver.find_element(By.NAME, "password")
        password_input.send_keys(password)
        time.sleep(0.3)
        password_input.send_keys(Keys.ENTER)

def get_search_twitter(driver: Firefox, term: str, start_date, end_date):
    base_url = 'https://x.com/search?q='
    search_term = f'{term.replace(" ", "+")}+lang%3Apt+'
    initial_date = f'until%3A{end_date}+'
    last_date = f'since%3A{start_date}&src=typed_query&f=live'
    url = base_url + search_term + initial_date + last_date
    driver.get(url)
    time.sleep(5)

    try:
        section = driver.find_element(By.XPATH, "/html/body/div[1]/div/div/div[2]/main/div/div/div/div/div/div[3]/section")
        texts = section.find_elements(By.XPATH, ".//span")

        text_list = [text.text for text in texts if text.text.strip()]
        
        return ("\n".join(text_list), url)
    except:
        return ""

async def get_posts_from_twitter(data: SummaryDataRequest):
    driver = setup_driver()
    driver.get('https://x.com/i/flow/login')
    await asyncio.sleep(6)

    await asyncio.get_running_loop().run_in_executor(
        None, login, driver, USER_TWITTER, EMAIL_TWITTER, PASSWORD_TWITTER
    )
    await asyncio.sleep(2)

    results = await asyncio.get_running_loop().run_in_executor(
        None, get_search_twitter, driver, data.term, data.start_date, data.end_date
    )

    driver.quit()
    return results
