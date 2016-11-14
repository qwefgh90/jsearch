# Unmanaged libraries
- com.argo / java-hwp / 0.1 (https://github.com/ddoleye/java-hwp)

# How to deploy

Take a look at a example here (https://devcenter.heroku.com/articles/local-maven-dependencies)

## Code example 

```mvn deploy:deploy-file -Durl=file://C:\JSearch\ext_lib -Dfile=java-hwp.jar -DgroupId=com.argo -DartifactId=java-hwp -Dpackaging=jar -Dversion=0.1```



