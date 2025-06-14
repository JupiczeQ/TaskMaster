# Import bazy danych TaskMaster

## Wymagania
- XAMPP z MySQL/MariaDB
- Plik `taskmaster_db.sql`

## Instalacja XAMPP
Pobierz i zainstaluj XAMPP ze strony `https://www.apachefriends.org`. Pakiet zawiera wszystkie potrzebne komponenty, w tym serwer MySQL.

## Instrukcja

### 1. Uruchom XAMPP
- Otwórz XAMPP Control Panel
- Kliknij **Start** przy Apache i MySQL

### 2. Utwórz bazę danych
- Otwórz przeglądarkę i wejdź na: `http://localhost/phpmyadmin`
- Kliknij zakładkę **Bazy danych**
- W polu "Utwórz bazę danych" wpisz: `taskmaster`
- Kliknij **Utwórz**

### 3. Zaimportuj dane
- Kliknij na utworzoną bazę `taskmaster` (lista po lewej)
- Kliknij zakładkę **Import**
- Kliknij **Wybierz plik** i wskaż plik `taskmaster_db.sql`
- Przewiń na dół i kliknij **Wykonaj**

### 4. Utwórz użytkownika
- Kliknij zakładkę **SQL**
- Wklej poniższe polecenia:
```sql
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON taskmaster.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
```
- Kliknij **Wykonaj**

Aplikacja powinna teraz połączyć się z bazą danych używając:
- Użytkownik: `admin`
- Hasło: `admin`
- URL połączenia: `jdbc:mysql://localhost:3306/taskmaster?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC`

**WAŻNE**: Parametry w URL są konieczne dla:
- `useUnicode=true&characterEncoding=UTF-8` - poprawne wyświetlanie polskich znaków
- `serverTimezone=UTC` - synchronizacja czasu z serwerem
