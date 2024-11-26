from selenium import webdriver
from selenium.webdriver.firefox.options import Options

def setup_driver():
    options = Options()
    options.headless = True
    options.binary_location = "/usr/bin/firefox"
    geckodriver_path = "/usr/local/bin/geckodriver"
    driver = webdriver.Firefox(
        executable_path=geckodriver_path, options=options)
    return driver