#JSearch

## Overview
JSearch is the open software to extract string and find keyword from HWP and Office format.

## Download & Installation (2ways)
[JSearch.jar](https://github.com/qwefgh90/JSearch/raw/master/JSearch-1.0-SHADED.jar)
###1)just import
<br> **Just import JSearch.jar to your project**
###2)make local repository
<strong>mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file  -Dfile=JSearch-1.0-SHADED.jar \
                                                                              -DgroupId=com.qwefgh90.io.jsearch \
                                                                              -DartifactId=JSearch \
                                                                              -Dversion=1.0 \
                                                                              -Dpackaging=jar \
                                                                              -DlocalRepositoryPath=.</strong>
                                                                              <br><br>
<strong>mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file  -Dfile=JSearch-1.0-SHADED.jar \
                                                                              -DgroupId=com.argo \
                                                                              -DartifactId=hwp-utils \
                                                                              -Dversion=0.0.1-SNAPSHOT \
                                                                              -Dpackaging=jar \
                                                                              -DlocalRepositoryPath=.</strong>


## Requirement
1. It should work with various types of document. ex) hwp, pdf, office 
2. It should support extract string and rapidly find keyword from doucments.
3. It will be jar library.
4. All functions are synchronous.
5. a result of extraction contains full string.
6. a result of finding contains word count.

## Class

public class **JSearch**<br><br>
JSearch supports various types of documents with open source engines.<br> 
And this library contains 3 types of functions. extract...() and isContainsKeyword...() and getFileList...() 

HWP, DOC, PPT, EXCEL, TEXT, PDF and UNKNOWN are supported.


| Modifier and Type        | Method and Description |
| ------------- | -----|
| static java.lang.String |	extractContentsFromFile(java.io.File target) <br><strong> extract string </strong> |
| static java.lang.String |	extractContentsFromFile(java.lang.String filePath) <br><strong>  extract string </strong> |
| static java.util.List<java.io.File> |	getFileListContainsKeywordFromDirectory(java.lang.String dirPath, java.lang.String keyword) <br><strong>  get a list of files which are containing keyword. </strong> |
| static java.util.List<java.io.File> |	getFileListContainsKeywordFromDirectory(java.lang.String dirPath, java.lang.String keyword, boolean recursive) <br><strong>  get a list of files which are containing keyword. </strong> |
| static boolean |	isContainsKeywordFromFile(java.io.File file, java.lang.String keyword) <br><strong>  get true or false about containing keyword. </strong> |
| static boolean |	isContainsKeywordFromFile(java.lang.String filePath, java.lang.String keyword) <br><strong>  get true or false about containing keyword. </strong> |

