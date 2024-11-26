FROM python:3.11-slim

WORKDIR /app

COPY ./requirements.txt /app/requirements.txt

COPY ./ ./

RUN pip3 install -r /app/requirements.txt

RUN apt-get update && apt-get install -y firefox-esr \
    && apt-get install -y wget \
    && wget https://github.com/mozilla/geckodriver/releases/download/v0.30.0/geckodriver-v0.30.0-linux64.tar.gz \
    && tar -xvzf geckodriver-v0.30.0-linux64.tar.gz \
    && mv geckodriver /usr/local/bin/

CMD fastapi run --reload