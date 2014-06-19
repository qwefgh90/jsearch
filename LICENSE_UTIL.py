# -*- encoding:utf-8 -*-
import sys
import os
license = '''/*
 * Copyright (C) 2014 Changwon CHOE and Bora KIM <qwefgh90@naver.com, lemiyon@naver.com>
 * 
 * This file is part of Foobar.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
'''
license = license.replace('Foobar','OSFAD')
compileText = ''

import re
reobj = re.compile('\.java$')

def getjavafiles(parentPath,resultjavalist):
	lst = os.listdir(parentPath);
	for i in lst :
		i=parentPath+'/'+i
		if(os.path.isdir(i)):
			getjavafiles(i,resultjavalist)
			continue
		with open(i,'r') as f:
			content = f.read(-1) 
			if reobj.findall(f.name) and not license in content and not 'Copyright' in content: #except files to contain license String in content
				resultjavalist.append(i);

def addLicense(path):
	with open(path,'r') as f:
		content = f.read()
		compileText = license + content

	with open(path,'w') as f:
		f.write(compileText)

result=[]
getjavafiles('.',result)
print result
raw_input('Are you add Copyright on those files?  ')
for i in result:
	addLicense(i)