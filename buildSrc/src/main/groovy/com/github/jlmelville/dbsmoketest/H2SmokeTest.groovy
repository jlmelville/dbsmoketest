package com.github.jlmelville.dbsmoketest

import org.gradle.api.tasks.Input

class H2SmokeTest extends DbSmokeTest {
    @Input
    String driver = 'org.h2.Driver'

    @Input
    String driverDependency = "com.h2database:h2:1.3.173"

    @Input
    String validationQuery = 'SELECT 1'
}