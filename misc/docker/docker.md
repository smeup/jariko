# Running the interpreter in Docker

From the root directory ov the project, run:

```
docker build -f misc/docker/Dockerfile -t interpreterContainer
```

This will create the interpreterContainer container. Then

```
docker docker run -it interpreterContainer
```