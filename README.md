# dbsmoketest
Some simple example tasks for using Gradle to smoke test databases.

## Installing
I suggest simply sticking these in the `buildSrc` directory of your Gradle project.

## Using
There's an example of using the [H2 database](http://www.h2database.com/html/main.html) in `example.gradle`, 
but it's small enough to give in full:

```Gradle
import com.github.jlmelville.dbsmoketest.H2SmokeTest

task checkDb(type: H2SmokeTest) {
    url = "jdbc:h2:~/test;IFEXISTS=TRUE"
    username = "sa"
    password = ""
    driverRepo = "https://repo1.maven.org/maven2/"
    errorMessage = "DB doesn't seem to be up"
    stopBuildOnFailure = false
}
```

if you can install H2 and get as far as logging in the 
[quickstart guide](http://www.h2database.com/html/quickstart.html), then you should be able to successfully smoke test
the test DB with:

```Bash
gradle -b example.gradle checkDb
```

There are classes for Oracle and Postgres too, so it should be enough to give the general idea. The `stopBuildOnFailure`
flag is useful if you're running this in Jenkins, when the build failing will normally prevent the logs getting 
archived.

## Why?
In his excellent book Gradle in Action, Benjamin Muschko provides some useful tasks for smoke testing web apps via HTTP.
You can extend these ideas quite easily to attempting to smoke test a database by connecting via JDBC and then executing
a simple query.

What makes this a bit trickier is that you need to make sure that the JDBC drivers get loaded at the correct time. A 
straightforward solution is to add the following the your `build.gradle` whenever you want to load a JDBC driver:

```Gradle
repositories {
    maven {
       // ...
    }
}

configurations {
    jdbcdriver
}

dependencies {
    jdbcdriver 'oracle:ojdbc6:11.2.0.4'
}

task smokeTest {
    configurations.jdbcdriver.files.each {
        Sql.classLoader.addURL it.toURI().toURL()
    }

    // This is the only bit that does anything task-like
    Sql.withInstance(url, username, password, driver) { sql ->
        sql.execute 'select sysdate from dual'
    }
}
```
 
Quite a palaver, just to load a JDBC driver. The usual way to tidy things up by writing a custom task only cleans up
the Sql stuff in the `smokeTest` task. You can't just copy and paste the `repositories`, `configurations`
and `dependencies` blocks into the buildSrc `build.gradle`, because they need to be loaded when the task gets run, not
when it's built. So you would need to keep them littering your build script, and now there's not even anything terribly
obvious to indicate why they're there.

My solution was to eschew the declarative approach and use the Gradle API to load things from a private method in the
custom task. This was originally just a gist, but there are just enough moving parts that I don't know if anyone
could reproduce this from just some snippet of codes. Hence this project.

## Thanks
This is based heavily on the material in Chapter 15 of Gradle in Action. Author 
[Benjamin Muschko](https://github.com/bmuschko) kindly got in touch with Manning to explicitly apply a license to the 
[source code](https://github.com/bmuschko/gradle-in-action-source) for the book so I could publish this without fear of
infringing copyright. Obviously, the horrific quality of the code in these tasks is not a reflection on the material 
in the book.

## License
[The MIT License](http://opensource.org/licenses/MIT).