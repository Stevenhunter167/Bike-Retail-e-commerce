- # Project 5 General
    - #### Team#: team-108
    
    - #### Names: Litian Liang, Yaosheng Xu
    
    - #### Project 5 Video Demo Link: https://youtu.be/-912-eqa9vQ

    - #### Instruction of deployment: 
    ```bash
    cd team-108
    mvn package
    ```
    The packaged war file will be under team-108/target/team-108.war<br>
    goto: http://your-server-ip/manager/ and deploy this .war file<br>
    Check tomcat status on the redirect page, [OK] means the application is successfully deployed.

    - #### Collaborations and Work Distribution:
    Yaosheng Xu 50%, Litian Liang 50%
    
    - #### Login Credential:
    
    email: debra.burks@yahoo.com
    
    password: revolution

- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    A pool of connection is pre-established and ready to use on demand. When a connection is made, instead of creating a connection on the go, database driver will select a conenction thread in the specified resource and use it.
    
    This is done by changing our existing DB.connect() function to use connection pooling instead of creating new connection on the go.
    
    - #### Explain how Connection Pooling works with two backend SQL.
    2 datasource is specified in context.xml
    
    source 1: root@localhost
    
    source 2: root@masterip
    
    when connectMaster() is called it will only visit source 2 (master instance), which is implemented in DB.java.
    
    when connect() is called it will only visit mysql at localhost, no matter it's master or slave.
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

    - #### How read/write requests were routed to Master/Slave SQL?
    
    Change 1: MYSQL MASTER:
    All access are granted to master instance by using:
    
        GRANT ALL ON bikedb.* TO user@'masterip' IDENTIFIED BY 'pswd';
    
    Read Only access are granted to slave instance by using:
    
        GRANT SELECT ON bikedb.* TO user@'slaveip' IDENTIFIED BY 'pswd';
        
    ps: I also grant full access to my local machine for debugging
    
    Change 2: context.xml:
    
    Create a new data source at masterip
    
    Change 3: java class DB added function: connectMaster()
    
    Calling connectMaster() will make connection to master instance, while connect() only connects to local mysql instance.
    usage: When updating or accessing with explicit mentioning master=true, a connection is made directly to master mysql instance.

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
    ```
    python3 ~/tomcat/webapps/team-108/WEB-INF/log_processing.py
    ```
    will output the statistics read from log.txt.
    
    to clear this log, use: 
    ```
    rm log.txt
    ```

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-108/blob/master/img/single%20http%201%20thread.jpeg   | 14                         | 0.55                                | 0.53                      | normal load           |
| Case 2: HTTP/10 threads                        | https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-108/blob/master/img/single%20http%2010%20thread.jpeg   | 20                         | 5.20                                | 2.75                      | more load, slower           |
| Case 3: HTTPS/10 threads                       | https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-108/blob/master/img/single%20https%2010%20threads.png   | 20                         | 5.46                                | 3.12                      | no essential diff from case2           |
| Case 4: HTTP/10 threads/No connection pooling  | https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-108/blob/master/img/single%20http%20no%20pool.jpeg   | 21                         | 6.84                                | 5.63                      | significant increase in jdbc connection time     |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-108/blob/master/img/scaled%201%20thred.png   | 20                         | 0.52                                | 0.51                      | normal behavior           |
| Case 2: HTTP/10 threads                        | https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-108/blob/master/img/scaled%2010%20threads.png   | 20                         | 1.87                                | 1.32                      | more load, everything slower           |
| Case 3: HTTP/10 threads/No connection pooling  | https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-108/blob/master/img/scaled%2010%20threads%20no%20pool.png   | 21                         | 2.95                                | 2.56                      | servlet process time mainly increased because of JDBC connection time           |

## cs122b project4 team-108
#### aws url: https://54.193.35.56:8443/team-108/ or http://54.193.35.56:8080/team-108/
#### demo url: https://youtu.be/2GoXXSfXdAA
#### how to deploy your application with Tomcat

```bash
cd team-108
mvn package
```
The packaged war file will be under team-108/target/team-108.war<br>
goto: http://your-server-ip/manager/ and deploy this .war file<br>
Check tomcat status on the redirect page, [OK] means the application is successfully deployed.
#### contribution
Yaosheng Xu 60%, Litian Liang 40%

## cs122b project3 team-108
#### demo url: https://youtu.be/hpI-H8HWzBo
#### aws url: https://54.193.35.56:8443/team-108/ or http://54.193.35.56:8080/team-108/

```bash
cd team-108
mvn package
```

The packaged war file will be under team-108/target/team-108.war<br>
goto: http://your-server-ip/manager/ and deploy this .war file<br>
Check tomcat status on the redirect page, [OK] means the application is successfully deployed.

#### Places where we used prepared statement
We wrote our helper function DB.listAccess that calls on PrepareStatement with arguments.
Filename            Link
BikeList            /api/bikelist
SingleBike          /api/singlebike
SingleBikeStores    /api/singlebikestores
SingleStore         /api/singlestore
SingleStoreBikes    /api/singlestorebikes
CartServlet         /api/cart
LoginServlet        /api/login
PaymentServlet      /api/payment
SearchServlet       /api/search
staffLoginServlet   /api/staffLogin
staffServiceServlet /api/staffService

#### Two parsing time optimization strategies
1. Write all records as 1 big insert statement (before 20min - after 5 min)
2. Using hashset to identify repeating records (before 3 min - after 10s )

#### Inconsistent data from parsing report

<strong>(when parsing: Store.xml)</strong>

No of Store '14000'.

Duplicate Names:

Dawn 2403017 Bike

Norton 9359058 Bike

\# of missing data entries: 3

<strong>(when parsing: Bicycles.xml)</strong>

No of Bicycle '30000'.

Duplicate names:

Wilier 753767 - 2008

Hutch 2018550 - 2014

Argon 18 1226448 - 2005

New Brands:

New Brand14

New Brand13

New Brand12

New Brand6

New Brand5

New Brand4

New Brand3

New Brand2

New Brand11

New Brand10

New Brand1

New Brand0

New Brand9

New Brand8

New Brand7

\# of missing data entries: 4

<strong>(when parsing: Stocks.xml)</strong>

Already added Store: 'King 6903449 Bike' and product 'New Brand4 3942611 - 2003'.

Already added Store: '7770 8006448 Bike' and product 'CIOCC 1870205 - 2001'.

Already added Store: 'Amber 3679670 Bike' and product 'Stanridge Cycles 5743641 - 2000'.

Already added Store: 'Rhonda 6924438 Bike' and product 'Altruiste Bikes 2214351 - 2002'.

Already added Store: 'Ochoa 18176 Bike' and product 'Upland 5495221 - 2000'.

Already added Store: 'Sexton 1777114 Bike' and product 'Schwinn 3561761 - 2018'.

Already added Store: 'Garza 2879564 Bike' and product 'New Brand9 5455230 - 2007'.

Already added Store: '4972, 954077 Bike' and product 'Framed 9778104 - 2005'.

Already added Store: 'Justin 5040506 Bike' and product 'Gary Fisher 3752300 - 2006'.

Already added Store: 'Michele 2621026 Bike' and product 'Redline 4198875 - 2006'.

There is no store: 'FakeStore1' in db.

There is no store: 'FakeStore2' in db.

There is no store: 'FakeStore3' in db.

#### code contribution of each member
Litian Liang: 55%, Yaosheng Xu: 45%

## cs122b project2 team-108
#### demo url: https://youtu.be/6yzBBIXaMcw

#### instruction of deployment:

```bash
cd team-108
mvn package
```
The packaged war file will be under team-108/target/team-108.war<br>
goto: http://your-server-ip/manager/ and deploy this .war file<br>
Check tomcat status on the redirect page, [OK] means the application is successfully deployed.

#### substring matching design:
For "brand" and "store", we uses LIKE "%[brand_name / store_name]%", which can match a target given any substring.<br>
For "year" and "category", we uses "=" to match exactly the same string, because a substring of "year" makes no sense, and "category" is provided in a checkbox.<br>
For "product_name", we have two parts:<br>
1. In browsing page, we uses LIKE "[product_name]%" to match any product that starts with a specific letter/number/specific character, given in the front end.<br>
2. In search page, we uses LIKE "%[product_name]%" to match any prodcut that has the substring given by the user, similar to "brand" and "store".


#### contribution for each member:
Litian Liang: front end basic structure and debug, backend search servlet, payment and search session

Yaosheng Xu: backend basic structure and debug, front end beautify (CSS and JS), nav bar, and front end payment page and debug

estimate front end backend work distribution:

Litian Liang: 60% front end, 40% backend

Yaosheng Xu: 40% front end, 60% backend


## cs122b project1 team-108

#### We choose to use our bikedb dataset, which is approved by TA that the complexity is comparable with fablix.
You can go into Bikedb_workspace/createBikeTable.sql and Bikedb_workspace/BikedbInsertData.sql and checkout our dataset.

member 1: Litian Liang<br>litianl1@uci.edu  (梁力天)<br> github: Stevenhunter167

member 2: Yaosheng Xu<br>yaoshenx@uci.edu   (许耀昇)<br> github: APM150<br>
(SIDE NOTE: "许耀昇" and "APM150" in the commit bar are the same person. However, "许耀昇"'s work have not counted into the contributors bar. Please notify that we have balanced the workload. Sorry for the inconvince. Thank you so much!)

aws: http://13.52.246.197:8080/team-108/

#### demo url:
https://youtu.be/7Ih_OAJ6fEA

#### how to deploy your application with Tomcat

```bash
cd team-108
mvn package
```
The packaged war file will be under team-108/target/team-108.war<br>
goto: http://your-server-ip/manager/ and deploy this .war file<br>
Check tomcat status on the redirect page, [OK] means the application is successfully deployed.

#### Member contributions:
Litian Liang: front end html, js, css<br>
Yaosheng Xu: backend servlet, jdbc<br>
Both: bikedb dataset crowling and cleaning (under bikedb workspace folder)
