import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        // Required objects creation
        DBConnection dbc = new DBConnection();
        Connection conn = dbc.getConnection();
        DAO dao = new DAO();
        Scanner sc = new Scanner(System.in);

        // login system
        boolean loginNotAccepted = true;

        // value error string
        String inputValueError = "Wrong input! :(   Try Again!";

        String username = null;
        String password = null;
        boolean isCompanyLogin = false; // VERY IMPORTANT --> DECIDES WHETHER UPDATE TABLE DETAILS IS POSSIBLE OR NOT!

        System.out.println("\n1. Company Login\n2. Customer login");
        int userLoginChoice = sc.nextInt();

        if (userLoginChoice == 1)
            isCompanyLogin = true;

        if (!isCompanyLogin) {
            System.out.println("\n1. Signin\n2. Signup");
            int signingChoice = sc.nextInt();
            if (signingChoice == 2) {
                System.out.println("\nEnter New Username :");
                String newUserName = sc.next();
                System.out.println("Enter Password :");
                String newPassword = sc.next();
                dao.createUser(conn, newUserName, newPassword);
                System.out.println("User Successfully Created! :)");
            }
        }
        Map<String, String> loginDetails = dao.getLoginDetails(conn, isCompanyLogin);
        while (loginNotAccepted) {
            System.out.println("\n\nEnter Username: ");
            username = sc.next();
            System.out.println("Enter Password: ");
            password = sc.next();
            if (loginDetails.containsKey(username) && loginDetails.get(username).equals(password))
                loginNotAccepted = false;
            else
                System.out.println("\nUsername or Password is Wrong! Please try again!!");
        }

        // All required result data holders declared
        List<String> tableList;
        List<String> columnList;
        Map<String, Object> values = null;
        String specificValue;

        // variable to run the application infinitely
        boolean runApplication = true;

        System.out.println("\nHello " + username + "  !!!");

        while (runApplication) {
            System.out.println(
                    "What type of robots you wanna look into?\n1. Seed Sowing Robots\n2. Weed Removing Robots\n3. Harvesting Robots");

            int reqRobotId;
            boolean getInputAgain;
            do {
                reqRobotId = sc.nextInt();
                getInputAgain = reqRobotId < 0 || reqRobotId >= 4;
                if (getInputAgain) {
                    System.out.println(inputValueError);
                }
            } while (getInputAgain);

            System.out.println("What details you wanna see?"); // Selecting Table
            tableList = dao.getTableList(conn);
            for (int i = 1; i < tableList.size(); i++)
                System.out.println(i + ". " + tableList.get(i - 1));

            int reqTableIndex;
            do {
                reqTableIndex = sc.nextInt();
                getInputAgain = reqTableIndex < 0 || reqTableIndex >= tableList.size();
                if (getInputAgain) {
                    System.out.println(inputValueError);
                }
            } while (getInputAgain);

            System.out.println("Select an option:"); // Selecting Column
            columnList = dao.getColumnList(conn, tableList.get(reqTableIndex - 1));
            for (int i = 1; i < columnList.size(); i++)
                System.out.println(i + ". " + columnList.get(i));
            System.out.println(columnList.size() + ". " + "All Of The Above");

            int reqColumnIndex;
            do {
                reqColumnIndex = sc.nextInt();
                getInputAgain = reqColumnIndex < 0 || reqColumnIndex > columnList.size();
                if (getInputAgain) {
                    System.out.println(inputValueError);
                }
            } while (getInputAgain);

            if (reqColumnIndex == columnList.size()) { // If all the details are required
                values = dao.getValues(conn, tableList.get(reqTableIndex - 1), reqRobotId);

                Set<String> keys = values.keySet();
                Iterator<String> itr = keys.iterator();
                System.out.println(
                        "\n------------------------------------------\nDetails are :\n");
                int num = 1;
                while (itr.hasNext()) {
                    Object i = itr.next();
                    System.out.println((num++) + ". " + i + " : " + values.get(i));
                }
                System.out.println("------------------------------------------");

            } else {
                specificValue = dao.getSpecificValue(conn, tableList.get(reqTableIndex - 1), reqRobotId,
                        columnList.get(reqColumnIndex));
                System.out.println(specificValue);
            }

            if (isCompanyLogin) { // update table logic
                String wantToUpdate;
                System.out.println("\n\nDo you want to update any detail? (y/n)");
                wantToUpdate = sc.next();
                if (wantToUpdate.equals("y")) {
                    System.out
                            .println(
                                    "Enter the space separated column numbers that you want to update : (Eg. 1 2 3)\nNote: You cannot update id");

                    sc.nextLine();
                    String[] updateWishListString = sc.nextLine().split(" ");
                    int[] updateWishListIntegers = new int[updateWishListString.length];

                    for (int i = 0; i < updateWishListString.length; i++)
                        updateWishListIntegers[i] = Integer.parseInt(updateWishListString[i]);

                    dao.updateValues(conn, tableList.get(reqTableIndex - 1), reqRobotId,
                            updateWishListIntegers);
                    values = dao.getValues(conn, tableList.get(reqTableIndex - 1), reqRobotId);

                    Set<String> keys = values.keySet();
                    Iterator<String> itr = keys.iterator();
                    System.out.println(
                            "\n------------------------------------------\nUpdated Details are :\n");
                    int num = 1;
                    while (itr.hasNext()) {
                        Object i = itr.next();
                        System.out.println((num++) + ". " + i + " : " + values.get(i));
                    }
                    System.out.println("------------------------------------------");
                    System.out.println("\nThank you for using our app! :))))");
                }
                runApplication = false;
            }

            // sc.nextLine();
            if (!isCompanyLogin) {
                String isExit = "n";
                System.out.println("\n\nDo you want to exit? (y/n):");
                // sc.nextLine();
                isExit = sc.next();
                if (isExit.equals("y")) {
                    runApplication = false;
                    System.out.println("Thank you for using our app! :))))");
                }

            }
        }

        sc.close();
    }

}
