# iast-jvm

This repository contains a proof-of-concept (PoC) for a IAST (Interactive Application Security Testing) agent.
Make sure you have postgres running locally !

First, navigate to the iast-agent directory and hit:

```mvn clean package```

I've included a PoC for SQL injection to validate the capabilities of the IAST agent. To test:

Run your application with the VM option pointing to the IAST agent JAR:

```$pwd/iast-agent/target/iast-agent-1.0-SNAPSHOT.jar```

and hit:

```curl --location --request GET 'http://localhost:8080/index?name=toto' ```

If successful, you should receive a failure message resembling:

```
IAST Detector: Adding a new tainted data to the pool: toto
IAST Detector: Propagation of Tainted value: toto
IAST Detector: Adding a new tainted data to the pool: SELECT * FROM USERS WHERE id=toto
-------------IAST Detector - SQL Injection: Tainted data reached Sensitive Sink-------------
Propagator: com/applicationsec/Controller#index
Source: com/applicationsec/Controller#index
```