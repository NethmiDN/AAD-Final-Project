# BarkBuddy - Final Project

## Project Description

A full-stack web application to help people find, adopt, and care for dogs. It includes user authentication (email/password + Google OAuth2), a Lost & Found flow, owner-to-user messaging, product listings with orders, email-based password resets, and an AI assistant powered by a local Ollama model.

## What's inside

- Backend: Spring Boot 3 (Java 21), MySQL, JPA/Hibernate, Spring Security + JWT, OAuth2 (Google), Mail (SMTP), logging
- Frontend: Static HTML/CSS/JS (Bootstrap 5, Font Awesome); runs well with VS Code Live Server
- AI: Local chatbot via Ollama (configurable model)
- Media: Image uploads via ImgBB API

Repository structure:

- `BarkBuddy_BackEnd/` — Java Spring Boot service and API
- `BarkBuddy_FrontEnd/` — static site with pages for login, signup, dashboard, adoption, listings, messaging, admin, etc.

## Screenshots

Below are a few key screens from the application (more pages are available under `BarkBuddy_FrontEnd/pages/`).

![Home Page](BarkBuddy_FrontEnd/assets/images/Screenshot%202025-09-18%20124619.png)
Home page with quick entry points to Adopt, Listings, Lost & Found, and Chatbot.

![Discover/Listing Preview](BarkBuddy_FrontEnd/assets/images/Screenshot%202025-09-18%20130717.png)
Discover and listings preview showcasing adoptable friends and products.

![Onboarding Form – Signup](BarkBuddy_FrontEnd/assets/images/Screenshot%202025-09-18%20130840.png)
Signup form with validation before accessing the dashboard.

## Prerequisites

- Java 21 (JDK)
- Maven (wrapper included: `mvnw`/`mvnw.cmd`)
- MySQL 8.x (or compatible)
- VS Code (recommended) + Live Server extension (or any static file server)
- Optional for AI: Ollama installed and a model pulled (e.g., `gemma3:1b`)

## Setup Instructions

Follow these steps to install, configure, and run both the backend and the frontend locally.

### 1) Configure backend

Create a properties file based on the provided sample and ensure secrets are NOT committed:

- Copy `BarkBuddy_BackEnd/src/main/resources/application-local.properties.example` to `BarkBuddy_BackEnd/src/main/resources/application-local.properties`
- Update the placeholders to your environment
- Spring Boot can also read environment variables (e.g., `SPRING_DATASOURCE_URL`, `JWT_SECRET`)

Key properties to review:

- Database: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
- JWT: `jwt.secret`, `jwt.expiration`
- Server: `server.port` (default 8080)
- OAuth2 (Google): client id/secret + redirect URI: `http://localhost:8080/login/oauth2/code/google`
- Mail (SMTP): `spring.mail.username`, `spring.mail.password` (use an app password)
- AI: `ollama.api.url` (default `http://localhost:11434/api/generate`), `ollama.model` (e.g., `gemma3:1b`)

Note: The current `application.properties` in the repo contains concrete values used during development. Replace these with your own secure configuration for your environment.

### 2) Start backend

From the `BarkBuddy_BackEnd/` folder in Windows PowerShell:

```powershell
.\mvnw.cmd -q -DskipTests spring-boot:run
# or build a jar
.\mvnw.cmd -q -DskipTests clean package
java -jar .\target\BarkBuddy_BackEnd-0.0.1-SNAPSHOT.jar
```

Backend runs on `http://localhost:8080` by default and writes logs to `BarkBuddy_BackEnd/logs/application.log`.

### 3) Start frontend

Open the folder `BarkBuddy_FrontEnd/` in VS Code and use the Live Server extension to "Open with Live Server".

- The app expects the frontend at `http://localhost:5500` (or `http://127.0.0.1:5500`), which matches Live Server’s default. CORS in the backend allows this origin for most APIs.
- The homepage is `index.html`; feature pages are under `pages/` (e.g., `login.html`, `dashboard.html`).

### 4) Optional — Run Ollama (AI chatbot)

- Install Ollama and pull the model you configured (defaults are in `application.properties`):

```powershell
# Example (adjust model if needed):
ollama pull gemma3:1b
ollama run gemma3:1b
```

- The chatbot widget (floating button) calls the backend at `POST /api/chat`.

## Core features

- Authentication
  - Register: `POST /auth/barkbuddy/register`
  - Login: `POST /auth/barkbuddy/login` → returns JWT + user info
  - Forgot password: `POST /auth/barkbuddy/forgot-password` (sends OTP)
  - Verify OTP + reset: `POST /auth/barkbuddy/verify-otp`
  - Google OAuth2 login: redirect to Google, then backend redirects to frontend `pages/oauth-callback.html` with token

- Dogs
  - List my dogs: `GET /api/dogs` (JWT)
  - Create dog: `POST /api/dogs/saveDog` (multipart: `dog` JSON + `image`, JWT)
  - Update dog: `PUT /api/dogs/{id}` (multipart, JWT)
  - Delete dog: `DELETE /api/dogs/{id}` (JWT)
  - Public list: `GET /api/dogs/all`

- Lost & Found
  - Report missing: `POST /api/lostdog/report` (params: `dogId`, `lastSeenLocation`, JWT)
  - Get missing dogs: `GET /api/lostdog/missing`
  - Create sighting: `POST /api/lostdog/sighting/{lostDogId}` (body: `{ location }`, JWT)
  - List sightings: `GET /api/lostdog/{lostDogId}/sightings`

- Listings & Orders
  - Listings CRUD: `/api/listings` (GET, POST multipart, PUT multipart `/api/listings/{id}`, DELETE `/api/listings/{id}`)
  - Purchase: `POST /api/listings/{id}/purchase?quantity=1`
  - Create paid order (user): `POST /api/orders/pay/{listingId}?quantity=1` (JWT)
  - My orders: `GET /api/orders/me` (JWT)

- Messaging
  - List conversations: `GET /api/user-chat/conversations` (JWT)
  - Conversation: `GET /api/user-chat/conversation/{dogId}/{otherUserId}` (JWT)
  - Send text: `POST /api/user-chat/send` (JSON, JWT)
  - Send image: `POST /api/user-chat/send-image` (multipart, JWT)

- Admin endpoints (require `ROLE_ADMIN`)
  - List admins: `GET /api/admin/admins`
  - Add admin: `POST /api/admin/admins`
  - Delete admin: `DELETE /api/admin/admins/{adminId}`
  - Stats: `GET /api/admin/stats`

- AI chatbot
  - `POST /api/chat` with JSON `{ "message": "..." }` → `{ "response": "..." }`

## Frontend pages

- `index.html` — marketing home with entry points (Adopt, Lost & Found, Listings, Chatbot)
- `pages/login.html`, `pages/signup.html`, `pages/resetPassword.html` (OTP), `pages/oauth-callback.html`
- `pages/dashboard.html`, `pages/mydog.html`, `pages/messages.html`
- `pages/adoptFriend.html`, `pages/lostfound.html`, `pages/listings.html`
- `pages/adminDashboard.html`, `pages/adminListing.html`, `pages/adminSetting.html`
- `pages/chatbot.html`, `pages/404.html`

The floating chatbot widget is initialized by `assets/js/chatbot-widget.js` and works across pages.

## Google OAuth2 setup (overview)

- Create OAuth Client in Google Cloud Console (type: Web application)
- Authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
- Frontend callback page: `BarkBuddy_FrontEnd/pages/oauth-callback.html` (backend redirects to `http://127.0.0.1:5500/BarkBuddy_FrontEnd/pages/oauth-callback.html` with query params)
- Set `spring.security.oauth2.client.registration.google.client-id` and `...client-secret`

## Mail setup (password reset)

- Use a provider like Gmail with an App Password
- Set `spring.mail.username` and `spring.mail.password`

## Image uploads

- Images are uploaded to ImgBB. Set an API key in the service if you change providers.

## Troubleshooting

- CORS errors: ensure you’re serving the frontend from `http://localhost:5500` (Live Server). The backend `@CrossOrigin` settings allow this origin for key controllers.
- 401/403 errors: include the `Authorization: Bearer <token>` header after login; token is stored in `localStorage` by the frontend.
- DB connection: verify MySQL is running and credentials/URL are correct. The default URL can create the DB: `.../barkbuddy?createDatabaseIfNotExist=true`.
- Ollama errors: make sure Ollama is running, the model is pulled, and `ollama.api.url`/`ollama.model` match your setup.
- Gmail SMTP: use an App Password; regular account passwords will be blocked.
- Port conflicts: change `server.port` or your static server port to avoid clashes.

## Useful test calls (optional)

```http
POST http://localhost:8080/auth/barkbuddy/register
Content-Type: application/json

{
  "username": "alice",
  "email": "alice@example.com",
  "password": "Secret123!"
}
```

```http
POST http://localhost:8080/auth/barkbuddy/login
Content-Type: application/json

{
  "email": "alice@example.com",
  "password": "Secret123!"
}
```

```http
POST http://localhost:8080/api/chat
Content-Type: application/json

{ "message": "How do I prepare to adopt a dog?" }
```

## Tech stack

- Spring Boot 3.5, Java 21, Maven
- Spring Security (JWT), OAuth2 Client (Google)
- Spring Data JPA/Hibernate, MySQL
- Java Mail (SMTP)
- Ollama (local LLM)
- Bootstrap 5, Font Awesome, Vanilla JS

## Demo Video

Watch the walkthrough on YouTube:

https://youtu.be/UgG_LaobCcs

Naming convention: "AAD-Final-Project - BarkBuddy Demo".

## Notes

- Do not commit real secrets to source control. Use environment variables or a separate, ignored properties file.
- Logs are written under `BarkBuddy_BackEnd/logs/` (configurable via `logging.file.name`).

---

Made with love for the Final Project.
