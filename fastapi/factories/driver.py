from selenium import webdriver
from selenium.webdriver.firefox.options import Options

def setup_driver():
    options = Options()
    options.headless = True
    driver = webdriver.Firefox(options=options)
    return driver