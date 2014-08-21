corpus.sinhala.crawler.controller
=================================

Create a file named *project.properties* and add the bellow properties to it and make the necessasary changes.

    dbHost=localhost
    dbUser=maduranga
    dbPassword=maduranga
    savePath=/home/maduranga/data
    logPath=/home/maduranga/crawler/log

Add the file tocation to the *corpus.sinhala.crawler.controller.SysProperty*

Inside the logPath create a file named *log4j.properties* and add the bellow properties and make the necessary changes.

    # Root logger option
    log4j.rootLogger=DEBUG, file
    
    # Direct log messages to a log file
    log4j.appender.file=org.apache.log4j.RollingFileAppender
    log4j.appender.file.File=/home/maduranga/crawler/log/controller.log
    log4j.appender.file.MaxFileSize=1MB
    log4j.appender.file.MaxBackupIndex=1
    log4j.appender.file.layout=org.apache.log4j.PatternLayout
    log4j.appender.file.layout.ConversionPattern=%-5p - %d{yyyy-MM-dd HH:mm:ss.SSS}; %C; %m\n

Create a database in MySQL named *crawler_data* and run the *crawler_data.sql* in dbscripts folder.

Add the location of the crawler taken from https://github.com/madurangasiriwardena/corpus.sinhala.crawler to the table *crawler*.

Now you can test crawl the newspapers.
Run the *corpus.sinhala.crawler.controller.testClient* to crawl the required newspaper in the given time period.
