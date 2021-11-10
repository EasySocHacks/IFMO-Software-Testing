FROM node:10.19.0

WORKDIR /client

ENV NODE_ENV=production

COPY package.json ./package.json
COPY package-lock.json ./package-lock.json

RUN npm install --production

COPY . .

CMD npm start