# Twitter demo

## Clone the repo

```
git clone https://github.com/IgorIsailovic/twitter-demo.git
```

## Initialize the database

Make sure to navigate to the folder that contains the docker-compose.yml file.
From there run the command below:

```
docker-compose up
```

With that PostgreSQL DB is started locally.
Username, password, initial DB as well as host port that is bound to the container port are contained in the docker-compose.yml file.

## aplication.properties

The application is set to run on port 8090. That can be changed via the server.port property:
server.port=8090

On the first run of the application spring.jpa.hibernate.ddl-auto should be set to create so that the tables are created:

    spring.jpa.hibernate.ddl-auto=create

On every subsequent restart/start of the application if we wish to save the data in the database spring.jpa.hibernate.ddl-auto should be set to none.

Url for the conection to the database should be:

    spring.datasource.url=jdbc:postgresql://host:5432/tweets

where the host is the machine on which the Postgres container is running. If it’s on the same machine as the application localhost can be used:

    spring.datasource.url=jdbc:postgresql://localhost:5432/tweets

