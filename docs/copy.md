#/COPY

In this document, we are going to explain the implementation of **/COPY feature**.

The feature **/COPY** is a directive that allows you to include in a RPG program other RPG sources.

The **/COPY** directive resolution is managed at source preprocessing time.

This is possible thanks to the implementation of `SystemInterface` class,which is able to find programs with the same logic as AS400, but with the advantage of source's configuration (file system, Bucket s3, url).

This feature is important because it allows you to test the interpreter with real programs.