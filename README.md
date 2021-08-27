# User Management

[![CI](https://github.com/l-ray/user-mgmnt/actions/workflows/maven.yml/badge.svg)](https://github.com/l-ray/user-mgmnt/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/l-ray/user-mgmnt/branch/master/graph/badge.svg?token=L4RY9LLNF4)](https://codecov.io/gh/l-ray/user-mgmnt)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=l-ray_user-mgmnt&metric=alert_status)](https://sonarcloud.io/dashboard?id=l-ray_user-mgmnt)

This is an exercise on how to create a user-registration possibility. 
As the frontend interface isn't developed (yet), this exercise follows the standard of SCIM 2.0 to allow for compatibility with existing products/service, e.g. Okta. 
It is implemented in Jakarta EE 9 (using Wildfly as application server).

### Requirements
- Java JDK 11 _(e.g. OpenJDK 11.0.12)_
- Apache Maven _(e.g. Maven 3.6.3)_

## Getting started
For the [TL;DR](https://www.urbandictionary.com/define.php?term=tl%3Bdr) - approach, 

- execute within the project directory to build and deploy to an in-promptu Wildfly server
  ```shell
  mvn clean wildfly:run -Pwildfly \
      -Dwildfly.artifactId=wildfly-preview-dist \
      -Dwildfly.version=24.0.1.Final
  ```
- open in your browser: [http://localhost:8080/user/](http://localhost:8080/user/) for a Swagger-UI Endpoint interface

### Optional
Test-drive the service using Okta.
- create user-war ```mvn package``` and deploy to an outside-available Wildfly instance
- use an [Okta developer account](https://developer.okta.com) to connect an application "SCIM 2.0 Test App (Header Auth)"
- ignore the SAML authentication steps, select "Provisioning" -> "Enable API integration"
- set "Base API" to ```http://<AddYourIpHere>:8080/user/api/scim/v2/```, leave "API Token" empty
- enable "Provisioning to App" the following topics and save 
  - "Create Users"
  - "Update User Attributes"
  - "Deactivate Users"
  - "Sync Password"
- import the demo user into Okta

## Non-functional technical requirements
- backend-system without any UI functionality (except a [swagger-UI](https://swagger.io/tools/swagger-ui/) endpoint)
- services provided as RESTful web service
- payloads are in JSON format (SCIM v2 format, where possible)
- data storage is in-memory database of the application server (JBoss wildfly) only 
- no SSL or authentication included (yet)

⚠ **This is not production-ready code**, please do not necessarily use it as that.

## Possibly useful services
_(implemented functional business requirements/services are marked accordingly with a checkmark ✓)_

- ✓ **list all users** - provide a list and reduced details for all users in the system. Current solution includes paging.
- ✓ **filter for users** - current solution includes search by ```username``` and ```lastModifiedAfter```.
- ✓ **receive data from a single user**
- ✓ **add new user**
- ✓ **modify existing user** - current solution, following existing SCIM implementations, differs between credential changes (done as http patch) and user details (done as http put)
- ✓ **mark user as deactivated** - keeps the user data, but disallows e.g. logging in as the user.   
- ✓ **provide available provider configuration/services** - keeps the user data, but disallows e.g. logging in as the user.
- ✓ **provide Open-API definition** - to allow fast integration of the service.
- 🗒 **self-service password reset** - in case of a forgotten password, a new one can be created and sent to the user based on something the user knows/owns/can inherence.
- 🗒 **throttling user creation/modification** - this reduces digital vandalism.
- 🗒 **confirm e-mail/phone data** - by sending confirmation e-mail/text-message to user.
- 🗒 **allow mass/batch CRUD** - for import of pre-existing user-data, or mass-deactivation in case of organisational changes 

## Technical decisions
This part explains some fundamental technical decisions about the exercise.
### Data Model
The user entity is split into three actual tables, connected by a one-to-one mapping. 

The main [user-entity](https://github.com/l-ray/user-mgmnt/blob/master/src/main/java/de/lray/service/admin/user/persistence/entities/User.java) holds user-specific settings like timezone and locale specification. 
A [credential-entity](https://github.com/l-ray/user-mgmnt/blob/master/src/main/java/de/lray/service/admin/user/persistence/entities/Credentials.java), used during the login-process, holds a foreign key to the user-entity. By this, during the login process no direct access to the concrete user-entity is needed.
The user-entity holds a foreign key to the [contact-entity](https://github.com/l-ray/user-mgmnt/blob/master/src/main/java/de/lray/service/admin/user/persistence/entities/Contact.java). By separating user and contact, potential policies of persisting system data and personal data separately are (hopefully) met. 


### Endpoints
The endpoint design is in alignment with [Okta's SCIM implementation](https://developer.okta.com/docs/reference/scim/scim-20/). Creating an Oka Dev account and testing towards this service is expected to be successful.

#### Changing user data
For changing existing user-data, two http methods are used:
- **put** - change user-specific details, but changes to active or password are ignored.
- **patch** - changing single-valued fields. This only works for ```active``` and ```password``` at the moment.

#### Filter for user data
The following minimal filters are supported as ```GET``` parameter, they can not be concatenated.
- ```filter=userName eq "test.user@email.local"```
- ```filter=lastModified gt "2021-11-11T04:42:34Z"```
