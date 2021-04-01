# CS 1632 - Software Quality Assurance
Spring Semester 2020 - Exercise 1

For this exercise, you and your partner will determine a test plan for the simple simulator GoatGoatCar, based on the requirements listed.  There are several known defects in the software; you will need to find and report on at least two.  Additionally, a traceability matrix showing the mapping of test cases to requirements is required.  

There should be at least six test cases altogether.

Test cases should mention all necessary preconditions, execution steps, and postconditions.

It is expected that you actually execute the test plan in order to find the defects, along with some exploratory testing to determine how the system works and where defects might lie.  While you are not expected to find *all* of the defects, a reasonable test plan should definitely find at least two.  This is an intentionally target-rich environment.

You may ask me any questions you may have during class.  It's not graded.  Ask me anything.

Remember, the template for test cases -

```
	IDENTIFIER:
	TEST CASE: 
	PRECONDITIONS:
	EXECUTION STEPS:
	POSTCONDITIONS:
```

The IDENTIFIER is some value which will UNIQUELY specify the test case.  It can be a number, word or any other mnemonic (e.g. TEST-INVALID-TIMES, TEST-LOW-NUM-TIMES, etc.).  Note that the INPUT VALUES and OUTPUT VALUES fields in the template are omitted because we are not yet doing method unit testing.

Remember the correct format for defects -

```
	 IDENTIFIER:
	 SUMMARY:
	 DESCRIPTION:
	 REPRODUCTION STEPS:
	 EXPECTED BEHAVIOR:
	 OBSERVED BEHAVIOR:
```

Other attributes of a defect (e.g., SEVERITY or IMPACT) are not necessary.  The test case which found the defect should be listed as part of the DESCRIPTION.

A traceability matrix allows us to determine that our test cases are checking requirements, and that our requirements have test coverage.  See Chapter 6 section 6 (6.6) in the textbook for examples and detail on creating them.

Note that some test cases may actually test several requirements.  You should specify the one that the test case fits best - that is, what are you really trying to test with this test case?

Note that it is NOT necessary for all requirements to be covered by this assignment!

Hint: Try to have a combination of explicit boundary values and implicit boundary values and corner cases in your test cases.  As we learned, this is where most of the defects will reside!

## GoatGoatCar
GoatGoatCar is going to be our way of determining the correct answer to the "Monty Hall Problem" (https://en.wikipedia.org/wiki/Monty_Hall_problem).  The Monty Hall Problem can be summarized as follows:

_Assume you are on a game show with three closed doors.  Behind one door is a car (which you want), and behind the other two doors are goats (which you do not want - if you do want a goat, I implore you to research how difficult goat tending is).  You pick one door, which you hope has the car behind it.  The game show host - who knows where the car is, and so will not open that door - opens up one of the two doors you did not select.  Behind that door is, of course, a goat._

_The question: is the optimal strategy to switch doors to the remaining closed door, to stay with the door you've already selected, or does it not matter?_

The answer is: it is always better to switch doors (twice better)!  To see why, you can read the above wikipedia entry if you like reading, or here is a video if you are a more visual learner: https://www.youtube.com/watch?v=9vRUxbzJZ9Y.

Our program will attempt to find the solution the worst possible way - by brute force.  It will simulate a large number of these decisions and give a summary at the end of what percentage of the time switching would give you the "good item" and what percentage of the time staying would have won you the "bad" item.

The program will accept four arguments, in this order:

* __"Good" option__ - This will be the item that the player actually wants (e.g., a car).

* __"Bad" option__ - This will be the item that the player does not want (e.g., a goat)

* __Num Times__ - This is the number of rounds that will be simulated.

* __Num Threads__ - This will be the number of threads that the simulation runs in

Example command to execute.  This will use "car" as the good choice, "goat" as the bad choice, 10001 as the number of rounds to simulate, and it will do it on four threads.

```
$ java -jar GoatGoatCar.jar car goat 10001 4
```

GoatGoatCar.jar is available in this directory.  

The requirements are listed in the file requirements.md in this directory.

## Submission

Note that the in-class exercise is not used for final grade calculation.  However, an extra credit of 1 point out of 100 points for the entire course will be awarded to the group that finds the most number of defects in the program.  There can be multiple winners too if there is a tie!

Please use the ReportTemplate.docx file provided in this directory to write your report.  If you don't have a .docx compatible word processor, that's perfectly fine as long as you follow the same organization.  A PDF version of the file is at ReportTemplate.pdf.  Please make sure that the traceability matrix, test cases, and defects are on seperate pages.  You will be submitting to GradeScope in PDF format.  When you submit, you will be asked to assign pages in the PDF file to each rubric item: 1. Traceability Matrix, 2. Test Cases, 3. Defects, and 4. Extra credit: Defects found.  You will assign the same pages for 3. and 4. (obviously).

Each pairwise group will submit the exercise *once* to GradeScope, by *one member* of the group.  The submitting member will press the "View or edit group" link at the top-right corner of the assignment page after submission to add his/her partner.  That way, the feedback will be accessible to both of you.

When your exercise is marked as graded, you should find feedback written on your grade details.  Please use the feedback wisely when doing Deliverable 1!
