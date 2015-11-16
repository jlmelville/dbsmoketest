package com.github.jlmelville.dbsmoketest

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.StopExecutionException

class SmokeTest extends DefaultTask {

    @Input
    String errorMessage

    @Input
    boolean stopBuildOnFailure = true

    Exception ex

    protected handleTestResult(boolean success) {
        if (!success) {
            if (ex) {
                errorMessage += ": ${ex}"
            }
            if (stopBuildOnFailure) {
                throw new GradleException(errorMessage)
            }
            else {
                logger.quiet "Smoke test FAILURE: ${errorMessage}"
                throw new StopExecutionException(errorMessage)
            }
        }
    }
}