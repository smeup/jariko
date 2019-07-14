# Running the interpreter in Docker

From the root directory of the project, run:

```
docker build -f misc/docker/Dockerfile -t interpretercontainer .
```

This will create the `interpretercontainer` container. Then

```
docker run -it interpretercontainer
```