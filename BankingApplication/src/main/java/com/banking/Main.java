package com.banking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import com.banking.User.UserType;
import com.revature.dao.ApplicationDao;
import com.revature.dao.ApplicationDaoPostgres;
import com.revature.dao.BankAccountDao;
import com.revature.dao.BankAccountDaoPostgres;
import com.revature.dao.CustomerDao;
import com.revature.dao.CustomerDaoPostgres;
import com.revature.dao.EmployeeDao;
import com.revature.dao.EmployeeDaoPostgres;
import com.revature.dao.LogDao;
import com.revature.dao.LogDaoPostgres;

public class Main {

	public static boolean running = true;
	public static User currentUser = null;
	public static ArrayList<Customer> customerList;
	public static ArrayList<Employee> employeeList;
	public static ArrayList<BankAccount> accountList;
	public static ArrayList<Application> applicationList;
	
	public static EmployeeDao employeeDao = new EmployeeDaoPostgres();
	public static BankAccountDao accountDao = new BankAccountDaoPostgres();
	public static CustomerDao customerDao = new CustomerDaoPostgres();
	public static ApplicationDao applicationDao = new ApplicationDaoPostgres();
	public static LogDao logDao = new LogDaoPostgres();

	static String customerFile = "./BankingCustomers.txt";
	static String employeeFile = "./BankingEmployees.txt";
	static String accountFile = "./BankingAccounts.txt";
	static String applicationFile = "./BankingApplications.txt";

	static Scanner input = new Scanner(System.in);

	public static UserType getUserType() {
		System.out.println();
		System.out.println("Are you a customer or employee?");
		System.out.println("1. Customer");
		System.out.println("2. Employee");
		boolean validInput = false;

		while (!validInput) {

			String choice = input.nextLine();

			switch (choice) {
			case "quit":
				running = false;
				return null;
			case "1":
				return UserType.Customer;
			case "2":
				return UserType.Employee;
			default:
				System.out.print(
						"That is not a valid choice. Please enter 1 for customer or 2 for employee: ");
			}
		}
		System.out.println();
		return UserType.Customer;
	}

	public static <T extends User> boolean nameIsTaken(String name, ArrayList<T> list) {
		if (list == null || list.isEmpty())
			return false;

		for (User u : list) {
			if (u.getUsername().equals(name))
				return true;
		}

		return false;
	}

	public static Customer newCustomer() {

		String username = null, password, firstName, lastName, phoneNumber = null, address, email, ssn = null;
		int year = 0, month = 0, day = 0;

		// entries are assumed to be invalid until a valid entry is made
		boolean valid = false;

		// make sure that the username is not already taken
		while (!valid) {
			System.out.print("What username would you like for your account? ");
			username = input.nextLine();
			if (nameIsTaken(username, customerList)) {
				System.out.println("Sorry but that username has already been taken.");
			} else if (username.equals("quit")) {
				System.out.println("That is not a valid username.");
			} else
				valid = true;
		}
		// set valid to false after each entry
		valid = false;

		System.out.print("What password would you like for your account? ");
		password = input.nextLine();

		System.out.print("What is your first name? ");
		firstName = input.nextLine();

		System.out.print("What is your last name? ");
		lastName = input.nextLine();

		// make sure that entered phone number is a valid entry
		while (!valid) {
			System.out.print("What is your phone number? ");
			phoneNumber = (input.nextLine()).replaceAll("[^\\d.]", "");

			if (phoneNumber.length() != 10) {
				System.out.println("That is not a valid phone number, make sure it contains exactly 10 digits.");
			} else
				valid = true;
		}
		valid = false;

		System.out.print("What is your address? ");
		address = input.nextLine();

		System.out.print("What is your email? ");
		email = input.nextLine();

		// make sure that birth year is a valid year
		while (!valid) {
			System.out.print("What is your birth year? ");
			try {
				year = Integer.parseInt(input.nextLine());
			} catch (Exception e) {
				System.out.println("Please be sure to only input numbers for birth year.");
				continue;
			}

			if (year < 1900 || year > LocalDateTime.now().getYear())
				System.out.println("That is not a valid birth year.");
			else
				valid = true;
		}
		valid = false;

		// make sure that birth month is a valid month
		while (!valid) {
			System.out.print("What is your birth month as a number (ex. enter \"5\" for May)? ");
			try {
				month = Integer.parseInt(input.nextLine());
			} catch (Exception e) {
				System.out.println("Please be sure to only input numbers for birth month.");
				continue;
			}

			// month has to be between 1 and 12
			if (month < 1 || month > 12)
				System.out.println("That is not a valid month. Please enter a number from 1 to 12.");
			else
				valid = true;
		}
		valid = false;

		// make sure that birth day is a valid day of the month
		while (!valid) {
			System.out.print("What day of the month where you born? ");
			try {
				day = Integer.parseInt(input.nextLine());
			} catch (Exception e) {
				System.out.println("Please be sure to only input numbers for day of month.");
				continue;
			}

			if (day < 1 || day > 31)
				System.out.println("That is not a valid day of the month. Please enter a number from 1 to 31.");
			else
				valid = true;
		}
		valid = false;

		while (!valid) {
			System.out.print("What is your Social Security number (SSN)? ");
			ssn = (input.nextLine()).replaceAll("[^\\d.]", "");

			if (ssn.length() != 9)
				System.out.println("That is not a valid SSN. Make sure to enter exactly 9 digits");
			else
				valid = true;
		}

		LocalDate dob = LocalDate.of(year, month, day);

		Customer customer = new Customer(firstName, lastName, phoneNumber, address, email, dob, ssn, username, password);

		System.out.println("Your account has been created.");
		System.out.println();

		customerDao.create(customer);
		customerList.add(customer);
		writeObject(customerFile, customerList);

		return customer;
	}

	public static void main(String[] args) {

		// deserializes lists for customers, employees, admins, applications, and
		// accounts or creates new lists for them
		initializeLists();

		System.out.println(
				"Welcome to Jaron's banking application. Type in the number of the choice you would like or type \"back\" to return to the user selection or \"quit\" to end the application.");
		
		while (running) {
			while (currentUser == null) {
				UserType type = getUserType();

				if (type == null)
					break;

				if (type.equals(UserType.Customer)) {
					System.out.println("1. New customer");
					System.out.println("2. Existing customer");
					String choice = input.nextLine();

					switch (choice) {
					case "quit":
						running = false;
						break;
					case "back":
						break;
					case "1":
						currentUser = newCustomer();
						break;
					case "2":
						login(customerList);
						break;
					default:
						System.out.println(
								"Please enter 1 if you are a new customer or 2 if you are an existing customer.");
					}
				} else if (type.equals(UserType.Employee) || type.equals(UserType.Admin)) {
					System.out.println("1. New employee");
					System.out.println("2. New admin");
					System.out.println("3. Existing employee/admin");
					String choice = input.nextLine();

					switch (choice) {
					case "quit":
						running = false;
						break;
					case "back":
						break;
					case "1":
						newEmployee(UserType.Employee);
						break;
					case "2":
						newEmployee(UserType.Admin);
						break;
					case "3":
						login(employeeList);
						break;
					default:
						System.out.println(
								"Please enter 1 if you are a new employee, 2 if you are a new admin, or 3 if you are an existing employee/admin.");
						System.out.println(
								"You may also type \"back\" to go back to the user selection or \"quit\" to end the application.");
					}
				}
			}

			if (running) {
				currentUser.showOptions();
				System.out.println("You may log into a different account now or type \"quit\" to end the application.");
			}
		}
		System.out.println("Thank you for using Jaron's banking application.");
	}

	// Log in to account with username and password if existing user
	private static void newEmployee(UserType employeeType) {

		String username = null, password;

		boolean valid = false;

		// make sure that the username is not already taken
		while (!valid) {
			System.out.print("What username would you like for your account? ");
			username = input.nextLine();
			if (nameIsTaken(username, customerList)) {
				System.out.println("Sorry but that username has already been taken.");
			} else
				valid = true;
		}
		// set valid to false after each entry
		valid = false;

		System.out.print("What password would you like for your account? ");
		password = input.nextLine();

		Employee e = new Employee(username, password);

		e.setType(employeeType);
		employeeList.add(e);
		currentUser = e;
		writeObject(employeeFile, employeeList);
		employeeDao.create(e);
	}

	private static <T extends User> void login(ArrayList<T> userList) {

		System.out.println();

		boolean valid = false;

		// The user that is trying to log in
		User user = null;

		while (!valid) {
			System.out.print("What is your username? ");
			String username = input.nextLine();
			if (username.equals("quit"))
				return;
			for (User u : userList) {
				if (u.getUsername().equals(username)) {
					user = u;
					valid = true;
					break;
				}
			}
			if (!valid) {
				System.out.println(
						"That username does not exist. Please make sure you typed it correctly or type \"quit\".");
			}
		}

		valid = false;
		while (!valid && user != null) {
			System.out.print("What is your password? ");
			String password = input.nextLine();

			// if the user enters quit they return to select the user type.
			if (password.equals("quit")) {
				return;
			}

			if (user.getPassword().equals(password)) {
				currentUser = user;
				valid = true;
			} else
				System.out.println("That password is incorrect please try again or type \"quit\".");
		}
	}

	// Deserializes each list for customers, employees, admins, applications, and
	// accounts or creates new empty list
	
	private static void initializeLists() {
		
		//read from serialized files to assign array lists
		//customerList = (ArrayList<Customer>) readObject(customerFile);
		//employeeList = (ArrayList<Employee>) readObject(employeeFile);
		//applicationList = (ArrayList<Application>) readObject(applicationFile);
		//accountList = (ArrayList<BankAccount>) readObject(accountFile);
		
		//read from database to assign array lists
		employeeList = (ArrayList<Employee>)employeeDao.readAll();
		customerList = (ArrayList<Customer>)customerDao.readAll();
		accountList = (ArrayList<BankAccount>) accountDao.readAll();
		applicationList = (ArrayList<Application>)applicationDao.readAll();
		
		assignBankAccounts();
	}

	private static void assignBankAccounts() {

		// Checks each bank account and adds it to the correct customer list of accounts
		// if the username matches
		for (Customer customer : customerList) {
			//make sure customer has no current accounts before adding serialized accounts
			customer.clearAccounts();
			
			for (BankAccount account : accountList) {
				if (account.getCustomer().getUsername().equals(customer.getUsername())) {
					customer.addAccount(account);
				} else if (account.getJointCustomer() != null
						&& account.getJointCustomer().getUsername().equals(customer.getUsername()))
					customer.addAccount(account);
			}
		}
	}

	static <T> Object readObject(String filename) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			Object obj = ois.readObject(); // deserializing
			return obj;

		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}

		return new ArrayList<T>();
	}

	static void writeObject(String filename, Object obj) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {

			oos.writeObject(obj); // serialization - turning an object to bytes into text file

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static <T> void printList(ArrayList<T> list, String title) {
		System.out.println();
		System.out.println(title);
		for (Object obj : list)
			System.out.println(obj);
	}

}
