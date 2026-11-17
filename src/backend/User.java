package backend;

public abstract class User {
	
	private String username;
	private String password;
	
	// log in takes in username and password then checks
	
	boolean logIn(String username, String password) {
		if (password.equals(this.password) && username.equals(this.username)) {
			System.out.printf("Successfully logged in!");
			return true;

		} else {
			System.out.printf("Could not log in. Try again!");
			return false;
		}
	}
	

}
