# Spring Example Project

This project aim to provide the ability to develop our CI/CD pipeline. 
This project should be as all other project in terms of it's runnable interface and helm configuration.


# New Back-End micro service steps : 


## 1. New repository in github
1. open new gitHub repository with template spring-example.
2. clone the new repository to your local machine.

## 2. Database

1. flywaydb 
     - create new directory under resources with name db.
     - create new directory under db with name migration.
     - create new empty file at migration directory with name V1_1__initial.sql
2. Create new cluster db at aws. 

## 3. Config server 

1. new micro service configuration
2. gate configuration routing to new micro service. 

## 4. New ECR repository

1. See the helm-assembly readme: https://github.com/bizzabo/helm-assembly#4-create-a-new-ecr-repository 

## 4. Route 53 aws 

1. DB - Create record set 
    - Name of the new database 
    - Type : CName 
    - Value is the name of the DB
2. DBp - Create record set 
    - Name of the new database 
    - Type : IPV4 
    - Value is The IP address of the machine (db machine).

## 5. Helm assembly

1. Bizzabo charts add the new micro-service.
2. Change yaml values by adding the new micro-service with specific replication. 

## 6. Deploy 

1. Dev env 
2. Passive
3. Prod
 
