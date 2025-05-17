# CamelUpb – Multiplayer Camel Up Board Game

CamelUpb is a digital recreation of the *Camel Up* board game featuring **multiplayer gameplay** over raw TCP sockets, built with a custom protocol and handcrafted networking stack. Designed with a clean **Onion Architecture**, the project ensures clear separation of concerns and maintainability.

## 🛠 Tech Stack

* **Backend**: Java with **Spring Boot**
* **Networking**: **Raw TCP sockets** with a **custom packets protocol**
* **Client**: **JavaFX GUI**
* **Architecture**: **Onion Architecture**
* **I/O Handling**: Fully implemented from scratch with IO streams and additional techniques likes heartbeat and broadcasting channels etc.

## 💡 Features

* 🧠 Handcrafted **packet handler** and binary protocol for communication
* 🎮 Smooth **multiplayer** experience with turn-based logic
* 👁️ Rich **JavaFX GUI** to visualize gameplay
* 🧅 **Onion Architecture** for clean layering: Domain, Application, Infrastructure, Presentation
* ⚙️ Robust **I/O system** handling percistancy and consistancy, disconnections, game state storage

## 🚀 Running the Game

1. Start the Spring Boot server.
2. Launch one or more JavaFX clients.
3. Connect clients using host and port info.
4. Enjoy Camel Up multiplayer gameplay!

## 📜 License

MIT – Free to use and modify.
