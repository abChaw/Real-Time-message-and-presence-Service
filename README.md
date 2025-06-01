# Real‑Time Chat & Presence Service

This is a simple full‑stack chat application:
- **Backend** (Java + Spring Boot + WebSocket/STOMP + JPA/H2)
- **Frontend** (React + SockJS + STOMP.js)

Users can join a single “room,” see who’s online, send messages in real time, and load message history. Rate‑limiting prevents spamming.

---

## Prerequisites

- Java 17+ and Maven (or use the included Maven Wrapper)
- Node.js 14+ and npm

---

## 1. Back‑End Setup (Spring Boot)

1. Open a terminal and go to the project’s root:

   ```bash
   cd messaging-service-parent
