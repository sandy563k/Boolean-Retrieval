package com.boolRetrieval.invertedIndex;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DAAT {
	// performs document-at-at-time-Or operation
	public static void daatOr(String[] terms, Map<String, MyLinkedList> dictionary, Writer output, String line)
			throws Exception {
		List<MyLinkedList> L = new ArrayList<>(terms.length);
		int numOfComp = 0;
		int maxDocId = -1;
		MyLinkedList result = new MyLinkedList();

		// get maxdocId to decide when to stop
		for (String term : terms) {
			MyLinkedList c = dictionary.get(term);
			L.add(c);
			if (c.tail.data > maxDocId) {
				maxDocId = c.tail.data;
			}
		}

		int docId = Integer.MAX_VALUE;
		while (docId != maxDocId) {
			docId = Integer.MAX_VALUE;
			MyLinkedList minList = null;
			// get min at each stage
			for (MyLinkedList currentList : L) {
				if (currentList.current != null) {
					if (currentList.current.data < docId) {
						docId = currentList.current.data;
						minList = currentList;
					}
				}
			}
			if (minList == null) {
				break;
			}
			// add min to result
			// numOfComp++;
			result.add(minList.current.data);
			minList.current = minList.current.next;
			// move past min in other lists as well
			for (MyLinkedList currentList : L) {
				if (currentList == minList) {
					continue;
				}

				if (currentList.current != null) {
					numOfComp++;
					if (currentList.current.data == docId) {
						currentList.current = currentList.current.next;
					}
				}

			}

		}

		// reset all current pointers to head
		for (MyLinkedList l : L) {
			l.resetCurrentToHead();
		}

		// print out the result
		output.write("DaatOr");
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

	// performs document-at-at-time-And operation
	public static void daatAnd(String[] terms, Map<String, MyLinkedList> dictionary, Writer output, String line)
			throws Exception {
		List<MyLinkedList> L = new ArrayList<>(terms.length);
		int numOfComp = 0;
		MyLinkedList result = new MyLinkedList();
		for (String term : terms) {
			L.add(dictionary.get(term));
		}
		int docId = -1;
		boolean listfinished = false;
		while (!listfinished) {
			// get max in each column
			MyLinkedList maxList = null;
			for (MyLinkedList currentList : L) {
				if (currentList.current != null && currentList.current.data > docId) {
					docId = currentList.current.data;
					maxList = currentList;
				}
			}
			if (maxList == null) {
				break;
			}
			maxList.current = maxList.current.next;
			// numOfComp++;

			for (MyLinkedList currentList : L) {
				if (currentList == maxList) {
					continue;
				}
				// skip forward to docId
				numOfComp = numOfComp + currentList.skipForwardToDoc(docId);

				if (currentList.current != null) {
					if (currentList.current != null && currentList.current.data == docId) {
						numOfComp++;
						currentList.current = currentList.current.next;
						if (currentList.current == null) {
							listfinished = true;
							System.out.println("end of list reached");
						}

					} else {
						numOfComp++;
						docId = -1;
						break; // break out of for loop
					}
				} else {
					listfinished = true;
					docId = -1;
					break;
				}

			}
			if (docId > -1) {
				result.add(docId);
				System.out.println("doc Id added:" + docId);

			}

		}
		// reset all current pointers to head
		for (MyLinkedList l : L) {
			l.resetCurrentToHead();
		}

		// print out the result to output file
		output.write("DaatAnd");
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
