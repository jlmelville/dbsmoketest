package com.github.jlmelville.dbsmoketest

import java.sql.SQLException

import groovy.sql.Sql

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/*
 * Subclass this and provide the driver, driverDependency, and validationQuery inputs
 */
class DbSmokeTest extends SmokeTest {
    @Input
    String url

    @Input
    String username

    @Input
    String password

    @Input
    String driverRepo

    @TaskAction
    void letThereBeSmoke() {
        loadDriver()
        boolean success = isUp(url, username, password, driver)
        handleTestResult(success)
    }

    private void loadDriver() {
        project.repositories.maven { url driverRepo }
        project.configurations.create "jdbcdriver"
        project.dependencies.add("jdbcdriver", driverDependency)
        project.configurations.jdbcdriver.files.each {
            Sql.classLoader.addURL it.toURI().toURL()
        }
    }

    private boolean isUp(String url, String username, String password, String driver) {
        try {
            Sql.withInstance(url, username, password, driver) { sql ->
                sql.execute validationQuery
            }
            return true
        }
        catch (SQLException e) {
            ex = e
            return false
        }
        catch (Exception e) {
            ex = e
            return false
        }
    }
}
