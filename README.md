# Description
An assignment done for some job interview and slightly refactored since then.

It reads the input file containing log entries stored in JSON format (*JsonLogRepository* class).
Then it processes them calculating duration for the matched logs (*DefaultLogHandle* class) and
sets alert if applicable (*FlagEventStrategy.LONG_EVENT* class).
Finally the processed logs are stored in the database (*JdbcEventRepository* class).

It features object-oriented design, clean code, hexagonal architecture and prioritize the Single Responsibility Principle.

# Pre-requirements:
* Java 8+
* Gradle build system

# Usage
gradlew run --args="INPUT_FILE [DATABASE_NAME]"

## Examples:
* gradlew run --args="samplelogfile.txt"
* gradlew run --args="samplelogfile.txt sampledb"

# Design Considerations
* The use of jdbc batch support has improved the program execution time.
  My performance tests have showed the decrease of execution time (for my sample data)
  from 83 seconds to 52 seconds. 
* The dedicated thread for processing and storing the parsed logs has improved the execution
  time by 10 seconds compared to single-thread solution (in hardware setup with external 
  USB hard drive on which the input file was located).
  The thread support is implemented in the *ThreadedLogHandle* class utilizing
  the Decorator design pattern.
* Detecting long events is implemented in dedicated strategy class (*FlagEventStrategy.LONG_EVENT*) to
  make it easily findable.
* To deserialize json the GSON library is used (*JsonLogAdapter* class).
* I had some issues with database (not persisting inserted rows). It turned out
  that it has to be properly shutdown (some connection url parameter was needed and
  closing all jdbc connections);
* To support logging the OpsSupport class is used. I like this solution, because
  all the logging stuff is in a known single (*OpsSupport* class) place.
* The data classes (*Log*, *Event*) are immutable Value Objects. To construct them
  i use the Builder design pattern.
* To fetch and store data i use the concept of Repositories from DDD.
  The project repositories are: *JsonLogRepository* and *JdbcEventRepository*.
* The infrastructure classes (adapters part of Hexagonal Architecture) are located in
  the *infrastructure* package.