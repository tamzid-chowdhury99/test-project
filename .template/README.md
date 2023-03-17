# Maven boot scratch

Sample maven spring boot app

## How to start with Maven Template

1. Create project repo from github template `maven-template`
1. Clone repo into your local
1. Init your java project
1. Add your java project code
1. Setup project for CircleCI build

![create-repo](https://github.com/aetna-digital-applications/internal-doc-contents/blob/master/gifs/template-maven/create-repo.gif?raw=true)

![init-spring](https://github.com/aetna-digital-applications/internal-doc-contents/blob/master/gifs/template-maven/init-spring.gif?raw=true)

![cci-setup-project](https://github.com/aetna-digital-applications/internal-doc-contents/blob/master/gifs/template-maven/cci_setup_project.gif?raw=true)

## Steps to create a maven project and circleci build pipeline

- Add project in CircleCI if it has not been added yet. Please refer to circle documenation for that.
Use this project as a template for CircleCI maven java app build and deploy
- This scratch repo contains the following files. Your project should have these files when you create project using the scratch template and may have to edit these files depending on your project needs.
    - `.cirlcleci/config.yml`
        -  circleci pipeline looks for The `config.yml` in '.circleci' folder to create and build the pipeline
    - `Dockerfile`
        - pipeline uses this dockerfile to build java app. It's using openjdk:11.0.4-jre-slim image.
    - `pipeline/values.yaml`
        - Jenkins use this file for deploying the apps to 'lab/dev/test/prod' environments.  
    - `Jenkinsfile`
        - Jenkinsfile to use the shared pipeline code for deploying apps
- Project specific file/folders
    - Add your project related folders and any other files/folders that may required for building the project
- config.yml contains multiple jobs to build, publish and deploy apps
    - `build-jar` job
        - build job runs maven test, maven deploy and creates the docker image for maven image.
    - `build-docker` job
        - This job runs only Master branch. publish-ah-nexus job executes after the build job is successful and in parallel with 'publish-lab-nexus' job to tag the maven app with Master tag and publish it to AH Nexus
    - `scan-checkmarx` job
        - static analysis scan for your project
    - `scan-docker` job
        - deploy job triggers the deployment of maven app to HC jenkins to deploy  the app in desired environment 'lab/infra/dev/test/prod'.
    - `publish-docker` job
        - deploy job triggers the deployment of maven app to HC jenkins to deploy  the app in desired environment 'lab/infra/dev/test/prod'.
    - `sonar_scan/pull_request` job
        - deploy job triggers the deployment of maven app to HC jenkins to deploy  the app in desired environment 'lab/infra/dev/test/prod'.
    - `tag-and-publish-to-nexus` job
        - only runs when brach name is hotfix-manual. this will semantic tag the branch artifact and push it to nexus.    
    - `nexusiq-scan-jar` job
        - scan job triggers a nexusIQ scan of the jarfile
    - `nexusiq-scan-docker` job
        - scan job triggers a nexusIQ scan of the docker image
- By default, the image uploaded to AH Nexus Repo will be tagged as `${DOCKER_REGISTRY_URL}/${DOCKER_REGISTRY_USERNAME}/${CIRCLE_PROJECT_REPONAME}:${CIRCLE_SHA1:0:7}`.
    - `${CIRCLE_PROJECT_REPONAME}` refers to the image name
    - `${DOCKER_REGISTRY_URL}` refers to AH nexus url
    - `${DOCKER_REGISTRY_USERNAME}` refers to directory in which the image is available in AH nexus.
- Scan image using Twistlock for vulnerabilities
    - Build will fail if scan does not pass minimum vulnerability threshold set in config.yml
- Once merge happens to master `on-merge-to-main-branch` workflow kicks off. This workflow will runs `build-jar` and then `sonar_scan/branch` which
  will run sonarcloud scans for the master branch and then it will run `on-merge-to-main-branch` which will semantic tag last artifact 
  from feature branch and push it to nexus for further deployment reference.    
- Any additional build secrets required for your image can be added to the environment variables in CircleCI or exported directly in the config.yml
- Build should kick off in CircleCI when a pull request is opened against the project repo

## How to add sonarcloud scans to your projects

Each project will have a slightly different configuration. Here are the steps you need to follow for Java based project - 
1. Add jacoco-plugin to your pom.xml 

```xml
	        <plugin>
   				<groupId>org.jacoco</groupId>
   				<artifactId>jacoco-maven-plugin</artifactId>
   				<version>0.8.2</version>
   				<configuration>
   					<append>true</append>
   				</configuration>
   				<executions>
   					<execution>
   						<id>prepare-agent</id>
   						<goals>
   							<goal>prepare-agent</goal>
   						</goals>
   					</execution>
   					<execution>
   						<id>prepare-agent-integration</id>
   						<goals>
   							<goal>prepare-agent-integration</goal>
   						</goals>
   					</execution>
   					<execution>
   						<id>jacoco-site</id>
   						<phase>verify</phase>
   						<goals>
   							<goal>report</goal>
   						</goals>
   					</execution>
   				</executions>
   			</plugin>
```
2. Add sonar-project.properties to the root of your repo and specify information relevant to your project. e.g of what you need to add in
   sonar-project.properties to your project -
```java   
sonar.sources=src
sonar.coverage.jacoco.ReportPaths=target/jacoco.exec
sonar.sourceEncoding=UTF-8
sonar.exclusions=test
```   
3. After you create a PR for the branch your are working and with you should see sonar_scan/pull_request stage in your workflow in circleci and results can be accessed at -
   `https://sonarcloud.io`
