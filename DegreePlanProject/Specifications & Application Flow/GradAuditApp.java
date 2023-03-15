/*  A program to help with grad file audits.

    By: L. Thompson
*/
 
import java.util.Scanner;

public class GradAuditApp
{
	static final int MAX_CORE_GRADES = 5;
	static final int MAX_ELECT_GRADES = 7;
	static final int MAX_OTHER_GRADES = 10;
	static final double MIN_CORE_GPA = 3.19;
	static final double MIN_ELECT_GPA = 3.0;
	static final double MIN_OVRALL_GPA = MIN_ELECT_GPA;
    static final double A_GRADEPTS = 4.000;
    static final double A_MINUS_GRADEPTS = 3.670;
    static final double B_PLUS_GRADEPTS = 3.330;
    static final double B_GRADEPTS = 3.000;
    static final double B_MINUS_GRADEPTS = 2.670;
    static final double C_PLUS_GRADEPTS = 2.330;
    static final double C_GRADEPTS = 2.000;
    static final double F_GRADEPTS = 0.000;
    static final double P_GRADEPTS = 0.000;
    
    static Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args)
	{
		String[] coreGrades = new String[MAX_CORE_GRADES];
		String[] electiveGrades = new String[MAX_ELECT_GRADES];
		String[] otherGradGrades = new String[MAX_OTHER_GRADES];
		String grade;	
        
        while (true)
        {	
           	int numCoreGrades = 0;		
    		int numElectiveGrades = 0;
    		int numOtherGradGrades = 0;
    		int allCount = 0;
            int numCorePs = 0, numElectPs = 0, numOtherPs = 0;
            
            System.out.println("\n");    
    		numCoreGrades = getCoreGrades(coreGrades);
            
            // Call function to count Pass grades in core
            
            numCorePs = countPasses(coreGrades, numCoreGrades);
            
            int nonPCore = numCoreGrades - numCorePs;                                         
                   
            allCount += numCoreGrades; 
            
            System.out.println();         
            numElectiveGrades = getElectiveGrades(electiveGrades);
            
            // Call function to count Pass grades in electives
            
            numElectPs = countPasses(electiveGrades, numElectiveGrades);
            
            int nonPElect = numElectiveGrades - numElectPs;            
        
            allCount += numElectiveGrades;
            System.out.println();
            numOtherGradGrades = getOtherGradGrades(otherGradGrades);
            
            // Call function to count Pass grades in other grad couses
            
            numOtherPs = countPasses(otherGradGrades, numOtherGradGrades);
            
            int nonPOther = numOtherGradGrades - numOtherPs;
            
            allCount += numOtherGradGrades;
            
            // Print totals as check
                    		
    		System.out.println("\nThe total number of grades entered was: " + allCount);
            System.out.println("The number of core grades entered was: " + numCoreGrades);
    		System.out.println("The number of elective grades entered was: " + numElectiveGrades);
    		System.out.println("The number of other graduate grades entered was: " + numOtherGradGrades);
            
    		if (numCoreGrades > 0)
    		{
    			System.out.println("\nThe core grades entered were: ");
    			displayGrades(coreGrades, numCoreGrades);
    		}
    		
            if (numElectiveGrades > 0)
            {		
    		    System.out.println("\nThe elective grades entered were: ");
                displayGrades(electiveGrades, numElectiveGrades);
            }
            
            if (numOtherGradGrades > 0)
            {		
    		    System.out.println("\nThe other graduate grades entered were: ");
                displayGrades(otherGradGrades, numOtherGradGrades);
            }
    
            double coreTotal = 0.0, overallTotal = 0;
    		
    		for (int count = 0; count < numCoreGrades; count++)
    		{
    			coreTotal += getGradePoints(coreGrades[count]);
    		}
    		
            overallTotal += coreTotal; 
            
            // Calculate and display Core GPA
            
            double coreAvg = 2.000;
            
            if (nonPCore > 0)
            {
    		    coreAvg = coreTotal / nonPCore;
            }
            
    		System.out.printf("\nThe core GPA is %.3f.\n", coreAvg);
    		
    		double electiveTotal = 0.0;
    		
    		for (int count = 0; count < numElectiveGrades; count++)
    		{
    			electiveTotal += getGradePoints(electiveGrades[count]);
    		}
    		
            overallTotal += electiveTotal;
            
            // Calculate and display elective GPA
            
    		double electiveAvg = 2.000;
            
            if (nonPElect > 0)
            {
                electiveAvg = electiveTotal / nonPElect;
            }
            		
    		System.out.printf("The elective GPA is %.3f.\n", electiveAvg);
    
    		double otherTotal = 0.0;
    		
    		for (int count = 0; count < numOtherGradGrades; count++)
    		{
    			otherTotal += getGradePoints(otherGradGrades[count]);
    		}
    		
            overallTotal += otherTotal;
            
            // Calculate and display overall GPA
            
    		double overallAvg = 2.000;
            
            int totalPs = numCorePs + numElectPs + numOtherPs;
            
            if (allCount > totalPs)
            {   
                overallAvg = overallTotal / (allCount - totalPs);
            }
            		
    		System.out.printf("The overall GPA is %.3f.\n", overallAvg);
            
       		int remainingCore = MAX_CORE_GRADES - numCoreGrades; 
            String letterGradeNeeded = "C";           
                        
            if (remainingCore > 0)
            {
                double pointsNeeded = calcGradePointsNeeded (MIN_CORE_GPA, MAX_CORE_GRADES - numCorePs , coreTotal);
                double GPANeededFor3_2Core = pointsNeeded / remainingCore;
                
                pointsNeeded = calcGradePointsNeeded (MIN_ELECT_GPA, (MAX_CORE_GRADES - numCorePs) , coreTotal);
                
                double GPANeededFor3_0Core = pointsNeeded / remainingCore;
                
                if (1 == remainingCore)
                {    
                    if (GPANeededFor3_2Core > 2.000)
                    {                                                      
                        letterGradeNeeded = getGradeNeeded(GPANeededFor3_2Core);                   
                        System.out.printf("\nTo maintain a 3.19 core GPA:\n\tThe student needs a grade of %s in the remaining " 
                            + "core course.\n", letterGradeNeeded);
                    }
                    else
                    {
                        System.out.printf("\nTo maintain a 3.19 core GPA:\n\tThe student must pass the remaining " 
                            + "core course.\n"); 
                    } 
                                        
                    if (GPANeededFor3_0Core > 2.000)
                    {                                                      
                        letterGradeNeeded = getGradeNeeded(GPANeededFor3_0Core);                   
                        System.out.printf("\nTo maintain a 3.19 core GPA:\n\tThe student needs a grade of %s in the remaining " 
                            + "core course.\n", letterGradeNeeded);
                    }
                    else
                    {
                        System.out.printf("\nTo maintain a 3.0 core GPA:\n\tThe student must pass the remaining " 
                            + "core course.\n"); 
                    }        
                } // End if only 1 core remains
                else // remainingCore > 1
                {   
                    if (GPANeededFor3_2Core > 2.000)
                    {                 
                        System.out.printf("\nTo maintain a 3.19 core GPA:\n\tThe student needs a GPA of %.3f in the remaining " 
                            + "%d core courses.\n", GPANeededFor3_2Core, remainingCore);
                    }
                    else 
                    {
                       System.out.printf("\nTo maintain a 3.19 core GPA:\n\tThe student must pass the remaining " 
                            + "%d core courses.\n", remainingCore);
                    }
                    
                    if (GPANeededFor3_0Core > 2.000)
                    {                        
                        System.out.printf("\nTo maintain a 3.0 core GPA:\n\tThe student needs a GPA of %.3f in the remaining " 
                            + "%d core courses.\n", GPANeededFor3_0Core, remainingCore);
                    }
                    else
                    {
                        System.out.printf("\nTo maintain a 3.0 core GPA:\n\tThe student must pass the remaining " 
                            + "%d core courses.\n", remainingCore);  
                    }
                }
            } // End if there are remaining core            
                        
            int totalElectives = MAX_ELECT_GRADES - 1;
            
            if (numElectiveGrades < MAX_ELECT_GRADES)
            {
        		System.out.println("\nWill the student be taking an extra elective?");
                System.out.print("Enter Y for Yes or N for No: ");
        		String extraElectNeeded = keyboard.nextLine();
                
                while ( !extraElectNeeded.equalsIgnoreCase("Y") &&  !extraElectNeeded.equalsIgnoreCase("N"))
        		{
        			System.out.println("\nERROR, invalid response.");
        			System.out.println("Will the student be taking an extra elective?");
                    System.out.print("Enter Y for Yes or N for No: ");
            		extraElectNeeded = keyboard.nextLine();        
        		}
        		
                if (extraElectNeeded.equalsIgnoreCase("Y"))
                {
                    totalElectives = MAX_ELECT_GRADES;
                }         
            }
            
            int remainingElectives = totalElectives - numElectiveGrades;                
                  
    		if (remainingElectives > 0)
    		{
                double electGradePtsNeeded = calcGradePointsNeeded (MIN_ELECT_GPA, (remainingElectives + nonPElect), electiveTotal);
                
    			double GPANeededInElectives = electGradePtsNeeded / remainingElectives;
                
                if (1 == remainingElectives)
                {    
                    if (GPANeededInElectives > 2.000)
                    {
                        letterGradeNeeded = getGradeNeeded(GPANeededInElectives);   
                                           
                        System.out.printf("\nTo maintain a 3.0 elective GPA:\n\tThe student needs a grade of %s in the remaining " 
                            + "elective course.\n", letterGradeNeeded);
                    }
                    else
                    {
                        System.out.printf("\nTo maintain a 3.0 elective GPA:\n\tThe student must pass the remaining " 
                            + "elective course.\n");    
                    }             
                        
                } // End if exactly 1 
                else // remainingElectives > 1
                { 
                    if (GPANeededInElectives > 2.000)
                    {                         
                        System.out.printf("\nTo maintain a 3.0 elective GPA:\n\tThe student needs a GPA of %.3f in the remaining " 
                            + "%d elective courses.\n", GPANeededInElectives, remainingElectives);
                    }
                    else
                    {
                        System.out.printf("\nTo maintain a 3.0 elective GPA:\n\tThe student must pass the remaining " 
                            + "%d elective courses.\n", remainingElectives);   
                    }                 
     
                } // End if more than 1 remaining elective
           	}           
        
            System.out.println("\nWill the student be taking any more extra graduate courses?");
            System.out.print("Enter Y for Yes or N for No: ");
    		String moreExtra = keyboard.nextLine();
            
            while ( !moreExtra.equalsIgnoreCase("Y") &&  !moreExtra.equalsIgnoreCase("N"))
    		{
    			System.out.println("\nERROR, invalid response.");
    			System.out.println("Will the student be taking any more extra graduate courses?");
                System.out.print("Enter Y for Yes or N for No: ");
    		    moreExtra = keyboard.nextLine();     
    		}
    
            int remainingExtra = 0;
            
            if (moreExtra.equalsIgnoreCase("Y"))
            {
                System.out.print("How many more extra graduate courses will the student be taking? ");
                remainingExtra = keyboard.nextInt();
        		
        		while (remainingExtra < 0 || remainingExtra > (MAX_OTHER_GRADES - numOtherGradGrades))
        		{
        			System.out.println("\nERROR, invalid number entered.");
                    System.out.print("How many more extra graduate courses will the student be taking? ");
                    remainingExtra = keyboard.nextInt();
                }
        		
    		    keyboard.nextLine();
            }
            
            int remainingCourses = remainingCore + remainingElectives + remainingExtra;
            int nonPassGrades = nonPCore + nonPElect + nonPOther;
    
    		if ((remainingCourses) > 0)
    	    {           
                double gradePtsInRemCourses = calcGradePointsNeeded (MIN_OVRALL_GPA, (remainingCourses + nonPassGrades), overallTotal);
    			double GPANeededInRemCourses = gradePtsInRemCourses / remainingCourses;
                
                if (1 == remainingCourses)
                {                    
                    if (GPANeededInRemCourses > 2.000)
                    {
                        letterGradeNeeded = getGradeNeeded(GPANeededInRemCourses);   
                                           
                        System.out.printf("\nTo maintain a 3.0 overall GPA:\n\tThe student needs a grade of %s in the remaining " 
                            + "course.\n", letterGradeNeeded);
                    }
                    else
                    {
                        System.out.printf("\nTo maintain a 3.0 overall GPA:\n\tThe student must pass the remaining " 
                            + "course.\n");    
                    }
                }
                else
                {
                    if (GPANeededInRemCourses > 2.000)
                    {
                         System.out.printf("\nTo maintain a 3.0 overall GPA:\n\tthe student needs a GPA of %.3f in their remaining courses.\n", 
                            GPANeededInRemCourses);
                    }
                    else
                    {
                          System.out.printf("\nTo maintain a 3.0 overall GPA:\n\tthe student must pass their remaining courses.\n"); 
                    }
                } 
    		} // End if remainingCourses > 0
        } // End while
        
	} // End main method
	
    
    /** A method that gets the letter grades earned by a student in their core
        classes. 
        @param coreGrades The array that stores the letter grades as Strings
        @return The number of letter grades put in the array
    */
    
    public static int getCoreGrades(String[] coreGrades)
	{
		int count = 0;
		String grade;
		
        do
        {
            System.out.print("Enter core grade #" + (count + 1) + " or X to eXit core entry: ");
		    grade = keyboard.nextLine();
            
            while ( !grade.equalsIgnoreCase("X") && !validGrade(grade))
			{
				System.out.println("\nError, " + grade + " is not a valid grade.");		
				System.out.print("Enter core grade #" + (count + 1) + " or X to eXit core entry: ");
				grade = keyboard.nextLine();
			}	
            
            if ( !grade.equalsIgnoreCase("X"))
		    {
                coreGrades[count] = grade;           
				count++;
			}
            
		} while (count < coreGrades.length && !grade.equalsIgnoreCase("X"));
		
		return count;
		
 	} // End getCoreGrades method


    /** A method that returns true if the String that is the argument is a
        valid letter grade.
        @param The grade String
        @return True if the letter grade is valid, false otherwise
    */
    
	public static boolean validGrade(String grade)
	{
		boolean valid = false;
		
		if (grade.equalsIgnoreCase("A"))
			valid = true;
		else if (grade.equalsIgnoreCase("A-"))
			valid = true;
		else if (grade.equalsIgnoreCase("B+"))
			valid = true;
		else if (grade.equalsIgnoreCase("B"))
			valid = true;
		else if (grade.equalsIgnoreCase("B-"))
			valid = true;
		else if (grade.equalsIgnoreCase("C+"))
			valid = true;
		else if (grade.equalsIgnoreCase("C"))
			valid = true;
		else if (grade.equalsIgnoreCase("F"))
			valid = true;
        else if (grade.equalsIgnoreCase("P"));
            valid = true;
		
		return valid;
	}
	
    
    /** A method that gets the letter grades earned by a student in their elective
        courses. 
        @param electiveGrades The array that stores the letter grades as Strings
        @return The number of letter grades put in the array
    */
    
    public static int getElectiveGrades(String[] electiveGrades)
	{
		int count = 0;
		String grade;
		
        do
        {
            System.out.print("Enter elective grade #" + (count + 1) + " or X to eXit elective entry: ");
		    grade = keyboard.nextLine();
            
            while ( !grade.equalsIgnoreCase("X") && !validGrade(grade))
			{
				System.out.println("\nError, " + grade + " is not a valid grade.");	
				System.out.print("Enter elective grade #" + (count + 1) + " or X to eXit elective entry: ");
				grade = keyboard.nextLine();
			}	
            
            if ( !grade.equalsIgnoreCase("X"))
		    {
                electiveGrades[count] = grade;
				count++;
			}
            
		} while (count < electiveGrades.length && !grade.equalsIgnoreCase("X"));
		
		return count;
		
 	} // End getElectiveGrades method


    /** A method that gets the letter grades earned by a student in their extra grad
        courses. 
        @param otherGradGrades The array that stores the letter grades as Strings
        @return The number of letter grades put in the array
    */
    
    public static int getOtherGradGrades(String[] otherGradGrades)
	{
		int count = 0;
		String grade;
		
        do
        {
            System.out.print("Enter other graduate grade #" + (count + 1) + " or X to eXit grade entry: ");
		    grade = keyboard.nextLine();
            
            while ( !grade.equalsIgnoreCase("X") && !validGrade(grade))
			{
				System.out.println("\nError, " + grade + " is not a valid grade.");		
				System.out.print("Enter other grade #" + (count + 1) + " or X to eXit grade entry: ");
				grade = keyboard.nextLine();
			}	
            
            if ( !grade.equalsIgnoreCase("X"))
		    {
                otherGradGrades[count] = grade;
				count++;
			}
            
		} while (count < otherGradGrades.length && !grade.equalsIgnoreCase("X"));
		
		return count;
		
 	} // End getOtherGradGrades method


	/** A method that displays the letter grades stored in 
	    an array one per line on the screen.
	    @param grades The array containing the letter grade Strings
	    @param numGrades The number of grades in the array.
	*/
	
	public static void displayGrades(String[] grades, int numGrades)
	{
		for (int count = 0; count < numGrades; count++)
		{
			System.out.println(grades[count]);
		}
		
		return;
        
	} // End of method displayGrades


    /** A method that returns the the number of gradePoints
        corresponding to a letter grade.
        @param The letter grade as a String
        @return The grade points
    */
    
	public static double getGradePoints(String letterGrade)
	{
		double gradePoints = 0.00 ;
		
		if (letterGrade.equalsIgnoreCase("A"))
			gradePoints = A_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("A-"))
			gradePoints = A_MINUS_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("B+"))
			gradePoints = B_PLUS_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("B"))
			gradePoints = B_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("B-"))
			gradePoints = B_MINUS_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("C+"))
			gradePoints = C_PLUS_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("C"))
			gradePoints = C_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("F"))
			gradePoints = F_GRADEPTS;
		else if (letterGrade.equalsIgnoreCase("P"))
            gradePoints = P_GRADEPTS;
        else
			System.out.println("\nError, grade not found!");
			
		return gradePoints;
	}
    
    /** A function that counts the number of Pass grades in an array.
        @param grades An array of letter grades
        @param elements The number of elements in the array
        @return The number of P (Pass grades) in the array
    */
    
    public static int countPasses (String[] grades, int elements)
    {
        int passCount = 0;
        
        for(int count = 0; count < elements; ++count)
        {
            if (grades[count].equalsIgnoreCase("P"))
            {
                ++passCount;
            }
        }
        
        return passCount;
    }
        
    
    /* A function that finds the number of grade points needed.
       @param level The required grade point level
       @param basis The number of courses contributing to grade points needed
       @param gradePtsSoFar The grade points earned so far
       @returns The grade points outstanding
    */    
    
    public static double calcGradePointsNeeded (double level, int basis, double gradePtsSoFar)
    {
        double gradePtsNeeded = level * basis;
        
        return (gradePtsNeeded - gradePtsSoFar);      				
    }
    
    /* A method that returns the grade needed in the remaining course.
       @param avgNeeded The double containing the average needed;
       @returns neededGrade The string that will store the grade needed.
    */		        
        
    public static String getGradeNeeded(double avgNeeded)
    {
        String neededGrade = "C+";		
        
        if (avgNeeded > 3.670)
            neededGrade = "A";
        else if (avgNeeded > 3.330)
            neededGrade = "A-"; 
        else if (avgNeeded > 3.000)
            neededGrade = "B+";
        else if (avgNeeded > 2.670)
            neededGrade = "B";   
        else if (avgNeeded > 2.330)
            neededGrade = "B-";
        
        return neededGrade;
    }	

    	
} // End of class GradAudit



    
    

							