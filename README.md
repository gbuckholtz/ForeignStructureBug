# ForeignStructureBug

## Description of problem

The heart of this bug is that when using the models in Java DMN models that have structures defined in an inluded DMN model will fail to return decisions and not give any indication of error that I could find.
The scesim is doing the same thing as the java code, it passes but the java fails with null decisions.

## models found in dmnModels
 common.dmn is the one that has a simple structure defined
 SpecificROR.dmn is the model that includes common.dml

 ## Java tester app
 JavaExample is a basic example using the two decisions in SpecificROR.dmn

## SCESIM tester
ForeignDataTypeBug.scesim
