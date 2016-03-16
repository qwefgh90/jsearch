#JSearch

## Overview
JSearch is the open software to extract string and find keyword from HWP and Office format.

## Download & Installation (2ways)
[JSearch.jar](https://github.com/qwefgh90/JSearch/raw/master/JSearch-1.0-SHADED.jar)
###1)just import
<br> **Just import JSearch.jar to your project**
###2)make maven local repository

&lt;repositories&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;repository&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;id&gt;local-repo&lt;/id&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;name&gt;local-repo&lt;/name&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;url&gt;https://github.com/qwefgh90/maven-repository/raw/master/repository &lt;/url&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;releases&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;enabled&gt;true&lt;/enabled&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;updatePolicy&gt;never&lt;/updatePolicy&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/releases&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;snapshots&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;enabled&gt;true&lt;/enabled&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;updatePolicy&gt;never&lt;/updatePolicy&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/snapshots&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;/repository&gt; <br>
&lt;/repositories&gt;<br>
	<br>
&lt;dependency&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.qwefgh90.io.jsearch&lt;/groupId&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;JSearch&lt;/artifactId&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;1.0&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
<br>
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

