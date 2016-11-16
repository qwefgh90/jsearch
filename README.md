#JSearch

[![Build Status](https://travis-ci.org/qwefgh90/JSearch.svg?branch=master)](https://travis-ci.org/qwefgh90/JSearch)

## Overview
JSearch is the open software to extract string and find keyword from HWP and Office format.

## Download (Maven Central Repository)

```
<dependency>
	<groupId>io.github.qwefgh90</groupId>
	<artifactId>jsearch</artifactId>
	<version>0.2.0</version>
</dependency>
```

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
| static boolean |	isContainsKeywordFromFile(java.io.File file, java.lang.String keyword) <br><strong> get true or false about containing keyword. </strong> |
| static boolean |	isContainsKeywordFromFile(java.lang.String filePath, java.lang.String keyword) <br><strong>  get true or false about containing keyword. </strong> |

