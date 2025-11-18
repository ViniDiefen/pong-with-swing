# Pong Game

A classic Pong game implementation in Java with Swing, featuring proper game loop architecture, collision detection, and score management. Built as part of Programming II course at Feevale University.

**Enjoy the game!**

## 🎮 Controls

**Player 1 (left):** W/S | **Player 2 (right):** ↑/↓

## 🚀 How to Run

**Requirements:** JDK 17+, Maven 3.6+, and Docker

### 1. Start the Database

```bash
docker-compose up -d
```

This will start a PostgreSQL database container on port 5432 for game state persistence.

### 2. Run the Game

```bash
# Compile and run
mvn clean compile
mvn exec:java -Dexec.mainClass="br.com.vinidiefen.pong.Main"
```

**Save/Load:** The game includes save/load functionality that persists game state to the PostgreSQL database.

**Database Configuration:** You can customize the database connection using environment variables:

- `POSTGRES_URL` - Database URL (default: `jdbc:postgresql://localhost:5432/pws`)
- `POSTGRES_USER` - Database user (default: `docker`)
- `POSTGRES_PASSWORD` - Database password (default: `docker`)

## 🐛 Known Issues

None at this time. Feel free to report any bugs or issues.
