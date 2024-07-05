FROM node:20

WORKDIR /webshop/frontend
COPY ./package.json .
RUN npm install
COPY . .
EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host"]