#JSearch (developing)

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

**JSearch**

- **String extractContentsFromPath(String path)** - It returns contents or null (File version will be created)
- **JSResult searchKeywordFromPath(String path, String keyword)** - It returns JSResult (File version will be created)
- **List<JSResult> searchKeywordFromDirPath(String dirPath, String keyword, boolean recursive)** - It returns a list of JSResult

**JSResult**

getter/setter is provided.

- boolean result 
- File target 
- String requestKeyword 
- int wordCount 
