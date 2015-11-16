package com.github.jlmelville.dbsmoketest
import org.gradle.api.tasks.Input

class PostgresSmokeTest extends DbSmokeTest {
    @Input
    String driver = 'org.postgresql.Driver'

    @Input
    String driverDependency = "org.postgresql:postgresql:9.3-1101-jdbc41"

    @Input
    String validationQuery = 'select 1'
}
