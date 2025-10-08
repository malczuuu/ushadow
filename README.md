# Micro Shadow

A simple IoT device shadow application.

## Table of Contents

- [Device State Replica](#device-state-replica)
- [Running the application](#running-the-application)
- [REST API](#rest-api)
    - [`GET /api/things`](#get-apithings)
    - [`GET /api/things/{thing}`](#get-apithingsthing)
    - [`POST /api/things`](#post-apithings)
    - [`PUT /api/things/{thing}`](#put-apithingsthing)
    - [`DELETE /api/things/{thing}`](#delete-apithingsthing)
    - [`GET /api/things/{thing}/shadow`](#get-apithingsthingshadow)
    - [`POST /api/things/{thing}/shadow`](#post-apithingsthingshadow)
    - [`GET /api/things/{thing}/shadow/violations`](#get-apithingsthingshadowviolations)
- [MQTT API](#mqtt-api)
    - [Topics](#topics)
    - [Messages](#messages)

## Device State Replica

Device State Replica is an IoT communication pattern for replicating current device's state, desired
future state and the difference between current and desired states. Consists of two models, the
logical one, defined in cloud system and the physical one, represented by the device itself. Device
communicates with the cloud application, in order to adjust its state, according to given
requirements.

**Sources:**

- [https://iotatlas.net/](https://iotatlas.net/en/patterns/device_state_replica/)

## Running the application

The application requires MongoDB and RabbitMQ to be up and running.

```bash
$ docker-compose up -d
```

Consider browsing [`docker-compose.yaml`](./docker-compose.yaml) file and [`conf/`](./conf)
directory for more detail.

Run the application with `bootRun` task.

```bash
$ ./gradlew bootRun
```

Alternatively, the application can be run by building and executing the jarfile or by using most
IDEs.

Browsing `http://localhost:8080` would redirect to `/swagger-ui/index.html` for easier API access.

## REST API

### `GET /api/things`

Returns a list of things.

```text
Content-Type: application/json
```

```json
{
  "content": [
    {
      "id": "d2bfcd45-94a7-43c9-98af-87893cfc2c66",
      "name": "SmartCamera03",
      "created_date": "2021-10-01T11:32:12.671Z",
      "last_modified_date": "2021-10-01T12:31:42.239Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 23,
  "totalPages": 2
}
```

### `GET /api/things/{thing}`

Returns the details of given thing.

```text
Content-Type: application/json
```

```json
{
  "id": "d2bfcd45-94a7-43c9-98af-87893cfc2c66",
  "name": "SmartCamera03",
  "created_date": "2021-10-01T11:32:12.671Z",
  "last_modified_date": "2021-10-01T12:31:42.239Z",
  "version": 5
}
```

### `POST /api/things`

Create a new thing.

```text
Content-Type: application/json
```

```json
{
  "id": "d2bfcd45-94a7-43c9-98af-87893cfc2c66",
  "name": "SmartCamera03"
}
```

New thing is created with default `0` version and proper metadata.

```text
Content-Type: application/json
```

```json
{
  "id": "d2bfcd45-94a7-43c9-98af-87893cfc2c66",
  "name": "SmartCamera03",
  "created_date": "2021-10-01T11:32:12.671Z",
  "last_modified_date": "2021-10-01T11:32:12.671Z",
  "version": 0
}
```

### `PUT /api/things/{thing}`

Update given thing.

```text
Content-Type: application/json
```

```json
{
  "name": "SmartCamera03",
  "version": 4
}
```

Thing update bumps version to `5`.

```text
Content-Type: application/json
```

```json
{
  "id": "d2bfcd45-94a7-43c9-98af-87893cfc2c66",
  "name": "SmartCamera03",
  "created_date": "2021-10-01T11:32:12.671Z",
  "last_modified_date": "2021-10-01T12:31:42.239Z",
  "version": 5
}
```

### `DELETE /api/things/{thing}`

Deletes given thing. Doesn't return any response.

### `GET /api/things/{thing}/shadow`

Returns current setup of thing shadow along, which consists of `reported` and `desired` state.

```text
Content-Type: application/json
```

```json
{
  "desired": {
    "light_on": true,
    "resolution": "1080p",
    "upload_url": "https://10.34.231.12:8443/cams"
  },
  "reported": {
    "light_on": true,
    "resolution": "480p",
    "upload_url": "https://10.34.231.12:8443/cams"
  },
  "version": 8
}
```

### `POST /api/things/{thing}/shadow`

Updated desired state of a thing shadow.

```text
Content-Type: application/json
```

```json
{
  "desired": {
    "light_on": false,
    "resolution": "360p",
    "upload_url": "https://10.34.231.12:8443/cams"
  },
  "version": 8
}
```

The reported state is not affected by desired state change. Communication between server and thing
will eventually make states consistent.

```text
Content-Type: application/json
```

```json
{
  "desired": {
    "light_on": false,
    "resolution": "360p",
    "upload_url": "https://10.34.231.12:8443/cams"
  },
  "reported": {
    "light_on": true,
    "resolution": "1080p",
    "upload_url": "https://10.34.231.12:8443/cams"
  },
  "version": 9
}
```

### `GET /api/things/{thing}/shadow/violations`

Messages sent over MQTT (or AMQP) are being validated and quietly discarded if validation
constraints aren't met. Violations are available over REST API.

```text
Content-Type: application/json
```

```json
{
  "content": [
    {
      "action_type": "reported_update",
      "violations": [
        {
          "path": "reported.qwertyuiopoghyjrfogsdfogjdfgsldfkjgsldkfgjhsldfkghjsfdghklsfgjhslfjghslfdjgs;dfljhs;dlfkjh;sdlfkghjs;dflkgjsld;fgkjs;dflgksjdfl;ghsjdfhlkdjflghkdfgjdfghfghfghfiuytrewqwertyuioiuytrewqwertyuiopoiuytrewsdfghuytfghjuytfghjiuytfghjuygtbnjuygvbnjuygtvbnhjytgfvbhgfvbhytfvbhytrfvghuytfvbnjuyg",
          "message": "must have field name matching ^[a-zA-Z]\\w*$"
        },
        {
          "path": "reported.'qwertyuiopoghyjrfogsdfogjd...'",
          "message": "must have field name no longer than 256"
        }
      ],
      "created_date": "2021-10-10T07:46:38Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 23,
  "totalPages": 2
}
```

## MQTT API

### Topics

| topic name                       | description                                         |
| -------------------------------- | --------------------------------------------------- |
| `api/things/{id}/shadow/query`   | send a request to retrieve current thing shadow     |
| `api/things/{id}/shadow/update`  | send a request to update shadow reported state      |
| `api/things/{id}/shadow/receive` | receive twin messages and responses from the server |

### Messages

#### `TokenEnvelope`

A message published on `(...)/shadow/query` topic, to request `ShadowEnvelope` message to be sent
on `(...)/shadow/receive` topic.

```json
{
  "token": "9ocsicS1pdaldXDqLoU9"
}
```

Where:

- `"token"` is used to correlate request with response, sent asynchronously on another topic.

#### `ShadowEnvelope`

A message received on `(...)/shadow/receive` topic, with the latest shadow update.

```json
{
  "type": "shadow",
  "shadow": {
    "desired": {
      "light_on": true,
      "resolution": "1080p",
      "upload_url": "https://10.34.231.12:8443/cams"
    },
    "reported": {
      "light_on": true,
      "resolution": "1080p",
      "upload_url": "https://10.34.231.12:8443/cams"
    },
    "last_modified_date": "2021-10-01T00:23:11Z",
    "version": 31
  },
  "token": "9ocsicS1pdaldXDqLoU9"
}
```

Where:

- `"desired"` is the state requested by the user in cloud application.
- `"reported"` is the latest state reported by the device.
- `"last_modified_date"` is the latest timestamp of shadow modification (either the timestamp device
  or user modification).
- `"version"` is the version of the document for optimistic locking. **Note**, that optimistic
  locking does not apply to the MQTT API.
- `"token"` is used to correlate request with response, sent asynchronously on another topic.

#### `ReportedEnvelope`

A message published on `(...)/shadow/update` topic, to update the reported status of the device.
Afterwards, `ShadowEnvelope` message is sent as a response on `(...)/shadow/receive` topic.

```json
{
  "type": "reported",
  "reported": {
    "light_on": true,
    "resolution": "1080p",
    "upload_url": "https://10.34.231.12:8443/cams"
  },
  "token": "9ocsicS1pdaldXDqLoU9"
}
```

Where:

- `"reported"` is the latest state reported by the device.
- `"token"` is used to correlate request with response, sent asynchronously on another topic.
