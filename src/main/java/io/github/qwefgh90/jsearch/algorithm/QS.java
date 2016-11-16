package io.github.qwefgh90.jsearch.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * This is for Quick Search Algorithm.
 * <br><br>
 * <strong>process</strong>
 * <ol>
 * <li>
 * compile your pattern
 * </li>
 * <li>
 * call findAll() or isExist()
 * </li>
 * </ol>
 * @author cheochangwon
 */
public class QS {
	/*
	 * useless 
	 * --->
	 */
	public static QS qs = null;	
	private static void preQsBc(char[] x, int qsBc[]) {
		int i, m = x.length;

		for (i = 0; i < qsBc.length; ++i)
			qsBc[i] = m + 1;
		for (i = 0; i < m; ++i)
			qsBc[x[i]] = m - i;
	}

	private static int arrayCmp(char[] a, int aIdx, char[] b, int bIdx,
			int length) {
		int i = 0;

		for (i = 0; i < length && aIdx + i < a.length && bIdx + i < b.length; i++) {
			if (a[aIdx + i] == b[bIdx + i])
				;
			else if (a[aIdx + i] > b[bIdx + i])
				return 1;
			else
				return 2;
		}

		if (i < length)
			if (a.length - aIdx == b.length - bIdx)
				return 0;
			else if (a.length - aIdx > b.length - bIdx)
				return 1;
			else
				return 2;
		else
			return 0;
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
//		System.out.println(Integer.toHexString(x[0]));
		int j, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] qsBc = new int[65536];

		/* Preprocessing */
		preQsBc(x, qsBc);

		/* Searching */
		j = 0;
		while (j < n - m) {
			if (arrayCmp(x, 0, y, j, m) == 0)
				result.add(j);
			j += qsBc[y[j + m]]; /* shift */
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			result.add(j);

		return result;
	}

	/**
	 * <<<-----
	 * end of useless 
	 */

	/**
	 * Thread Safe
	 * @param pattern - keyword you want
	 * @return QS - New QuickSeach object
	 */
	public static QS compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;

		int[] qsBc = new int[65536];

		preQsBc(x, qsBc);
		
		QS qs = new QS();
		qs.m = m;
		qs.x = x;
		qs.qsBc = qsBc;
		qs.pattern = pattern;
		return qs;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int j, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		while (j < n - m) {
			if (arrayCmp(x, 0, y, j, m) == 0)
				result.add(j);
			j += qsBc[y[j + m]]; /* shift */
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			result.add(j);

		return result;
	}
	
	public boolean isExist(String source) {
		char[] y = source.toCharArray();
		int j, n = y.length;
		
		j = 0;
		while (j < n - m) {
			if (arrayCmp(x, 0, y, j, m) == 0)
				return true;
			j += qsBc[y[j + m]]; /* shift */
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			return true;

		return false;
	}
	
	private char[] x;
	private int m;
	private int[] qsBc;
	String pattern=null;
}