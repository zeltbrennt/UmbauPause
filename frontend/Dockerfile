FROM node:20

ARG BACKEND
ENV VITE_BACKEND_URL=$BACKEND

WORKDIR /webshop/frontend
COPY ./package.json .
RUN npm install
RUN npm i -g serve
COPY . .
RUN npm run build
EXPOSE 3000

CMD ["serve", "-s", "dist"]