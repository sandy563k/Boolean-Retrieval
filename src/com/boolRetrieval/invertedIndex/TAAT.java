package com.boolRetrieval.invertedIndex;

import java.io.Writer;
import java.util.Map;

public class TAAT {
	// performs term-at-at-time-And operation
	public static void taatAnd(String[] terms, Map<String, MyLinkedList> dictionary, Writer output, String line)
			throws Exception {

		int numOfComp = 0;
		MyLinkedList result = null;
		Node prevResult = dictionary.get(terms[0]).head;

		for (int i = 1; i < terms.length; i++) {
			result = new MyLinkedList();
			Node p2 = dictionary.get(terms[i]).head;
			while (prevResult != null && p2 != null) {
				if (prevResult.data == p2.data) {
					result.add(prevResult.data);
					prevResult = prevResult.next;
					p2 = p2.next;
					numOfComp++;
				} else if (prevResult.data < p2.data) {
					numOfComp++;
					if (prevResult.hasSkip()) {
						if (prevResult.hasSkip() && (prevResult.skip.data < p2.data)) {
							while (prevResult.hasSkip() && prevResult.skip.data < p2.data) {
								prevResult = prevResult.skip;
								numOfComp++;
							}
						} else {
							numOfComp++;
							prevResult = prevResult.next;
						}
					} else {
						prevResult = prevResult.next;
					}

				} else {
					numOfComp++;
					if (p2.hasSkip()) {
						if (p2.hasSkip() && (p2.skip.data < prevResult.data)) {
							while (p2.hasSkip() && p2.skip.data < prevResult.data) {
								numOfComp++;
								p2 = p2.skip;
							}
						} else {
							numOfComp++;
							p2 = p2.next;
						}
					} else {
						p2 = p2.next;
					}

				}

			}
			prevResult = result.head;

		}
		output.write("TaatAnd");
		output.write("\r\n");
		output.write(line);
		output.write("\r\n");
		output.write("Results:");
		result.printList(output);
		output.write("Number of documents in results: " + result.size);
		output.write("\r\n");
		output.write("Number of comparisons: " + numOfComp);
		output.write("\r\n");

	}

	// performs term-at-at-time-Or operation
	public static void taatOr(String[] terms, Map<String, MyLinkedList> dictionary, Writer output, String line)
			throws Exception {
		int numOfComp = 0;
		MyLinkedList result = null;
		Node prevResult = dictionary.get(terms[0]).head;

		for (int i = 1; i < terms.length; i++) {
			result = new MyLinkedList();
			Node p2 = dictionary.get(terms[i]).head;
			while (prevResult != null && p2 != null) {

				if (prevResult.data == p2.data) {
					result.add(prevResult.data);
					prevResult = prevResult.next;
					p2 = p2.next;
					numOfComp++;
				} else if (prevResult.data < p2.data) {
					numOfComp++;
					result.add(prevResult.data);
					prevResult = prevResult.next;
					while (prevResult != null && prevResult.data < p2.data) {
						numOfComp++;
						result.add(prevResult.data);
						prevResult = prevResult.next;
					}

				} else {
					numOfComp++;
					result.add(p2.data);
					p2 = p2.next;
					while (p2 != null && p2.data < prevResult.data) {
						numOfComp++;
						result.add(p2.data);
						p2 = p2.next;
					}

				}

			}

			while (prevResult != null) {
				result.add(prevResult.data);
				prevResult = prevResult.next;
			}
			while (p2 != null) {
				result.add(p2.data);
				p2 = p2.next;
			}

			prevResult = result.head;

		}
		output.write("TaatOr");
		output.write("\r\n");
		output.write(line);
		output.write("\r\n");
		output.write("Results:");
		result.printList(output);
		output.write("Number of documents in results: " + result.size);
		output.write("\r\n");
		output.write("Number of comparisons: " + numOfComp);
		output.write("\r\n");

	}

}
