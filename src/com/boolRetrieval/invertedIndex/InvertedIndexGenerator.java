package com.boolRetrieval.invertedIndex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

// Creates inverted index, reads boolean queries from input file ,performs term-at-a-time(taat), document-at-a-time(daat) operations ,writes results to output file
public class InvertedIndexGenerator {
	public static Map<String, MyLinkedList> dictionary;

	public static void main(String[] args) {
		try {
			FileSystem fs = FileSystems.getDefault();
			Path path_of_index = fs.getPath(args[0]);
			createInvertedIndex(path_of_index);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(args[2]), "UTF8"));
			Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF8"));
			String line;
			while ((line = br.readLine()) != null) {
				String terms[] = line.split("\\s+");
				getPostings(terms, dictionary, output);
				TAAT.taatAnd(terms, dictionary, output, line);
				TAAT.taatOr(terms, dictionary, output, line);
				DAAT.daatAnd(terms, dictionary, output, line);
				DAAT.daatOr(terms, dictionary, output, line);
			}
			br.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// generates inverted index by reading the given index files
	public static IndexReader createInvertedIndex(Path p) {
		dictionary = new HashMap<>();
		try {
			Directory indexDirectory = FSDirectory.open(p);
			IndexReader reader = DirectoryReader.open(indexDirectory);
			Fields fields = MultiFields.getFields(reader);
			for (String field : fields) {
				System.out.println("field:" + field);
				Terms terms = fields.terms(field);
				TermsEnum termsEnum = terms.iterator();
				while (termsEnum.next() != null) {
					BytesRef term = termsEnum.term();
					PostingsEnum postings = null;
					postings = termsEnum.postings(null, PostingsEnum.ALL);
					MyLinkedList postinglist = new MyLinkedList();
					int docId = postings.nextDoc();
					while (docId != PostingsEnum.NO_MORE_DOCS) {

						postinglist.add(docId);
						docId = postings.nextDoc();

					}
					postinglist.generateSkipPointers();
					dictionary.put(term.utf8ToString(), postinglist);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// prints out postings list for the given terms
	public static void getPostings(String[] terms, Map<String, MyLinkedList> dictionary, Writer output)
			throws Exception {

		for (int i = 0; i < terms.length; i++) {
			output.write("GetPostings");
			output.write("\r\n");
			output.write(terms[i]);
			output.write("\r\n");
			output.write("Postings list:");
			dictionary.get(terms[i]).printList(output);
		}

	}

}
