# Pong Game

A classic Pong game implementation in Java with Swing, featuring proper game loop architecture, collision detection, and score management.

## 📋 Description

This is a two-player Pong game built as part of a programming course at Feevale University. The game implements a proper game loop with fixed timestep physics, separation of concerns through design patterns, and a clean object-oriented architecture.

## ✨ Features

- **Two-player local gameplay** - Play against a friend on the same keyboard
- **Proper game loop** - Implements fixed timestep for consistent physics (60 UPS)
- **Collision detection** - Physics-based collision system with Observer pattern
- **Score tracking** - First player to 5 points wins
- **Custom retro font** - Press Start 2P font for authentic retro feel
- **Clean architecture** - Separation of rendering, physics, input, and game logic

## 🎮 Controls

### Player 1 (Left Paddle)

- **W** - Move up
- **S** - Move down

### Player 2 (Right Paddle)

- **↑ (Up Arrow)** - Move up
- **↓ (Down Arrow)** - Move down

## 🏗️ Architecture

The project follows object-oriented design principles with the following structure:

### Core Components

- `Main.java` - Entry point and font configuration
- `GameFrame.java` - Main game window
- `GamePanel.java` - Game loop and rendering
- `GameState.java` - Game state management (PLAYING, PAUSED, GAME_OVER, STOPPED)
- `ScoreManager.java` - Score tracking and win condition management

### Entities

- `Entity.java` - Base class for game objects
- `GameObject.java` - Abstract game object with rendering
- `Paddle.java` - Player paddle implementation
- `Ball.java` - Ball physics and movement
- `FieldLine.java` - Center line decoration

### Input System

- `KeyboardHandler.java` - Keyboard input handler
- `InputObserver.java` - Observer pattern for input events

### Physics System

- `CollisionDetector.java` - Collision detection between game objects
- `CollisionEvent.java` - Collision event data
- `CollisionObserver.java` - Observer pattern for collision events

### Renderers

- `BallRenderer.java` - Ball rendering
- `PaddleRenderer.java` - Paddle rendering
- `FieldLineRenderer.java` - Field line rendering
- `ScoreRenderer.java` - Score display rendering
- `GameOverRenderer.java` - Game over screen rendering

## 🛠️ Technologies

- **Java 17** - Programming language
- **Swing** - GUI framework
- **Maven** - Build tool and dependency management

## 📦 Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher

## 🚀 How to Run

### Using Maven

1. Clone or download the project
2. Navigate to the project directory:

   ```bash
   cd /path/to/game1
   ```

3. Compile the project:

   ```bash
   mvn clean compile
   ```

4. Run the game:

   ```bash
   mvn exec:java -Dexec.mainClass="br.com.vinidiefen.pong.Main"
   ```

### Building JAR

To create an executable JAR file:

```bash
mvn clean package
java -jar target/pong-1.0-SNAPSHOT.jar
```

## 🎯 Game Rules

1. Two players control paddles on opposite sides of the screen
2. The ball bounces between paddles and top/bottom walls
3. If a player misses the ball, the opponent scores a point
4. First player to reach **5 points** wins the game
5. After game over, the winner is displayed on the screen

## 🔧 Design Patterns Used

- **Observer Pattern** - For input handling and collision detection
- **Strategy Pattern** - For rendering different game objects
- **State Pattern** - For game state management
- **Singleton Pattern** - For score management

## 📁 Project Structure

```text
game1/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── br/com/vinidiefen/pong/
    │   │       ├── Main.java
    │   │       ├── GameFrame.java
    │   │       ├── GamePanel.java
    │   │       ├── GameState.java
    │   │       ├── ScoreManager.java
    │   │       ├── entities/
    │   │       ├── input/
    │   │       ├── physics/
    │   │       └── renderers/
    │   └── resources/
    │       └── fonts/
    │           └── PressStart2P.ttf
    └── test/
        └── java/
```

## 👨‍💻 Author

### Vinicius Diefenbach

- Course: Programming II
- Institution: Feevale University

## 📝 License

This project is developed for educational purposes as part of a university course.

## 🐛 Known Issues

None at this time. Feel free to report any bugs or issues.

---

**Enjoy the game!** 🎮
