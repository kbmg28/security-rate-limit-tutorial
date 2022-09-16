# security-rate-limit-tutorial

A complete implementation of Recaptcha v3 and Bucket4j

## Run Locally with docker

* run the comand: `docker compose up --build -d`
* front application: `http://localhost:4000`
* back documentation: `http://localhost:8080/swagger-ui.html`

## Run by terminal

* Front

  ```
  cd recaptcha-v3-with-library
  npm i
  npm start 
  
  #access application on http://localhost:4200
  ```
  
* Back

  ```
  cd back-rate-limit
  ./gradlew bootrun

  #access api documentation on http://localhost:8080/swagger-ui.html
  ```


