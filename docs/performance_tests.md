# Performance tests

We defined some tests in order to measure the performance of the interpreter.

The standard test tasks (i.e _test_ and _check_) execute all tests except for performance tests. For example:

```
./gradlew check
```

Performance test are tagged with the ```@Category(PerformanceTest::class)``` annotation:
```
@Test @Category(PerformanceTest::class)
fun executeMUTE10_01_perf_calls() {
    assertEquals(LinkedList<String>(), outputOf("performance/MUTE10_01"))
}
```
At the moment, we collected the performance test _.rpgle_ examples in the [performance](https://github.com/smeup/smeup-rpg/tree/master/rpgJavaInterpreter-core/src/test/resources/performance) folder.

To run performance tests (i.e. tests tagged with the annotation `@Category(PerformanceTest::class)`) use this gradle command:
```
./gradlew testPerformance
```

You can run all tests at the same time this way:
```
./gradlew testAll
```

The configuration of these tasks is defined in [build.gradle](https://github.com/smeup/smeup-rpg/blob/master/rpgJavaInterpreter-core/build.gradle)
```
test {
...
    useJUnit {
        excludeCategories 'com.smeup.rpgparser.PerformanceTest'
    }
}

task testPerformance(type: Test) {
...
    useJUnit {
        includeCategories 'com.smeup.rpgparser.PerformanceTest'
    }
}
```