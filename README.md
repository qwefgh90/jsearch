#JSearch

[![Build Status](https://travis-ci.org/qwefgh90/jsearch.svg?branch=master)](https://travis-ci.org/qwefgh90/jsearch)

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

### HWP

This software has been developed with reference to
the HWP file format open specification by Hancom, Inc.
http://www.hancom.co.kr/userofficedata.userofficedataList.do?menuFlag=3
한글과컴퓨터의 한/글 문서 파일(.hwp) 공개 문서를 참고하여 개발하였습니다. 

a part to handle *.hwp* format is forked source in *[java-hwp](https://github.com/ddoleye/java-hwp)* project.
