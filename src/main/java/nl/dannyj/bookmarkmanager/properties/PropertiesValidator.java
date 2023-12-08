/*
 A modern, archiving bookmark manager.
 Copyright (C) 2023  Danny Jelsma

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nl.dannyj.bookmarkmanager.properties;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PropertiesValidator {

    private final ApplicationContext applicationContext;

    private final ApplicationProperties applicationProperties;

    private static final String DEFAULT_TOKEN = "CHANGEME";

    @Autowired
    public PropertiesValidator(ApplicationContext applicationContext, ApplicationProperties applicationProperties) {
        this.applicationContext = applicationContext;
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void validate() {
        String jwtSecret = applicationProperties.getSecurity().getJwtSecret();

        if (jwtSecret.equals(DEFAULT_TOKEN)) {
            log.error("The default JWT secret is still in use. Please change it in application.yml. Shutting down...");
            SpringApplication.exit(applicationContext, () -> 1);
        }
    }
}
