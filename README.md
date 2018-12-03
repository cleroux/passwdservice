# Passwd as a Service
Minimal HTTP Web Service application that provides read access to the passwd and group files on a UNIX-like system.
This project is intended as a demonstration only. It is not intended for deployment.

## Requirements

* Java 7+  
* Maven


## To build and run locally using Jetty

```bash
mvn jetty:run
```

Runs the service on the local machine listening on port 8080.

## To run all tests

```bash
mvn verify
```


## To build a war file for deployment in other servlet containers

```bash
mvn package
```

Produces target/passwdservice-1.0.0.war


## Runtime options

Specify the relative or absolute paths to the passwd and/or group files using the following options:  
-DpasswdFile=/path/to/passwd  
-DgroupFile=/path/to/group

Eg.:
```bash
mvn -DpasswdFile=src/test/resources/passwd -DgroupFile=src/test/resources/group jetty:run
```

All of the [Jetty Container options](https://www.eclipse.org/jetty/documentation/9.4.x/jetty-maven-plugin.html) are also available.
eg.:
```bash
mvn -Djetty.http.port=80 jetty:run
```

## API Endpoints

* [Users](#users) : `GET /users`
* [Users Query](#usersquery) : `GET /users/query`
* [User ID](#userid) : `GET /users/{uid}`
* [User Groups](#usergroups) : `GET /users/{uid}/groups`
* [Groups](#groups) : `GET /groups`
* [Groups Query](#groupsquery) : `GET /groups/query`
* [Group ID](#groupid) : `GET /groups/{gid}`

<hr>

### <a name="users">Users</a>

Return a list of all users on the system, as defined in the /etc/passwd file.

**URL** : `/users`

**Method** : `GET`

#### Success Response

**Code** : `200 OK`

**Content** :

```json
[
  {"name": "root", "uid": 0, "gid": 0, "comment": "root", "home": "/root", "shell": "/bin/bash"},
  {"name": "cleroux", "uid": 1000, "gid": 1000, "comment": "", "home": "/home/cleroux", "shell": "/bin/bash"}
]
```

#### Error Response

**Condition** : If passwd file is absent or malformed.

**Code** : `500 Internal Server Error`

<hr>

### <a name="usersquery">Users Query</a>

Return a list of users matching all of the specified query fields. Supports only exact matches.

**URL** : `/users/query`

**Method** : `GET`

**URL Parameters** :

  `name=[string]` (Optional) : User name  
  `uid=[integer]` (Optional) : User ID  
  `gid=[integer]` (Optional) : Group ID  
  `comment=[string]` (Optional) : Comment  
  `home=[string]` (Optional) : Home directory  
  `shell=[string]` (Optional) : Shell executable  

#### Success Response

**Code** : `200 OK`

**Content** :

```json
[
  {"name": "root", "uid": 0, "gid": 0, "comment": "root", "home": "/root", "shell": "/bin/bash"},
  {"name": "cleroux", "uid": 1000, "gid": 1000, "comment": "", "home": "/home/cleroux", "shell": "/bin/bash"}
]
```

#### Error Response

**Condition** : If passwd file is absent or malformed.

**Code** : `500 Internal Server Error`

<hr>

### <a name="userid">User ID</a>

Return a single user with the specified ID.

**URL** : `/users/{uid}`

**Method** : `GET`

#### Success Response

**Code** : `200 OK`

**Content** :

```json
{"name": "cleroux", "uid": 1000, "gid": 1000, "comment": "cleroux,,,", "home": "/home/cleroux", "shell": "/bin/bash"}
```

#### Error Response

**Condition** : If user ID does not exist.

**Code** : `404 Not Found`

OR

**Condition** : If passwd file is absent or malformed.

**Code** : `500 Internal Server Error`

<hr>

### <a name="usergroups">User Groups</a>

Return all the groups for a given user as defined in the group file.
Note that this does not include the user's primary group.

**URL** : `/users/{uid}/groups`

**Method** : `GET`

#### Success Response

**Code** : `200 OK`

**Content** :

```json
[
  {"name": "sudo", "gid": 27, "members": ["cleroux"]}
]
```

#### Error Response

**Condition** : If user ID does not exist.

**Code** : `404 Not Found`

OR

**Condition** : If passwd or group files are absent or malformed.

**Code** : `500 Internal Server Error`

<hr>

### <a name="groups">Groups</a>

Return a list of all groups on the system, as defined by /etc/group.

**URL** : `/groups`

**Method** : `GET`

#### Success Response

**Code** : `200 OK`

**Content** :

```json
[
  {"name":"root", "gid":0, "members":[]},
  {"name":"sudo", "gid":27, "members":["cleroux"]},
  {"name":"cleroux", "gid":1000, "members":[]}
]
```

#### Error Response

**Condition** : If group file is absent or malformed.

**Code** : `500 Internal Server Error`

### <a name="groupsquery">Groups Query</a>

Return a list of groups matching all of the specified query fields. Any group
containing all the specified members will be returned.

**URL** : `/groups/query`

**Method** : `GET`

**URL Parameters** :

  `name=[string]` (Optional) : Group name  
  `gid=[integer]` (Optional) : Group ID  
  `member=[string]` (Optional, Repeatable) : User name  

#### Success Response

**Code** : `200 OK`

**Content** :

```json
[
  {"name":"cleroux", "gid":1000, "members":[]}
]
```

#### Error Response

**Condition** : If the group file is absent or malformed.

**Code** : `500 Internal Server Error`

<hr>

### <a name="groupid">Group ID</a>

Return a single group with the specified ID.

**URL** : `/groups/{gid}`

**Method** : `GET`

#### Success Response

**Code** : `200 OK`

**Content** :

```json
{"name":"sudo", "gid":27, "members":["cleroux"]}
```
