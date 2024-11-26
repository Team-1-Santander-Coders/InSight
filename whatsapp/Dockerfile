FROM node:latest

WORKDIR /app

COPY . .

RUN npm install -g bun

RUN bun install

EXPOSE 3000

CMD ["bun", "dev"]
