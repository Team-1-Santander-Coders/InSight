FROM python:3.11-slim

WORKDIR /app

COPY ./requirements.txt /app/requirements.txt

COPY ./ ./

RUN pip3 install -r /app/requirements.txt

CMD fastapi run --reload