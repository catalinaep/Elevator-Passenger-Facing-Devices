# Passenger Facing Devices

This application provides the passenger facing devices of the elevator:

- an **elevator panel** inside an elevator car and allows a passenger chooses a destination floor
- a **floor panel** is at a building floor and allows a passenger to request an elevator direction.

Both devices allow the passenger to view relevant information about the elevator status.

## Directions to Run

Open two terminals in the repository directory.

### 1. Start the Software Bus

```bash
mvn compile
mvn exec:java -Dexec.mainClass=SoftwareBus.SBMain "-Dexec.args=6000"
```

### 2. Start the passenger-panel application

In a second terminal:

```bash
mvn javafx:run "-Djavafx.args=127.0.0.1 6000 test-script.txt"
```

## Test-script format

`Manager` reads one command per line. Blank lines and lines beginning with `#` are ignored.

| Command | Format | Meaning |
|---|---|---|
| Subscribe | `SUBSCRIBE <topic> <subtopic>` | Subscribe to one message channel. |
| Publish | `POST <topic> <subtopic> <value>` | Send a message to the bus. |
| Retrieve | `GET <topic> <subtopic>` | Retrieve and distribute the next message for that channel. |
| Pause | `SLEEP <milliseconds>` | Wait before the next command. |

For a test message, subscribe first, publish the message, then retrieve it:

```text
SUBSCRIBE 15 1
POST 15 1 5
GET 15 1
```

This tells elevator panel 1 to show floor 5.

## Software Bus Message Format

Every message contains the following:

```text
topic: what action/event the message represents
subtopic: which elevator or floor is involved
value: the data for that action/event
```

### Elevator-panel topics (the panel inside the elevator)

For all topics starting with `CAR`, the subtopic is the elevator number. The valid values are 1-4.

| Topic | Name | Value | Message Direction            |
|---:|---|---|------------------------------|
| 10 | `CAR_BUTTON_PRESSED` | destination floor | Elevator panel to controller |
| 11 | `CAR_BUTTON_RESET` | floor button to turn off | Controller to elevator panel |
| 12 | `CAR_KEY_STATE` | `0` removed, `1` inserted | Elevator panel to controller |
| 13 | `CAR_DING` | `0` (unused) | Controller to elevator panel |
| 14 | `CAR_BUZZER` | `0` stop, `1` play | Controller to elevator panel |
| 15 | `CAR_FLOOR_DISPLAY` | floor number to display | Controller to elevator panel |
| 16 | `CAR_DIRECTION_DISPLAY` | direction code | Controller to elevator panel |

### Floor-panel topics (outside an elevator)

For all the `HALL` and `FLOOR` topics, the subtopic is the floor number. Valid values are 1-10.

| Topic | Name | Value | Direction |
|---:|---|---|---|
| 20 | `HALL_CALL` | direction code | Floor panel to controller |
| 21 | `FLOOR_DING` | `0` (unused) | Controller to floor panel |
| 22 | `HALL_REQUEST_STATE` | direction code | Controller to floor panel |
| 23 | `ELEVATOR_STATUS` | packed floor and direction | Controller to every floor panel |
| 24 | `FLOOR_ALARM_STATE` | `0` off, `1` on | Floor panel to controller |

### Direction values

| Value | Direction |
|---:|---|
| 0 | `IDLE` |
| 1 | `UP` |
| 2 | `DOWN` |

### Elevator status packing

For topic `23`, the subtopic is the elevator number. Pack its current floor and direction into `value`:

```text
value = (currentFloor << 2) | directionValue
```

For example, elevator 1 at floor 3 going up is:

```text
topic = 23
subtopic = 1
value = (3 << 2) | 1 = 13
```

The manager updates every registered floor panel with this elevator status.