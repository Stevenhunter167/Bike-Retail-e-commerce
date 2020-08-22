-- Grant privilege to ip address

GRANT ALL ON bikedb.* TO root@'ipAddrHere' IDENTIFIED BY '123456';
-- ADD dev, slave, and master ip to master

-- mysql> GRANT ALL ON bikedb.* TO root@'68.5.191.132' IDENTIFIED BY '123456';
# Query OK, 0 rows affected, 1 warning (0.00 sec)
#
# mysql> GRANT ALL ON bikedb.* TO root@'3.101.61.39' IDENTIFIED BY '123456';
# Query OK, 0 rows affected, 1 warning (0.00 sec)
#
# mysql> GRANT SELECT ON bikedb.* TO root@'13.56.212.121' IDENTIFIED BY '123456';
# Query OK, 0 rows affected, 1 warning (0.00 sec)