# Emotion-Based Recommendation System — Backend

An AI-powered recommendation system that detects a user's facial emotion and suggests personalized movies, music, and videos based on their mood.

## Overview

This backend service captures a user's emotion from a webcam image, uses an AI emotion detection API to identify the mood, and returns curated recommendations (movies, music, videos) sourced from YouTube. It learns from user engagement over time — content the user previously selected for a given emotion is ranked higher on repeat visits.

## Tech Stack

- **Java 17**, **Spring Boot**
- **Spring Data JPA** + **MySQL**
- **Spring Security** + **JWT** (authentication & authorization)
- **OAuth2** (Google Sign-In)
- **Face++ API** (facial emotion detection)
- **YouTube Data API v3** (content recommendations)
- **WebClient / WebFlux** (external API calls)

## Features

- Facial emotion detection from webcam images (Happy, Sad, Angry, Fear, Surprise, Neutral)
- Personalized recommendations (movies, videos, music) based on detected emotion
- Engagement-based ranking — previously selected content ranks higher on repeat emotions
- User authentication: email/password registration & login, and Google OAuth login
- JWT-secured REST APIs
- Emotion history and recommendation history tracking

## Prerequisites

- Java 17+
- Maven
- MySQL 8+
- API keys: [Face++](https://console.faceplusplus.com/), [YouTube Data API](https://console.cloud.google.com/), Google OAuth2 credentials

## Environment Variables

This project reads secrets from environment variables (never hardcoded). Set the following before running:

| Variable | Description |
|---|---|
| `DB_URL` | MySQL JDBC URL |
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| `DRIVER_CLASS` | `com.mysql.cj.jdbc.Driver` |
| `EMOTION_URL` | Face++ detect API URL |
| `EMOTION_KEY` | Face++ API secret |
| `EMOTION_API_KEY` | Face++ API key |
| `YOUTUBE_URL` | YouTube Data API search endpoint |
| `YOUTUBE_API` | YouTube Data API key |
| `CLIENT_ID` | Google OAuth2 client ID |
| `CLIENT_SECRET` | Google OAuth2 client secret |
| `JWT_SECRET` | Secret key for signing JWT tokens |

## Running Locally

```bash
git clone https://github.com/amit9058807381/emotion-recommender-backend.git
cd emotion-recommender-backend
# Set environment variables (see table above)
./mvnw spring-boot:run
```

Server runs on `http://localhost:8081` by default.

## Key API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT |
| GET | `/oauth2/authorization/google` | Google OAuth login |
| POST | `/api/emotion/detect` | Detect emotion from a base64 image |
| GET | `/api/emotion/history/{userId}` | Get a user's emotion history |
| GET | `/api/recommendations` | Get ranked recommendations for an emotion + type |
| POST | `/api/recommendations/select` | Record that a user engaged with a recommendation |

## Project Structure

- `controller/` — REST controllers
- `service/` — Business logic
- `repository/` — Spring Data JPA repositories
- `entity/` — JPA entities
- `security/` — JWT, OAuth2, Spring Security config
- `helper/` — DTOs / request-response objects

## Author

Amit — College Project (Statement of Purpose: Emotion-Based Personalized Recommendation System)