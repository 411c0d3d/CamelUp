# CamelUpb – Networked Multiplayer Camel Up Board Game

CamelUpb is a digital implementation of the *Camel Up* board game with **real-time multiplayer support** over **raw TCP sockets**. The system is built on a **custom binary protocol** and a fully handcrafted networking stack, with a strong focus on deterministic state handling, explicit protocol design, and architectural separation via **Onion Architecture**.

The project emphasizes correctness, maintainability, and transparent data flow across network boundaries.

---

## Architecture Overview

The application follows a strict **Onion Architecture**, enforcing clear dependency direction and isolation of concerns:

- **Domain**
  - Core game rules, entities, and invariants  
  - Pure business logic, no framework or I/O dependencies

- **Application**
  - Use cases and turn orchestration  
  - Validation, game flow coordination, and command handling

- **Infrastructure**
  - Raw TCP socket management  
  - Binary protocol encoding/decoding  
  - Persistence, connection lifecycle, and fault handling

- **Presentation**
  - JavaFX-based client  
  - Rendering, user interaction, and client-side state projection

---

## Technology Stack

- **Server**: Java, Spring Boot  
- **Client**: JavaFX  
- **Networking**: Raw TCP sockets with a custom binary packet protocol  
- **Concurrency**: Explicit thread management for connections and broadcasts  
- **Persistence**: Server-side game state storage with consistency guarantees  

---

## Networking and Protocol Design

- Custom **binary packet format** with:
  - Explicit headers
  - Payload length definitions
  - Message and command identifiers
- Dedicated **packet handler pipeline**:
  - Decode → validate → dispatch
- Server-controlled **broadcast channels** for game state propagation
- **Heartbeat mechanism** for detecting dropped or stale connections
- Graceful handling of:
  - Client disconnects
  - Reconnects
  - Partial network failures
- Deterministic state synchronization to ensure all clients observe identical game progression

---

## Gameplay Characteristics

- Fully server-authoritative, turn-based multiplayer logic
- Client actions validated server-side to prevent state divergence
- Deterministic game state updates broadcast to all connected clients
- Robust handling of invalid, duplicate, or out-of-order packets

---

## Running the System

1. Start the Spring Boot server.
2. Launch one or more JavaFX clients.
3. Connect clients using host and port configuration.
4. Create or join a game session and play.

---

## License

MIT License — free to use, modify, and extend.
