# DSM4J
[![Build Status](https://travis-ci.com/ccenyo/JDSM.svg?branch=master)](https://travis-ci.com/github/ccenyo/JDSM)

Dsm for java is an open source java library for DSM Api, it's basically a wrapper for the API.
you can find all informations about the api [here](https://global.download.synology.com/download/Document/Software/DeveloperGuide/Package/FileStation/All/enu/Synology_File_Station_API_Guide.pdf) available for developers who want's to know how the api works.

If you find yourself on this page, it means that you have a java project and you need to communicate with your Nas Synology.
This wrapper can be a great startup for that.
I am trying to build the most complete wrapper for synology.
You are welcome if you want to help me, i will be greatfull.

##Prerequisites

This project is written in java 8. you can only use it in a project that uses java 8 and above.
if you want to use it in any environment, make sure you the right JVM installed on the machine.

##Integration tests

Most of the tests need an actual synology Nas. 
Make sure you have the synology in the same network as your server, or set a DDNS to access your synology easily from internet. Look at this official tutorial : [How to make Synology NAS accessible over the Internet](https://www.synology.com/en-global/knowledgebase/DSM/tutorial/Network/How_to_make_Synology_NAS_accessible_over_the_Internet)
Don't forget to use the firewall to restrict unknown ip from accessing your portal.

you have to define an environment file  in your resource folder``env.properties`` in which you have to put this lines :

| host=http://example.diskstation.me |                                                             
| --- |                                                                     
| port=5000 | 
| username=user  | 
| password=passwd   | 

## How to use it

add the maven repository to your pom.xml

```java
    <dependency>

    </dependency>
```

##Basic usage


```java
    DsmAuth auth = DsmAuth.fromResource("env.properties");
    DsmFileStationClient client = DsmFileStationClient.login(auth);
    
    //get the list of all folders in a ROOT_FOLDER = /homes
    Response<DsmListFolderResponse> response = client.ls(ROOT_HOME).call();

    //Files
    response.getData().getFiles();
```

you can create an ``auth`` object in different ways

```java
    DsmAuth auth = DsmAuth.fromResource("env.properties");
```
or
```java
    DsmAuth dsmAuth = DsmAuth.fromFile(file);
```
or
```java
    DsmAuth dsmAuth = DsmAuth.of(host, port, username, password);
```

## Features

#### FileStation features

| Feature                   | API                           | method            | Description                                                           
| ---                       | ---                           |---                |---                                                                   
| Login session             | `SYNO.API.Auth`               | login             | Login to synology dsm
| Logout session            | `SYNO.API.Auth`               | logout            | Logout 
| List all shared folders   | `SYNO.FileStation.List`       | list_share        | List all shared folders,  and get detailed file information
| List of folders/files     | `SYNO.FileStation.List`       | list              | Enumerate files in a shared folder, and get detailed file information
| upload file               | `SYNO.FileStation.Upload`     | upload            | Upload content to the cloud.
| Download files            | `SYNO.FileStation.Download`   | download          | Download files/folders. If only one file is specified, the file content is responded. If more than one file/folder is given, binary content in ZIP format which they are compressed to is responded.
| Delete a file or folder   | `SYNO.FileStation.Delete`     | delete            | Delete file synchoniously                                                                                 
| Delete a file or folder   | `SYNO.FileStation.Delete`     | start/status/stop | Delete file asynchoniously, non-blocking method     

If you don't know how to use the methods, feel free to look at the tests, i always make sure to use the methods in different ways in the tests.

##RoadMap
I will be completing new methods regularely

 * Adding all methods for FileStation
 * Adding features for DownloadStation
 * Adding features for AudioStation
 
## Built With
* [Java SDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) -  Java™ Platform
* [Maven](https://maven.apache.org/) - Dependency Management

## Author
* **Cenyo Medewou** - [medewou@gmail.com](mailto:medewou@gmail.com). 
 
## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details                                                                        

