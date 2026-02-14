
import java.sql.*;
import java.util.Scanner;

public class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            while (true){
                System.out.print("Add book[Add], Checkout book[Checkout],Return book[Return], All books[Books], All people[People], Checkout history[History], All loaned books[loans], Quit[Quit]\n> ");
                String control = scanner.nextLine();
                if (control.equalsIgnoreCase("Add")){
                    System.out.print("Book title?\n> ");
                    String TheTitle = scanner.nextLine();
                    System.out.print("Author of the book?\n> ");
                    String TheAuthor = scanner.nextLine();
                    int bookId = SQL_Command.findbook(TheTitle);
                    if (bookId != -1) {
                        System.out.println("There is already a book called "+TheTitle+ " by " + TheAuthor+ " here.\n");
                    }
                    else {
                        System.out.print("Year of release?\n> ");
                        int year = scanner.nextInt();
                        System.out.print("How many copies of "+TheTitle+"?\n> ");
                        int copies = scanner.nextInt();
                        SQL_Command.addbook(TheTitle, TheAuthor, year, copies);
                        System.out.print("Added '"+TheTitle+"' by '"+TheAuthor+"'.");
                    }

                }
                else if (control.equalsIgnoreCase("Checkout")) {
                    System.out.print("First name?\n> ");
                    String Fname = scanner.nextLine();
                    System.out.print("Last name?\n> ");
                    String Lname = scanner.nextLine();
                    int personid = SQL_Command.findperson(Fname, Lname);

                    if (personid == -1) {
                        SQL_Command.addperson(Fname, Lname);
                        personid = SQL_Command.findperson(Fname, Lname);
                    }

                    System.out.print("Book title?\n> ");
                    String TheTitle = scanner.nextLine();
                    int bookId = SQL_Command.findbook(TheTitle);

                    if (bookId != -1) {
                        SQL_Command.CheckoutBook(personid, bookId);
                        System.out.println(Fname + " " + Lname + " checked out " + TheTitle);
                    } else {
                        System.out.println("Sorry, we don't have '" + TheTitle + "'.");
                    }
                }
                else if (control.equalsIgnoreCase("Return")) {
                    System.out.print("First name?\n> ");
                    String Fname = scanner.nextLine();
                    System.out.print("Last name?\n> ");
                    String Lname = scanner.nextLine();
                    int personid = SQL_Command.findperson(Fname, Lname);

                    if (personid != -1) {
                        System.out.print("Book title?\n> ");
                        String TheTitle = scanner.nextLine();
                        int bookId = SQL_Command.findbook(TheTitle);
                        if (bookId != -1) {
                            SQL_Command.returnBook(personid, bookId);
                            System.out.println("Success: " + TheTitle + " has been returned by " + Fname);
                            System.out.println("");
                        } else {
                            System.out.println("Error: Book title not recognized.");
                        }
                    }
                    else {
                        System.out.println("Error: User '" + Fname + " " + Lname + "' not found.");
                    }
                }
                else if (control.equalsIgnoreCase("Books")) {
                    SQL_Command.AllBooks();
                    System.out.println("");
                }
                else if (control.equalsIgnoreCase("People")) {
                    SQL_Command.AllPeople();
                    System.out.println("");
                }
                else if (control.equalsIgnoreCase("History")) {
                    SQL_Command.CheckoutHistory();
                    System.out.println("");
                }
                else if (control.equalsIgnoreCase("loans")) {
                    SQL_Command.Loaningout();
                }
                else if (control.equalsIgnoreCase("Quit")) {
                    break;
                }
                else {
                    System.out.println("Sorry, '" + control +"' isn't a command.");
                }
            }


        }
}