# BSB Directory

### AusPayNet BSB FTP Overview
> AusPayNet BSB data is now available via download from a public FTP connection. Downloadable files include a full list and an update list, both available in CSV and TXT file formats.
> AusPayNet BSB data is generated and published at least once a month on the first business day of the month. However, it is possible that data could be generated more than once in a given calendar month.
> 
> #### File Naming Conventions
> The file naming convention for the full list is comprised of the previous month and report number. An example of this is below:
> 
> - BSBDirectoryMay13-205.csv
> - BSBDirectoryMay13-205.txt
> 
> The file naming convention for the update list is the date of the day after the last report was generated. An example of this is below (data was generated on 1 May 2013):
> 
> - BSB Directory Update 02May13-03Jun13.csv
> - BSB Directory Update 02May13-03Jun13.txt
> 
> #### URL
> #### Please note that this is not the address to use if you’re attempting to set up an automated FTP procedure. 
> The BSB Data can be downloaded from the following location: `ftp://bsb.hostedftp.com/~auspaynetftp/BSB`
> 
> Instructions for downloading via FTP, including worked examples and file formats can be downloaded [here](http://bsb.apca.com.au/public/BSB_DB.NSF/0/72E7EB6B4734232ECA2579650017682D/$File/Downloading%20BSB%20Files%20from%20AusPayNet%20via%20FTP.pdf).
> 
> #### Contact
> Should you have any questions or encounter any issues whilst downloading from the AusPayNet BSB Data FTP, please contact us at operations@auspaynet.com.au.

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
- Instead of loading files at scheduled intervals, maybe 
  - provide REST endpoint to upload file?
  - OR FTP from configured path at scheduled intervals?
- Add creationTime and bsb file name while loading entries
- Handle case when BSBs no longer sent in file i.e. was present originally but not sent anymore - should be deleted from Collection.
  - *Is this possible ? Or will APCA always send through all BSBs, closed ones will have empty string for PEH payment flag.*

### Reference Documentation and Guides
The following guides illustrate how to use some features concretely:

* [Spring Batch](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#howto-batch-applications)
* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Testing your Batch Job](https://www.baeldung.com/spring-batch-testing-job)
* [Spring Batch: CSV to Mongo](https://github.com/walkingtechie/spring-batch-csv-to-mongo)

