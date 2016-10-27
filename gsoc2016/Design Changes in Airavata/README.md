# Re-Factoring Aurora Marathon Integration Module and gfac-orchestrator core files to incorporate design changes.

##AuroraMarathonIntegration

* Apache style packaging : Organized the AuroraMarathonIntegration code to follow the Apache guidelines.
* Maven integration : Integrated Maven with the module as Airavata is build using Maven.
* Dependency injection pattern
  * Dependency Injection makes it simple for to manage dependencies between objects. In turn, that makes it easier to break coherent functionality off into it's own     contract (interface). As a result, the code has been far more modularized.
  * Another result of this is that I can much more easily see my way through to a design that supports the Open-Closed Principle.[software entities (classes, modules, functions, etc.) should be open for extension, but closed for modification]. This is one of the most confidence inspiring techniques (second only to automated testing).
  * Major advantage : Two important ones are the ability to control functionality from a central place (the Main() function) instead of spreading it throughout your program, and the ability to more easily test each class in isolation (because you can pass mocks or other faked objects into its constructor instead of a real value).
* Extract out the redundant code and create utilities.
* Kept updating code and made the above design changes to be in sync with his changes.


##Orchestrator
* Path to file : orchestrator/orchestrator-core/src/main/java/org/apache/airavata/orchestrator/core/utils/OrchestratorConstants.java
  * Change the class containing constants to enums.
  * Type safety
  * You get free compile time checking of valid values.
      * Catches typos

* Path to file : orchestrator/orchestrator-core/src/main/java/org/apache/airavata/orchestrator/core/utils/OrchestratorUtils.java
  * Use enums instead of directly accessing the static final constants. [Changed the usage]

* Path to file : /orchestrator/orchestrator-core/src/main/java/org/apache/airavata/orchestrator/cpi/impl/SimpleOrchestratorImpl.java
  * Captured in a multiple catch block, instead of 3 different catch clauses.
  * In some functions, the function argument and the instance variable have the same name. This may create ambiguity. Hence the function arguments names were changed to remove the ambiguity. The following are the functions in SimpleOrchestratorImpl.java which had this issue :

* Path to file : airavata/modules/orchestrator/orchestrator-core/src/main/java/org/apache/airavata/orchestrator/core/OrchestratorConfiguration.jav


##GFAC
* Path to files :
  * gfac/gfac-core/src/main/java/org/apache/airavata/gfac/core/authentication/GSIAuthenticationInfo.java
  * gfac/gfac-core/src/main/java/org/apache/airavata/gfac/core/authentication/SSHKeyAuthentication.java
  * gfac/gfac-core/src/main/java/org/apache/airavata/gfac/core/authentication/SSHPasswordAuthentication.java
