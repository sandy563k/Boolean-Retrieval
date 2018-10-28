package com.boolRetrieval.invertedIndex;

import java.io.Writer;

// Custom linked list to store the postings list
public class MyLinkedList {
	Node head;
	Node tail;
	Node current;
	int size;

	//adds doc with given id to linkedlist
	public void add(int data) {
		Node d = new Node(data);
		if (size == 0) {
			head = d;
			tail = d;
			current = head;
		} else {
			tail.next = d;
			tail = d;
		}
		size++;
	}

	//skips the current pointer to point to given doc id
	public int skipForwardToDoc(int docId) {
		int numOfComp = 0;
		while (this.current != null && this.current.hasSkip() && this.current.skip.data < docId) {
			numOfComp++;
			this.current = this.current.skip;
		}
		// move current to point to docId
		while (this.current != null && this.current.data < docId) {
			numOfComp++;
			this.current = this.current.next;
		}
		return numOfComp;
	}

	//resets current pointer to point to head
	public void resetCurrentToHead() {
		this.current = head;
	}

	//prints out the posting list to output file
	public void printList(Writer output) throws Exception {
		if (this.size == 0) {
			output.write(" empty");
			output.write("\r\n");
			return;
		}
		Node current = head;
		while (current != null) {
			output.write(" " + current.data);
			current = current.next;
		}
		output.write("\r\n");
	}

	public void printList() {
		Node current = head;
		while (current != null) {
			System.out.print(current.data + " ");
			current = current.next;
		}
		System.out.println("");
	}

	// generates skip pointers for the given linkedlist
	public void generateSkipPointers() {
		int skipLength = (int) Math.sqrt(this.size);
		Node currentSkip = head;
		Node runningPtr = head;
		int count = 0;

		while (runningPtr != null) {
			if (count == skipLength) {
				count = 0;
				currentSkip.skip = runningPtr;
				currentSkip = runningPtr;
			}
			count++;
			runningPtr = runningPtr.next;
		}

	}

}

//defines Node object for the linkedList
class Node {
	int data;
	Node next;
	Node skip;

	Node(int d) {
		data = d;
		next = null;
		skip = null;
	}

	boolean hasSkip() {
		if (this.skip != null) {
			return true;
		}
		return false;
	}
}