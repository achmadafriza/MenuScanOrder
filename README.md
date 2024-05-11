# Menu Scan Order

### Dependencies

- Java 21
- Gradle 8.7 or higher

## Running locally

1. Configure local MySQL server.
2. Adjust `./src/main/resources/application.properties` to use the local MySQL server.
3. Run `./gradlew bootRun`

Or, you can use the alternative `docker compose up`

## Deploying to UQCloud

1. Create a path for the deployment on `/var/www/app/` and `/var/www/html/img`.
2. Add `./deployment/menuscanorder.conf` as the enabled sites/framework in Nginx.

`menuscanorder.conf` uses a reverse proxy to the default deployed java app.

Afterwards, it has configurations to serve static content on `/var/www/html/img` at 
`{baseUrl}/img`.

3. Compile the fat Java JAR using `./gradlew clean bootJar`.
4. Adjust `./deployment/application.properties` to point to the MySQL database and the base url for serving static content. 
5. Copy the compiled JAR and `./deployment/application.properties` into `/var/www/app`

See `./deploy.sh` for the commands.

6. Add `./deployment/menuscanorder.service` as a systemd service.
7. Run the following commands:

```bash
sudo systemctl daemon-reload
sudo systemctl start menuscanorder
sudo systemctl enable menuscanorder

sudo systemctl reload nginx
```

8. You can see the logs for the deployed app using `journalctl`.

```bash
sudo journalctl -u menuscanorder # For complete log
sudo journalctl -u menuscanorder -f # To follow the current log
```
