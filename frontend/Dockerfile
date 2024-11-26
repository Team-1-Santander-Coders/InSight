FROM node:22.6

WORKDIR /app

COPY . .

RUN npm install -g bun

RUN bun install

EXPOSE 3000

CMD ["bun", "dev"]
