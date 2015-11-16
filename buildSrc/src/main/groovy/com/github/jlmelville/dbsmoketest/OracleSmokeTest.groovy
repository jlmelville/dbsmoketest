package com.github.jlmelville.dbsmoketest

import org.gradle.api.tasks.Input

class OracleSmokeTest extends DbSmokeTest {
    @Input
    String driver = 'oracle.jdbc.OracleDriver'

    @Input
    String driverDependency = "oracle:ojdbc6:11.2.0.4"

    @Input
    String validationQuery = 'select sysdate from dual'
}