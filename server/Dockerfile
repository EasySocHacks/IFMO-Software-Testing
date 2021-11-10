FROM node:10.19.0

WORKDIR /server

ENV NODE_ENV=production

COPY package.json ./package.json

RUN npm install --production

COPY . .

CMD npm start