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


- # JMeter TS/TJ Time Measurement Report

|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 14                         | 0.55                                | 0.53                      | ??           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 20                         | 5.20                                | 2.75                      | ??           |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | 20                         | 5.46                                | 3.12                      | ??           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 21                         | 6.84                                | 5.63                      | ??           |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 20                         | 0.52                                | 0.51                      | ??           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 20                         | 1.87                                | 1.32                      | ??           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | 21                         | 2.95                                | 2.56                      | ??           |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|