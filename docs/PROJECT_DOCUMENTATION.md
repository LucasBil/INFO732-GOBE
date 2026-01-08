# Project Documentation: INFO732-GOBE

## 1. Project Overview
This project is a web application for exchanging goods and services (e.g., student marketplace), likely between university students.
It is built using a **Spring Boot** backend and a **React (Vite)** frontend.

### Tech Stack
- **Backend**: Java, Spring Boot, Spring Data JPA, Hibernate.
- **Frontend**: JavaScript, React, Vite, React Router, TailwindCSS (inferred from `ClassName` usage in `App.jsx`).
- **Database**: SQL Database (controlled via JPA Entities).

---

## 2. Project Structure

The repository is divided into two main components:
- `GOBE/`: Backend application (Maven/Gradle Project).
- `frontend/`: Frontend application (Node/Vite Project).

### Backend Structure (`GOBE/src/main/java/polytech/idu/`)
- **`controllers/`**: REST API endpoints.
- **`models/`**: JPA Entities representing the database schema.
- **`repositories/`**: Data access interfaces (Spring Data JPA).
- **`services/`**: Business logic layer (e.g., `TimeSlotService`, `MessageService`).
- **`config/`**: Configuration classes (e.g., `DataInitializer`).

### Frontend Structure (`frontend/src/`)
- **`pages/`**: React components representing full pages (e.g., `Home`, `Marketplace`).
- **`components/`**: Reusable UI components (e.g., `Navbar`).
- **`context/`**: React Context (e.g., `AuthContext`).
- **`services/`**: API integration/Axios setups.
- **`assets/`**: Static assets.

---

## 3. Database Schema

The database consists of the following entities:

### `Profile`
User accounts.
- `id` (PK)
- `firstname`, `lastname`, `email`, `birthdate`
- `university` (FK to **University**)
- `preference` (List<String>)
- `followedKeywords` (List<String>) - Used for notification observers.

### `Advertisement` (Abstract)
Base class for listings.
- `id` (PK)
- `title`, `description`, `price`
- `date` (Creation date), `expire` (Expiration date)
- `holder` (FK to **Profile**)
- `place` (FK to **University**)
- `guarantee`, `type` ("GOOD" or "SERVICE")
- **Subclasses**:
    - `Good`: Has `status` (AVAILABLE, RESERVED, etc.) and `state`.
    - `Service`: Has specific service attributes.

### `TimeSlot`
Bookable slots involved in a transaction or request.
- `id` (PK)
- `date`, `amount`
- `status` (PENDING, WAITING_PAYMENT, AVAILABLE, CONFIRMED, REJECTED)
- `profile` (FK to **Profile** - Requester)
- `advertisement` (FK to **Advertisement**)

### `Transaction`
Records of payments/exchanges.
- `id` (PK)
- `date`, `currency`
- `sender` (FK to **Profile**)
- `timeslot` (FK to **TimeSlot**)

### `Message`
Chat messages between users.
- `id` (PK)
- `content`, `timestamp`
- `sender` (FK to **Profile**)
- `receiver` (FK to **Profile**)

### `Notification`
User notifications.
- `id` (PK)
- `message`, `is_read`, `created_at`
- `user` (FK to **Profile**)

### `University`
- `id` (PK)
- `name`, `city`

---

## 4. API Routes

### Advertisement Controller (`/api/advertisements`)
- `GET /` : Get all available advertisements (filters out non-available GOODS).
- `POST /` : Create a new advertisement (Good or Service).
- `GET /{id}` : Get details of a specific advertisement.

### Profile Controller (`/api/profiles`)
- `GET /{id}` : Get profile details.
- `POST /` : Create a new profile (Register).
- `POST /login` : Login with email (returns profile).
- `GET /{id}/advertisements` : Get advertisements created by a user.
- `GET /{id}/timeslots` : Get timeslots associated with a user (as requester).
- `GET /{id}/reservations` : Get incoming bookings/reservations for the user's ads.
- `POST /{id}/keywords` : Add a followed keyword for notifications.
- `DELETE /{id}/keywords` : Remove a followed keyword.

### TimeSlot Controller (`/api/timeslots`)
- `GET /` : Get all timeslots.
- `GET /advertisement/{adId}` : Get timeslots for a specific ad.
- `POST /` : Request a single timeslot (booking request).
- `PUT /{id}/status` : Update timeslot status (e.g., ACCEPT/REFUSE request).
- `POST /batch-create` : Create multiple available timeslots for an ad.
- `POST /batch-book` : Book multiple timeslots at once.

### Message Controller (`/api/messages`)
- `POST /` : Send a message.
- `GET /{userId}/{otherId}` : Get conversation history between two users.
- `GET /{userId}/conversations` : Get list of users the current user has chatted with.

### Notification Controller (`/api/notifications`)
- `GET /{userId}` : Get all notifications for a user.
- `PUT /{id}/read` : Mark a notification as read.
- `POST /` : Create a notification (Internal/Admin use).

### University Controller (`/api/universities`)
- `GET /` : Get list of all universities.

---

## 5. Frontend Routes (React Router)

The application defines the following routes in `App.jsx`:

- `/` -> **Home**: Landing page.
- `/marketplace` -> **Marketplace**: Browse advertisements.
- `/ad/:id` -> **AdDetail**: View full details of an ad.
- `/post-ad` -> **PostAd**: Create a new advertisement.
- `/profile` -> **Profile**: User dashboard (My Ads, Bookings, Requests).
- `/login` -> **Login**: Authentication page.
- `/register` -> **Register**: Sign up page.
- `/messages` -> **Messages**: Chat interface.
