#JSearch

## Overview
JSearch is the open software to extract string and find keyword from HWP and Office format.

## Requirement
1. It should work with various types of document. ex) hwp, pdf, office 
2. It should support extract string and rapidly find keyword from doucments.
3. It will be jar library.
4. All functions are synchronous.
5. a result of extraction contains full string.
6. a result of finding contains word count.

## Class

**Class JSearch**

public class **JSearch**
extends java.lang.Object
JSearch supports various types of documents with open source engines. 
And this library contains 3 types of functions. extract...() and isContainsKeyword...() and getFileList...() 

HWP, DOC, PPT, EXCEL, TEXT, PDF and UNKNOWN are supported.

Modifier and Type |	Method and Description
static java.lang.String	extractContentsFromFile(java.io.File target) | extract string
static java.lang.String	extractContentsFromFile(java.lang.String filePath) | extract string
static java.util.List<java.io.File>	getFileListContainsKeywordFromDirectory(java.lang.String dirPath, java.lang.String keyword) | get a list of files which are containing keyword.
static java.util.List<java.io.File>	getFileListContainsKeywordFromDirectory(java.lang.String dirPath, java.lang.String keyword, boolean recursive) | get a list of files which are containing keyword.
static boolean	isContainsKeywordFromFile(java.io.File file, java.lang.String keyword) | get true or false about containing keyword.
static boolean	isContainsKeywordFromFile(java.lang.String filePath, java.lang.String keyword) | get true or false about containing keyword.




