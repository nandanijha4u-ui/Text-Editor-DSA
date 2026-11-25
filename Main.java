
import java.util.*;

public class Main {

    // 1. Node of Linked List
    static class Node {
        char data;
        Node next;

        Node(char data) {
            this.data = data;
            this.next = null;
        }
    }

    // 2. Document using Linked List
    static class Document {
        Node head;
        int size = 0;

        // Insert text at a given position
        void insert(int position, String text) {
            if (position < 0 || position > size) {
                System.out.println("Invalid position!");
                return;
            }

            for (int i = 0; i < text.length(); i++) {
                Node newNode = new Node(text.charAt(i));

                if (position == 0) {
                    newNode.next = head;
                    head = newNode;
                } else {
                    Node temp = head;

                    for (int j = 0; j < position - 1; j++) {
                        temp = temp.next;
                    }

                    newNode.next = temp.next;
                    temp.next = newNode;
                }

                position++;
                size++;
            }
        }

        // Delete text from a given position
        String delete(int position, int length) {
            if (position < 0 || length <= 0 ||
                position > size || length > size - position) {
                return null;
            }

            StringBuilder deleted = new StringBuilder();

            for (int i = 0; i < length; i++) {
                if (position == 0) {
                    deleted.append(head.data);
                    head = head.next;
                } else {
                    Node temp = head;

                    for (int j = 0; j < position - 1; j++) {
                        temp = temp.next;
                    }

                    deleted.append(temp.next.data);
                    temp.next = temp.next.next;
                }

                size--;
            }

            return deleted.toString();
        }

        // Display the document
        public String toString() {
            StringBuilder result = new StringBuilder();
            Node temp = head;

            while (temp != null) {
                result.append(temp.data);
                temp = temp.next;
            }

            return result.toString();
        }
    }

    // 3. Edit information for Undo and Redo
    static class Edit {
        String type;
        int position;
        String text;

        Edit(String type, int position, String text) {
            this.type = type;
            this.position = position;
            this.text = text;
        }
    }

    // 4. Undo and Redo using Stacks
    static class History {
        Stack<Edit> undoStack = new Stack<>();
        Stack<Edit> redoStack = new Stack<>();

        void addEdit(Edit edit) {
            undoStack.push(edit);
            redoStack.clear();
        }

        void undo(Document doc) {
            if (undoStack.isEmpty()) {
                System.out.println("Nothing to undo!");
                return;
            }

            Edit edit = undoStack.pop();

            if (edit.type.equals("INSERT")) {
                doc.delete(edit.position, edit.text.length());
            } else {
                doc.insert(edit.position, edit.text);
            }

            redoStack.push(edit);
            System.out.println("Undo successful!");
        }

        void redo(Document doc) {
            if (redoStack.isEmpty()) {
                System.out.println("Nothing to redo!");
                return;
            }

            Edit edit = redoStack.pop();

            if (edit.type.equals("INSERT")) {
                doc.insert(edit.position, edit.text);
            } else {
                doc.delete(edit.position, edit.text.length());
            }

            undoStack.push(edit);
            System.out.println("Redo successful!");
        }
    }

    // 5. Searching and Word Frequency
    static class Analyzer {

        // Search text using Brute Force
        void search(String text, String word) {
            boolean found = false;

            for (int i = 0; i <= text.length() - word.length(); i++) {
                int j;

                for (j = 0; j < word.length(); j++) {
                    if (text.charAt(i + j) != word.charAt(j)) {
                        break;
                    }
                }

                if (j == word.length()) {
                    System.out.println("Word found at position " + i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Word not found!");
            }
        }

        // Count word frequency using HashMap
        void countWords(String text) {
            HashMap<String, Integer> map = new HashMap<>();

            String[] words = text.toLowerCase().split("[^a-z0-9]+");

            for (String word : words) {
                if (!word.isEmpty()) {
                    map.put(word, map.getOrDefault(word, 0) + 1);
                }
            }

            if (map.isEmpty()) {
                System.out.println("No words found!");
                return;
            }

            for (String word : map.keySet()) {
                System.out.println(word + " : " + map.get(word));
            }
        }
    }

    // Helper method to safely read integers
    static int readInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    // 6. Main Program
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Document doc = new Document();
        History history = new History();
        Analyzer analyzer = new Analyzer();

        int choice = 0;

        do {
            System.out.println("\n===== SIMPLE TEXT EDITOR =====");
            System.out.println("1. Insert Text");
            System.out.println("2. Delete Text");
            System.out.println("3. Undo");
            System.out.println("4. Redo");
            System.out.println("5. Search Word");
            System.out.println("6. Word Frequency");
            System.out.println("7. Display Document");
            System.out.println("8. Exit");

            choice = readInt(sc, "Enter your choice: ");

            switch (choice) {

                case 1:
                    int pos = readInt(sc, "Enter position: ");

                    System.out.print("Enter text: ");
                    String text = sc.nextLine();

                    if (text.isEmpty()) {
                        System.out.println("Text cannot be empty!");
                        break;
                    }

                    if (pos < 0 || pos > doc.size) {
                        System.out.println("Invalid position!");
                        break;
                    }

                    doc.insert(pos, text);
                    history.addEdit(new Edit("INSERT", pos, text));
                    System.out.println("Text inserted!");
                    break;

                case 2:
                    int start = readInt(sc, "Enter starting position: ");
                    int length = readInt(sc, "Enter number of characters: ");

                    String deleted = doc.delete(start, length);

                    if (deleted == null) {
                        System.out.println("Invalid position or length!");
                    } else {
                        history.addEdit(new Edit("DELETE", start, deleted));
                        System.out.println("Deleted: " + deleted);
                    }
                    break;

                case 3:
                    history.undo(doc);
                    break;

                case 4:
                    history.redo(doc);
                    break;

                case 5:
                    System.out.print("Enter word to search: ");
                    String word = sc.nextLine();

                    if (word.isEmpty()) {
                        System.out.println("Search word cannot be empty!");
                    } else {
                        analyzer.search(doc.toString(), word);
                    }
                    break;

                case 6:
                    analyzer.countWords(doc.toString());
                    break;

                case 7:
                    System.out.println("Document: " + doc);
                    break;

                case 8:
                    System.out.println("Thank you for using Text Editor!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 8);

        sc.close();
    }
}
