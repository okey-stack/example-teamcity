import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {

    vcsRoot(HttpsGithubComOkeyStackExampleTeamcityGitRefsHeadsMaster)

    buildType(Deploy)
}

object Deploy : BuildType({
    name = "Test and Deploy"

    artifactRules = "+:target/*.jar"

    vcs {
        root(HttpsGithubComOkeyStackExampleTeamcityGitRefsHeadsMaster)
    }

    steps {
        maven {
            name = "test"

            conditions {
                doesNotEqual("teamcity.build.branch", "master")
            }
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
        maven {
            name = "deploy master"

            conditions {
                equals("teamcity.build.branch", "master")
            }
            goals = "clean deploy"
            userSettingsSelection = "settings.xml"
        }
    }

    triggers {
        vcs {
        }
    }
})

object HttpsGithubComOkeyStackExampleTeamcityGitRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/okey-stack/example-teamcity.git#refs/heads/master"
    url = "https://github.com/okey-stack/example-teamcity.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "okey-stack"
        password = "credentialsJSON:af65f8ec-9293-47f2-a7c6-57c0515f64e9"
    }
})
