# BSB Directory


### Running this application

#### Pre-requisites
- Docker
- Mongo

#### Steps
- Build `./gradlew clean build`
- Start Mongo container `docker-compose up -f docker-compose.yml -d ` (references [docker-compose.yml](docker-compose.yml))
- Start application `./gradlew bootRun`
- This will read contents of BSB file and load them into MongoDB collection. 
  - `all-records: "BSBDirectoryNov19-284.csv.original"` Points to classpath file resource that has *all* BSB details

#### TODO
- Instead of needing BSB file as classpath resource, the application should FTP it from APCA
- Instead of loading files only on start-up, maybe set this up as a scheduled job ?
- Add creationTime and bsb file name while loading entries
- Handle case when BSBs no longer sent in file i.e. was present originally but not sent anymore - should be deleted from Collection.
  - *Is this possible ? Or will APCA always send through all BSBs, closed ones will have empty string for PEH payment flag.*

### Reference Documentation and Guides
The following guides illustrate how to use some features concretely:

* [Spring Batch](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#howto-batch-applications)
* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Testing your Batch Job](https://www.baeldung.com/spring-batch-testing-job)
* [Spring Batch: CSV to Mongo](https://github.com/walkingtechie/spring-batch-csv-to-mongo)

