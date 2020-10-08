# How to forge a particular build on JitPack

So you would like to get a version of jariko at a specific commit? Try these steps-
* Find the first ten chars of your commit, for example 4cbb2da621
* Open your browser on an URL like this: https://jitpack.io/#smeup/jariko/4cbb2da621
* Go to the "Builds" label and wait until JitPack tells you that the build is complete
* Now you can use the build in your building system. This is an example for maven:
```
<dependency>
    <groupId>com.github.smeup.jariko</groupId>
    <artifactId>rpgJavaInterpreter-core</artifactId>
    <version>4cbb2da621</version>
</dependency>
```