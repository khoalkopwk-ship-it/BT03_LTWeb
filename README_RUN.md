# BTWeb_JPA - Run guide

## 1. Requirements

- JDK 17+
- Maven 3.9+
- Tomcat 11.x
- SQL Server / SQL Server Express
- Gmail App Password if OTP email is required

## 2. Database

Run `DB_BT03.sql` in SSMS. The application uses database `ClothingShop`.

Default JDBC URL in `persistence.xml`:

`jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=ClothingShop;encrypt=true;trustServerCertificate=true;`

Override it with environment variable `DB_URL` when your SQL Server instance/port is different.

## 3. Required environment variables

PowerShell example for the current terminal session:

```powershell
$env:DB_PASSWORD="YOUR_SQL_SERVER_PASSWORD"
$env:DB_USER="sa"
$env:SMTP_EMAIL="your_gmail@gmail.com"
$env:SMTP_APP_PASSWORD="YOUR_GMAIL_APP_PASSWORD"
```

Optional JVM properties are also supported:

- `-Ddb.url=...`
- `-Ddb.user=...`
- `-Ddb.password=...`
- `-Dsmtp.email=...`
- `-Dsmtp.appPassword=...`

Do not commit real passwords into `persistence.xml` or Java source.

## 4. Build

```text
mvn clean package
```

Expected WAR:

`target/BTWeb_JPA-1.0-SNAPSHOT.war`

## 5. Run with Tomcat

Deploy the WAR to Tomcat 11 `webapps/` or use SmartTomcat in IntelliJ. Ensure the same environment variables are available to the Tomcat process.

Typical URLs:

- `/home`
- `/product`
- `/login`
- `/register`
- `/admin/category`
- `/admin/product`

The seeded admin account is `admin / 123456` on a fresh database. On the first successful login, the legacy plain-text seed password is automatically migrated to PBKDF2.

## 6. OTP mail

Registration and forgot-password require `SMTP_EMAIL` and `SMTP_APP_PASSWORD`. Use a Gmail App Password, not the normal Gmail password.

OTP expires after 10 minutes and is limited to 5 failed attempts per flow/session.
