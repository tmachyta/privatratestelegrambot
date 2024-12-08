# 🏦🏦privatratestelegrambot🏦🏦

# Project Description:
Welcome to PrivatRatesTelegramBot, your go-to Telegram assistant for real-time currency exchange rates! This bot provides users with quick and accurate updates on currency rates fetched directly from PrivatBank, ensuring you're always informed and up-to-date.

Features:
👋 Warm Welcome: Greets users with a friendly message and helpful instructions.
💱 Currency Rates: Fetches and displays the latest exchange rates for major currencies, including USD, EUR, and more.
🛠️ Commands:
/start: Get a warm welcome and learn how to use the bot.
/rate: View the current exchange rates.
/help: Receive guidance on how to use the bot effectively.
/archiveHelp: Learn how to access archived exchange rate data (if applicable).
📈 Real-Time Data: Ensures accuracy by fetching live updates directly from trusted sources.

## Setup

To set up the project, follow these steps:

### Prerequisites

Make sure you have the following software installed on your system:

- Java Development Kit (JDK) 17 or higher
- Spring Boot 2.2 or higher
- Apache Maven
- Apache Tomcat vesion 9 or higher
- DataBase: MySQL
- docker

### Installation
- First of all, you should made your fork
- Second, clink on Code<> and clone link, after that open your Intellij Idea, click on Get from VCS
- past link, which you clone later

### Replace Placeholders:
To connect to your DB, you should replace PlaceHolders in .env
- Open package resources and open file env in your project.
- Locate the placeholders that need to be replaced.
- These placeholders might include values such as
- MYSQL_USER= YOUR_USERNAME -> replace with your MySQL_DB
- MYSQL_PASSWORD=YOUR_PASSWORD -> replace with your password to your MySQL_DB
- MYSQL_LOCAL_PORT=YOUR_LOCAL_PORT -> replace with your local port
- MYSQL_DOCKER_PORT=YOUR_DOCKER_PORT -> replace with your docker port
- SPRING_LOCAL_PORT=YOUR_SPRING_LOCAL_PORT -> replace with your spring local port
- SPRING_DOCKER_PORT=YOUR_DOCKER_PORT -> replace with your docker port
- DEBUG_PORT=5006
