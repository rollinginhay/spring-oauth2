# Spring OAuth2 Authentication

A Spring application with API for authentication.

## Features

* Single-provider (Google) authentication
* Username and password authentication with password encryption
* JWT authorization
* Multi-role authorization
* Springdoc API documentation
* Docker support for db and app

## Setup

* A Google Cloud Platform project with Google People API enabled

* Configure GCP project:
    * Application type: Web application
    * Authorized Javascript origins: `http://localhost:8080`
    * Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
      and `http://localhost:8080/oauth2/authorize/google`

* Configure `application.yml`:
    ```properties
    oauth2:
      client:
        registration:
          google:
            client-id: your-gcp-client-id
            client-secret: your-gcp-client-secret

    jwt:
      secret: secret-at-least-32-characrters-long
      expiration: in-miliseconds
  ```

## Run

Run the app with Docker:

```bash
docker-compose up --build
```

Access Swagger UI at: `localhost:8080/swagger-ui.html`
To access JWT-protected API, access `localhost:8080`, perform login using Google
Oauth2, copy your token from the response, return to
`localhost:8080/swagger-ui.html` and authorize your request on the UI. 
