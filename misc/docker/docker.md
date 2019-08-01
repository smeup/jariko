# Running the interpreter in Docker

From the root directory of the project, run:

```
docker build -f misc/docker/Dockerfile -t interpretercontainer .
```

This will create the `interpretercontainer` container. Then

```
docker run -it interpretercontainer
```

If you want to execute rpg programs from your current directory, you can mount it in the `/rpg` direcotry of the conatiner:

```
docker run -it -v $PWD:/rpg interpretercontainer
```

You can find a prebuilt docker image on dockerhub. Run it, for example, this way:

```
docker run -it -v $PWD:/rpg rpginterpreter/interpretercontainer
```
