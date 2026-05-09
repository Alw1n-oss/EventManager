# EventManager

Desktop-приложение для управления локальными мероприятиями. Курсовой проект.

## Описание

Информационная система «EventManager» предназначена для автоматизации процессов планирования, анонсирования и учёта посещаемости локальных некоммерческих мероприятий. Система поддерживает три роли пользователей: Администратор, Организатор и Участник.

## Стек технологий

- Java 11+
- JavaFX (FXML, Scene Builder)
- JPA / Hibernate
- MySQL 8.0
- Maven
- JUnit 5

## Требования

- JDK 11 или новее
- Apache Maven 3.8+
- MySQL Server 8.0+
- JavaFX SDK (если не входит в JDK)

## Установка и запуск

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/Alw1n-oss/EventManager.git
   cd EventManager
2. Создать базу данных event_manager в MySQL:
   sql
   Copy
   CREATE DATABASE event_manager;
3. Настроить подключение в src/main/resources/META-INF/persistence.xml (указать логин/пароль от MySQL).
4. Собрать и запустить:
   bash
   Copy
   mvn clean compile
   mvn javafx:run
