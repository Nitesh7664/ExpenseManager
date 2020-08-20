
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/*
 * The problem statement
Write a command-line program to track your daily expenses and give you a weekly report.
- You should be able to add an expense with name and amount.
- You should be able to reports for the "n" number of weeks in the past.
- The reports should include the week duration, daily totals, and weekly totals.
- Allow users to set a warning on a certain expense amount per week. User should be warned 
when the expenses for the current week goes over this amount.
 */

public class ExpenseManager {

   static Week[] weeks;
   static Calendar calendar = Calendar.getInstance();
   static String startDayOfWeek;
   static int startingWeekNumber;

   public ExpenseManager() {
      weeks = new Week[53];

      startDayOfWeek = LocalDate.now().getDayOfWeek().name();
      calendar.setFirstDayOfWeek(getDayNumberFromDayName(startDayOfWeek));
      startingWeekNumber = calendar.get(calendar.WEEK_OF_YEAR);
   }

   static class Week {
      Day[] weekDays;
      double weekBudget;
      double weekTotal;
      String startDate;
      String lastDate;

      public Week() {
         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

         weekDays = new Day[7];
         weekTotal = 0;
         weekBudget = Double.MAX_VALUE;
         startDate = LocalDate.now().toString();
         Date currentDate = new Date();
         Calendar c = Calendar.getInstance();
         c.setTime(currentDate);
         c.add(Calendar.DATE, 6);
         lastDate = dateFormat.format(c.getTime()).toString();
      }

      public Week(double budget) {
         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

         weekDays = new Day[7];
         weekTotal = 0;
         weekBudget = budget;
         startDate = LocalDate.now().toString();
         Date currentDate = new Date();
         Calendar c = Calendar.getInstance();
         c.setTime(currentDate);
         c.add(Calendar.DATE, 6);
         lastDate = dateFormat.format(c.getTime()).toString();
      }

      class Day {
         List<Expense> expenses;
         double dailyTotal;

         public Day() {
            expenses = new ArrayList<>();
            dailyTotal = 0;
         }

         class Expense {
            String name;
            double amount;

            public Expense(String expenseName, double expenseAmount) {
               this.name = expenseName;
               this.amount = expenseAmount;
            }
         }

         public void addExpense(String expenseName, double expenseAmount) {
            Expense newExpense = new Expense(expenseName, expenseAmount);
            this.expenses.add(newExpense);
            this.dailyTotal += expenseAmount;
         }
      }

      public int getDayNumberFromDayName(String dayName) {
         String[] weekDays = new String[] { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
         int totalDaysInWeek = 7;

         int startDayIndex = 0;
         for (startDayIndex = 0; startDayIndex < weekDays.length; startDayIndex++) {
            if (weekDays[startDayIndex].equals(startDayOfWeek))
               break;
         }
         int index = 0;
         for (index = 0; index < 7; index++) {
            if (weekDays[(startDayIndex + index) % totalDaysInWeek].equals(dayName))
               break;
         }
         return index;
      }

      public void addExpense(String expenseName, double expenseAmount) {
         String currentDay = LocalDate.now().getDayOfWeek().name();

         Scanner sc = new Scanner(System.in);

         int index = getDayNumberFromDayName(currentDay);

         if (weekDays[index] == null)
            weekDays[index] = new Day();

         if (this.weekTotal + expenseAmount > this.weekBudget) {
            System.out.println();
            System.out.println("***********************WARNING************************");
            System.out.println("Monthly budget " + this.weekBudget + " has exceeded");
            System.out.println("Current Week Total : " + this.weekTotal);
            System.out.println("Current Week Total After adding this Expense : " + (this.weekTotal + expenseAmount));
            System.out.println("*****************************************************");
            System.out.println();

            System.out.println("Do You Still want to add this Expense");
            System.out.println("Press 1 to enter this expense");
            System.out.println("Press 0 to discard this expense");
            try {
               int option = Integer.parseInt(sc.nextLine());
               if (option == 1) {
                  weekDays[index].addExpense(expenseName, expenseAmount);
                  System.out.println("Expense added succesfully");
               } else
                  System.out.println("Expense " + expenseName + " discarded successfully");
            } catch (NumberFormatException e) {
               System.out.println("Please Enter a Numeric Value");
            }

         } else {
            weekDays[index].addExpense(expenseName, expenseAmount);
            System.out.println("Expense added succesfully");
         }
         this.weekTotal += expenseAmount;

      }
   }

   public static void getDetailsOfNWeek(int n) {

      int currentWeekNumber = calendar.get(calendar.WEEK_OF_YEAR) - startingWeekNumber;

      if (currentWeekNumber - n + 1 >= 0) {
         System.out.println("Week reports of " + n + " week is :-");
      } else {
         System.out.println("Only " + (currentWeekNumber + 1) + " weeks data is available");
      }
      for (int i = Math.max(currentWeekNumber - n, 0); i <= currentWeekNumber; i++) {
         Week week = weeks[i];
         if (week == null) {
            System.out.println();
            System.out.println("Week Number : " + (i + 1));
            System.out.println("Week Total : " + 0.0);
            System.out.println("Start Date: N.A.");
            System.out.println("Last Date : N.A.");
            System.out.println();
            continue;
         }
         System.out.println();
         System.out.println("Week Number : " + (i + 1));
         System.out.println("Week Total : " + week.weekTotal);
         System.out.println("Start Date: " + week.startDate);
         System.out.println("Last Date : " + week.lastDate);

         System.out.println();
         System.out.println("Daily expenses List");

         for (int j = 0; j < week.weekDays.length; j++) {
            if (week.weekDays[j] == null) {
               System.out.println("Day " + (j + 1) + " : " + 0.0);
            } else {
               System.out.println("Day " + (j + 1) + " : " + week.weekDays[j].dailyTotal);
            }
         }
         System.out.println();
      }

   }

   public static void addExpense(String expenseName, double expenseAmount) {
      int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR) - startingWeekNumber;

      Scanner sc = new Scanner(System.in);

      if (weeks[weekNumber] == null) {
         System.out.println("It's a New week, Do you want to set a budget for this week");
         System.out.println("press 1 if yes");
         System.out.println("press 2 if No");
         try {
            int option = Integer.parseInt(sc.nextLine());
            if (option == 1) {
               System.out.println("Enter this weeks Budget");
               double budget = Double.parseDouble(sc.nextLine());
               weeks[weekNumber] = new Week(budget);
               System.out.println("Budget added successfully");
            } else {
               weeks[weekNumber] = new Week();
            }
            Week currentWeek = weeks[weekNumber];
            currentWeek.addExpense(expenseName, expenseAmount);
         } catch (NumberFormatException e) {
            System.out.println("Please Enter a Numeric Value");
         }
      } else {
         Week currentWeek = weeks[weekNumber];
         currentWeek.addExpense(expenseName, expenseAmount);
      }

   }

   public int getDayNumberFromDayName(String dayName) {
      switch (dayName) {
         case "SUNDAY":
            return 1;
         case "MONDAY":
            return 2;
         case "TUESDAY":
            return 3;
         case "WEDNESDAY":
            return 4;
         case "THURSDAY":
            return 5;
         case "FRIDAY":
            return 6;
         case "SATURDAY":
            return 7;
         default:
            return 1;
      }
   }

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      ExpenseManager expenseManager = new ExpenseManager();

      boolean status = true;
      while (status) {
         System.out.println();
         System.out.println("#############################");
         System.out.println("Choose from below option what action you want to perform");
         System.out.println("1. Add an Expense");
         System.out.println("2. Get details of N week");
         System.out.println("3. Close the Expense Manager");
         System.out.println();
         try {
            int option = Integer.parseInt(sc.nextLine());
            if (option == 1) {
               System.out.println("Name of the Expense ");
               String expenseName = sc.nextLine().trim();
               System.out.println("Enter Expense Amount");
               double expenseAmount = Double.parseDouble(sc.nextLine());
               expenseManager.addExpense(expenseName, Math.abs(expenseAmount));
               System.out.println();
            } else if (option == 2) {
               System.out.println("Enter Number of weeks");
               int n = Integer.parseInt(sc.nextLine());
               expenseManager.getDetailsOfNWeek(n);
               System.out.println();
            } else if (option == 3) {
               status = false;
            } else {
               System.out.println("Choose your option carefully");
               System.out.println();
            }
         } catch (NumberFormatException e) {
            System.out.println("Please Enter a Numeric Value");
         }
      }
   }

}
