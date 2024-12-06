package millage_zoe;

import java.text.DecimalFormat;

/**
 * @author Dr. Lisa Rebenitsch
 */
public class ParkingTests {

    private static final String ERROR_MARK = ">>>>>>>>>>>>>>";
    //monitor for error
    private static boolean haveError = false;

    /**
     * Main test method: calls other tests.
     *
     * @param args Ignored command-line arguments
     */
    public static void main(String[] args) {
        tier1_SmallLot();
        checkForEarlyStop();

        tier2_ComingAndGoing();
        checkForEarlyStop();

        tier3_FeeCalculation();
        checkForEarlyStop();

        tier4_CheckForGlitches();
        checkForEarlyStop();

        tier5_FreeSmallLot();
        checkForEarlyStop();

        tier6_FreeComingAndGoing();
        checkForEarlyStop();

        tier7_FeeCalculation();
        checkForEarlyStop();

        tier8_CheckForGlitches();
        checkForEarlyStop();

        tier9_testTinyFreeDistrict();
        checkForEarlyStop();

        tier10_District();
        checkForEarlyStop();

        tier11_testPaidDistrict();
        checkForEarlyStop();

        System.out.println("All tests finished.");
    }

    /**
     * Test creating a parking lot with a single space.
     */
    public static void tier1_SmallLot() {
        System.out.println("Tier 1: Testing a Parking lot with one space (no fee calculation needed)..................................");

        String targetOutput = "Status for Paid Parking Spot parking lot: 1 vehicles (CLOSED) Money Collected: $0.00";

        //check default constructors
        ParkingLot defaultLot = new ParkingLot(1);
        if ( defaultLot.getName().equals("Test") )
            printError(" Default name should not be capitalized.");
        if ( !defaultLot.getName().equals("test") )
            printError(" Default name is not working with constructor with one parameter.");

        defaultLot = new ParkingLot(1, 2.25);
        if ( defaultLot.getName().equals("Test") )
            printError(" Default name should not be capitalized.");
        if ( !defaultLot.getName().equals("test") )
            printError(" Default name is not working  with two parameters.");


        //check constructor
        defaultLot = new ParkingLot("Paid Parking Spot", 1);

        if ( !defaultLot.getName().equals("Paid Parking Spot") )
            printError(".getName() not working");

        TestSmallLost(defaultLot, targetOutput);
    }

    /**
     * Test what happens when lot fills, empties, then refills. No fee check
     */
    public static void tier2_ComingAndGoing() {
        System.out.println("Tier 2: Testing Parking lot multiple coming and going cars (no fee check)............................");

        //test strings
        ParkingLot busy = new ParkingLot(6);
        String[] results = { "Status for test parking lot: 3 vehicles (50%) Money Collected: $0.00",
                "Status for test parking lot: 4 vehicles (66.7%) Money Collected: $0.00",
                "Status for test parking lot: 6 vehicles (CLOSED) Money Collected: $0.00" };
        TestStrings(busy, results);

        //test events
        busy = new ParkingLot(5);
        TestComingAndGoing(busy);


    }


    ///////////////////////////////////////////////////////////
    // district tests

    /**
     * Checks fee calculation
     */
    public static void tier3_FeeCalculation() {
        System.out.println("Tier 3: Test Fees in a Parking lot....................................................");

        DecimalFormat format = new DecimalFormat("#.##");
        String targetOutput = "Status for Reserved parking lot: 1 vehicles (CLOSED) Money Collected: $0.00";
        String targetOutput1 = "Status for Reserved parking lot: 0 vehicles (0%) Money Collected: $0.00";
        String targetOutput2 = "Status for Reserved parking lot: 0 vehicles (0%) Money Collected: $6.25";
        String targetOutput3 = "Status for Reserved Lot parking lot: 0 vehicles (0%) Money Collected: $3.67";

        //default default amount
        ParkingLot driveway = new ParkingLot("Reserved", 1);
        int id = driveway.markVehicleEntry(5);
        if ( Math.abs(driveway.getProfit()) > 0.01 )
            printError(" Wrong profit. It should be $0.00");
        if ( !driveway.toString().equals(targetOutput) ) {
            printError(" Incorrect full parking space string result.");
            showBeforeAfter(driveway.toString(), targetOutput);
        }

        //check threshold for no pay
        driveway.markVehicleExit(20, id);
        if ( Math.abs(driveway.getProfit()) > 0.01 )
            printError(" Wrong profit. It should be $0.00 since it is 15 minutes or less since arrival.");
        if ( !driveway.toString().equals(targetOutput1) ) {
            printError(" Incorrect empty parking space string result.");
            showBeforeAfter(driveway.toString(), targetOutput1);
        }

        //check pay calculation with default
        id = driveway.markVehicleEntry(25);
        driveway.markVehicleExit(400, id);
        if ( Math.abs(driveway.getProfit() - 6.25) > 0.01 )
            printError(" Wrong profit. It should be $6.25. Got $" + driveway.getProfit());
        if ( !driveway.toString().equals(targetOutput2) ) {
            printError(" Incorrect paid amount with 1 car string result.");
            showBeforeAfter(driveway.toString(), targetOutput2);
        }

        //reset with new fee
        driveway = new ParkingLot("Reserved", 1, 2.75);
        id = driveway.markVehicleEntry(5);
        driveway.markVehicleExit(400, id);
        if ( Math.abs(driveway.getProfit() - 18.1) > 0.01 )
            printError(" Wrong profit. It should be $18.10. Got " + format.format(driveway.getProfit()));

        //confirm still correct with multiple cars
        driveway = new ParkingLot("Reserved Lot", 5, 2.75);
        id = driveway.markVehicleEntry(5);
        int id2 = driveway.markVehicleEntry(5);
        int id3 = driveway.markVehicleEntry(10);
        int id4 = driveway.markVehicleEntry(15);
        int id5 = driveway.markVehicleEntry(15);
        driveway.markVehicleExit(15, id);
        driveway.markVehicleExit(20, id2);
        driveway.markVehicleExit(26, id4);
        driveway.markVehicleExit(45, id5);
        driveway.markVehicleExit(60, id3);
        if ( Math.abs(driveway.getProfit() - 3.66666666666) > 0.01 )
            printError(" Wrong profit. It should be $3.67. Got " + format.format(driveway.getProfit()));
        if ( !driveway.toString().equals(targetOutput3) ) {
            printError(" Incorrect paid lot string result.");
            showBeforeAfter(driveway.toString(), targetOutput3);
        }
    }

    /**
     * Test entry/exit where the time is in the past, bad ids, double removal, etc.
     */
    public static void tier4_CheckForGlitches() {
        System.out.println("Tier 4: Testing a Parking with invalid times.............................................");

        ParkingLot lot = new ParkingLot(6);
        CheckForGlitches(lot);

        //nonexistant id
        lot = new ParkingLot(3);
        lot.markVehicleEntry(2);
        lot.markVehicleExit(50, -1);
        if ( lot.getVehiclesInLot() != 1 )
            printError(" Error: expecting 1 vehicle at time 50.  Got: " + lot.getVehiclesInLot());
    }

    public static void tier5_FreeSmallLot() {
        System.out.println("Tier 5: Testing a free Parking lot with one space..................................");

        String targetOutput = "Status for Paid Parking Spot parking lot: 1 vehicles (CLOSED)";

        //check default constructor
        FreeParkingLot defaultLot = new FreeParkingLot(1);
        if ( defaultLot.getName().equals("Test") )
            printError(" Default name is not working.");

        //check constructor
        defaultLot = new FreeParkingLot("Paid Parking Spot", 1);

        if ( !defaultLot.getName().equals("Paid Parking Spot") )
            printError(".getName() not working");

        TestSmallLost(defaultLot, targetOutput);
    }

    /**
     * Test what happens when lot fills, empties, then refills. No fee check
     */
    public static void tier6_FreeComingAndGoing() {
        System.out.println("Tier 6: Testing free Parking lot multiple coming and going cars............................");

        //test strings
        FreeParkingLot busy = new FreeParkingLot(6);
        String[] results = { "Status for test parking lot: 3 vehicles (50%)",
                "Status for test parking lot: 4 vehicles (66.7%)",
                "Status for test parking lot: 6 vehicles (CLOSED)" };
        TestStrings(busy, results);

        //test events
        busy = new FreeParkingLot(5);
        TestComingAndGoing(busy);
    }

    /**
     * Checks fee calculation (or lack thereof)
     */
    public static void tier7_FeeCalculation() {
        System.out.println("Tier 7: Test Fees in a Free Parking lot (should always be 0)..............................................");

        //default default amount
        FreeParkingLot driveway = new FreeParkingLot("Reserved", 1);
        driveway.markVehicleEntry(5);
        if ( Math.abs(driveway.getProfit()) > 0.01 )
            printError(" Wrong profit. It should be $0.00");

        //check pay calculation with default
        driveway.markVehicleExit(10);
        if ( Math.abs(driveway.getProfit()) > 0.01 )
            printError(" Wrong profit. It should be $0.");

        driveway.markVehicleEntry(25);
        driveway.markVehicleExit(400);
        if ( Math.abs(driveway.getProfit()) > 0.01 )
            printError(" Wrong profit. It should be $0.");

        //confirm still correct with multiple cars
        driveway = new FreeParkingLot("Reserved Lot", 5);
        driveway.markVehicleEntry(5);
        driveway.markVehicleEntry(5);
        driveway.markVehicleEntry(10);
        driveway.markVehicleEntry(15);
        driveway.markVehicleEntry(15);
        driveway.markVehicleExit(15);
        driveway.markVehicleExit(20);
        driveway.markVehicleExit(26);
        driveway.markVehicleExit(45);
        driveway.markVehicleExit(60);
        if ( Math.abs(driveway.getProfit()) > 0.01 )
            printError(" Wrong profit. It should be $0. ");

    }

    /**
     * Test entry/exit where the time is in the past, bad ids, double removal, etc.
     */
    public static void tier8_CheckForGlitches() {
        System.out.println("Tier 8: Testing free Parking with invalid times.............................................");

        String targetOutput = "";

        FreeParkingLot lot = new FreeParkingLot(6);

        CheckForGlitches(lot);

        //nonexistant id
        lot = new FreeParkingLot(3);
        lot.markVehicleEntry(2);
        lot.markVehicleExit(50, -1);
        if ( lot.getVehiclesInLot() != 0 )
            printError(" Error: expecting 0 vehicle at time 50.  Since ids should be ignored. Got: " + lot.getVehiclesInLot());
    }

    /**
     * Create district with small parking lots, fill them, then
     * ensure one is not closed.
     */
    public static void tier9_testTinyFreeDistrict() {
        String initalOutput = "District status:\n" +
                "Status for red parking lot: 0 vehicles (0%)\n" +
                "Status for green parking lot: 0 vehicles (0%)\n" +
                "Status for blue parking lot: 0 vehicles (0%)\n";
        String endingOutput = "District status:\n" +
                "Status for red parking lot: 0 vehicles (0%)\n" +
                "Status for green parking lot: 1 vehicles (CLOSED)\n" +
                "Status for blue parking lot: 2 vehicles (CLOSED)\n";

        System.out.println("Tier 9: Testing a District with 3 tiny free parking lots......................................");
        District ourTown = new District();
        int redLot = ourTown.add(new FreeParkingLot("red", 1));
        int greenLot = ourTown.add(new FreeParkingLot("green", 1));
        int blueLot = ourTown.add(new FreeParkingLot("blue", 2));

        if ( !ourTown.toString().equals(initalOutput) ) {
            printError(" incorrect district toString()");
            showBeforeAfter(ourTown.toString(), initalOutput);
        }
        if ( ourTown.getVehiclesParkedInDistrict() != 0 )
            printError(" Expected 0 vehicles in tiny district");

        ourTown.markVehicleEntry(greenLot, 5);
        ourTown.markVehicleEntry(redLot, 7);
        ourTown.markVehicleEntry(blueLot, 10);
        ourTown.markVehicleEntry(blueLot, 12);
        if ( ourTown.getVehiclesParkedInDistrict() != 4 )
            printError(" Expected 4 vehicles in tiny district");
        if ( !ourTown.isClosed() )
            printError(" Error in tiny district at 12: should be closed.");
        ourTown.markVehicleExit(greenLot, 15, 0);
        if ( ourTown.isClosed() )
            printError(" Error in tiny district at 15: should be open.");
        if ( ourTown.getVehiclesParkedInDistrict() != 3 )
            printError(" Expected 3 vehicles in tiny district at time 15");

        ourTown.markVehicleExit(redLot, 17, 0);
        ourTown.markVehicleEntry(greenLot, 18);
        if ( ourTown.isClosed() )
            printError(" Error in tiny district at 18: should be open.");

        if ( !ourTown.toString().equals(endingOutput) ) {
            printError(" incorrect ending district toString()");
            showBeforeAfter(ourTown.toString(), endingOutput);
        }
    }

    private static void CheckForGlitches(ParkingLot lot) {
        int id1 = lot.markVehicleEntry(50);
        if ( lot.getVehiclesInLot() != 1 )
            printError(" Error: expecting 1 vehicle at time 50.");

        // try to enter, exit in past
        int id2 = lot.markVehicleEntry(49);
        if ( id2 != -1 )
            printError(" Error: entry should have failed.");
        if ( lot.getVehiclesInLot() != 1 )
            printError(" Error: expecting 1 vehicle at time -1A. Got: " + lot.getVehiclesInLot());
        lot.markVehicleExit(49, id1);
        if ( lot.getVehiclesInLot() != 1 )
            printError(" Error: expecting 1 vehicle at time -1B. Got: " + lot.getVehiclesInLot());

        // enter, exit all at once
        int id3 = lot.markVehicleEntry(51);
        int id4 = lot.markVehicleEntry(51);
        int id5 = lot.markVehicleEntry(51);
        if ( lot.getVehiclesInLot() != 4 )
            printError(" Error: expecting 4 vehicles at time 51. Got: " + lot.getVehiclesInLot());
        if ( lot.isClosed() )
            printError(" Error: lot should not be closed at time 51. Got: " + lot.getVehiclesInLot());
        int id6 = lot.markVehicleEntry(51);
        if ( lot.getVehiclesInLot() != 5 )
            printError(" Error: expecting 5 vehicles at time 51b. Got: " + lot.getVehiclesInLot());
        if ( !lot.isClosed() )
            printError(" Error: lot should be closed at time 51b. Got: " + lot.getVehiclesInLot());

        //overfill
        int id7 = lot.markVehicleEntry(52);
        int id8 = lot.markVehicleEntry(52);
        lot.markVehicleEntry(52);
        lot.markVehicleEntry(52);
        if ( lot.getVehiclesInLot() != 6 )
            printError(" Error: expecting 6 vehicles at time 52. Got: " + lot.getVehiclesInLot());
        if ( id8 != -1 )
            printError(" Error: car should have been denied.");

        //remove everything
        lot.markVehicleExit(53, id1);
        lot.markVehicleExit(53, id3);
        lot.markVehicleExit(53, id4);
        lot.markVehicleExit(53, id5);
        lot.markVehicleExit(53, id6);
        lot.markVehicleExit(53, id7);
        if ( lot.getVehiclesInLot() != 0 )
            printError(" Error: expecting 0 vehicles at time 53. Still had: " + lot.getVehiclesInLot());

        //try a double removal
        lot.markVehicleExit(53, id6);
        if ( lot.getVehiclesInLot() != 0 )
            printError(" Error: expecting 0 vehicles at time 53.");

        if ( lot.getClosedMinutes() != 2 )
            printError(" Error: expecting 2 minutes closed at time 51c. Got: " + lot.getClosedMinutes());
    }

    private static void TestComingAndGoing(ParkingLot busy) {
        int id0 = busy.markVehicleEntry(5);
        int id1 = busy.markVehicleEntry(10);
        int id2 = busy.markVehicleEntry(12);

        //check ids
        if ( id0 != 0 )
            printError(" IDs should start at 0.");
        if ( id1 != 1 )
            printError(" The next ID must be 1.");
        if ( id2 != 2 )
            printError(" The next ID must be 2.");
        if ( busy.isClosed() )
            printError(" Error: Busy lot should not be closed at time 12");


        //fill lot
        int id3 = busy.markVehicleEntry(15);
        if ( !busy.isClosed() )
            printError(" Error: Busy lot should be closed at time 15");
        if ( busy.getClosedMinutes() != 0 )
            printError(" Error: no time update yet, so total closed minutes is still 0");
        if ( busy.getVehiclesInLot() != 4 )
            printError(" Error: Busy lot should have four vehicles at time 15");
        int id4 = busy.markVehicleEntry(20);
        if ( busy.getVehiclesInLot() != 5 )
            printError(" Error: Busy lot should have five vehicles at time 20");
        if ( busy.getClosedMinutes() != 5 )
            printError(" Error: New entry, so total time closed updates to 5");
        busy.markVehicleExit(23, id0);
        if ( busy.getVehiclesInLot() != 4 )
            printError(" Error: Busy lot should have four vehicles at time 23");
        if ( busy.getClosedMinutes() != 8 )
            printError(" Error: New exit, so total time closed updates to 8. Got: " + busy.getClosedMinutes());

        //reopen lot
        busy.markVehicleExit(25, id1);
        busy.markVehicleExit(25, id2);
        if ( busy.getClosedMinutes() != 10 )
            printError(" Error: busy parking lot should have been closed for 10 minutes at time 30");

        //refill
        int id5 = busy.markVehicleEntry(33);
        int id6 = busy.markVehicleEntry(35);
        if ( !busy.isClosed() )
            printError(" Error: Busy lot should be closed at time 35");
        if ( busy.getClosedMinutes() != 10 )
            printError(" Error: Lot closed again, so total time closed updates to 12. Got: " + busy.getClosedMinutes());
        if ( busy.getVehiclesInLot() != 4 )
            printError(" Error: Busy lot should have four vehicles at time 35");
        int id7 = busy.markVehicleEntry(40);
        if ( busy.getVehiclesInLot() != 5 )
            printError(" Error: Busy lot should have five vehicles at time 40");

        //empty lot
        busy.markVehicleExit(45, id3);
        if ( busy.getVehiclesInLot() != 4 )
            printError(" Error: Busy lot should have four vehicles at time 45");
        busy.markVehicleExit(50, id4);
        busy.markVehicleExit(54, id5);
        busy.markVehicleExit(60, id6);
        busy.markVehicleExit(60, id7);
        if ( busy.getClosedMinutes() != 25 )
            printError(" Error: busy parking lot should have been closed for 25 minutes");
        if ( busy.getVehiclesInLot() != 0 )
            printError(" Error: Busy lot should be empty at end");
    }

    private static void TestSmallLost(ParkingLot driveway, String targetOutput) {

        //check variables
        if ( Math.abs(FreeParkingLot.CLOSED_THRESHOLD - 80) > 0.01 )
            printError("Incorrect threshold for closed lot.");
        if ( driveway.isClosed() )
            printError("Empty Paid Parking Spot is closed.");

        //check entry status
        int id = driveway.markVehicleEntry(5);
        if ( id != 0 )
            printError(" IDs should start at 0.");
        if ( !driveway.isClosed() )
            printError(" Paid Parking Spot with something in it is not closed.");
        if ( !driveway.toString().equals(targetOutput) ) {
            printError(" Incorrect string result ");
            showBeforeAfter(driveway.toString(), targetOutput);
        }

        //check vehicle exited and closed count
        driveway.markVehicleExit(400, id);
        if ( driveway.getVehiclesInLot() != 0 )
            printError(" Empty driveway should have no vehicles in it.");
        if ( driveway.getClosedMinutes() != 395 )
            printError(" Wrong number of minutes while sleeping overnight.");
    }

    private static void TestStrings(ParkingLot busy, String[] results) {
        //test strings
        busy.markVehicleEntry(1);
        busy.markVehicleEntry(1);
        busy.markVehicleEntry(1);
        if ( !busy.toString().equals(results[0]) ) {
            printError(" Incorrect open string result ");
            showBeforeAfter(busy.toString(), results[0]);
        }
        busy.markVehicleEntry(1);
        if ( !busy.toString().equals(results[1]) ) {
            printError(" Incorrect second open string result ");
            showBeforeAfter(busy.toString(), results[1]);
        }
        busy.markVehicleEntry(1);

        busy.markVehicleEntry(1);
        if ( !busy.toString().equals(results[2]) ) {
            printError(" Incorrect closed result ");
            showBeforeAfter(busy.toString(), results[2]);
        }
    }

    /**
     * Watches for a error call, and ends cleanly
     */
    private static void checkForEarlyStop() {
        System.out.println();
        if ( !haveError )
            return;

        System.err.println("\n\nA tier was not passed. Later teirs not tested");
        System.exit(0);
    }



    //helper function to both set a flag and output a error in easy to catch format
    private static void printError(String str) {
        haveError = true;
        System.err.println(ERROR_MARK + str);
    }

    /**
     * Helper function when the check is a long string
     *
     * @param actual   the target string of the show
     * @param expected what was actually returned
     */
    private static void showBeforeAfter(String actual, String expected) {

        String indent = "    ";
        printError(" Got this (indented added for readability):");
        printError(indent + actual.replace("\n", "\n" + indent));
        printError(" Needed this:");
        printError(indent + expected.replace("\n", "\n" + indent));
        printError("");

    }

    //////////////////////////////////////////////////////////
    // free parking lost tests

    /**
     * Test District class with three lots.
     */
    public static void tier10_District() {
        System.out.println("Tier 10: Testing a District with 3 normal parking lots (no fee check)..................");

        String targetTime7 = "District status:\n" +
                "Status for brown parking lot: 7 vehicles (70%) Money Collected: $0.00\n" +
                "Status for green parking lot: 14 vehicles (CLOSED) Money Collected: $0.00\n" +
                "Status for black parking lot: 7 vehicles (58.3%) Money Collected: $0.00\n";
        String targetTime8 = "District status:\n" +
                "Status for brown parking lot: 8 vehicles (CLOSED) Money Collected: $0.00\n" +
                "Status for green parking lot: 14 vehicles (CLOSED) Money Collected: $0.00\n" +
                "Status for black parking lot: 7 vehicles (58.3%) Money Collected: $0.00\n";
        String targetTime10 = "District status:\n" +
                "Status for brown parking lot: 8 vehicles (CLOSED) Money Collected: $0.00\n" +
                "Status for green parking lot: 14 vehicles (CLOSED) Money Collected: $0.00\n" +
                "Status for black parking lot: 10 vehicles (CLOSED) Money Collected: $0.00\n";

        District airport = new District();
        int brown = airport.add(new ParkingLot("brown", 10));
        int green = airport.add(new ParkingLot("green", 15));
        int black = airport.add(new ParkingLot("black", 12));

        for ( int i = 0; i < 7; i++ ) {
            airport.markVehicleEntry(brown, i);
            airport.markVehicleEntry(green, i);
            airport.markVehicleEntry(green, i);
            airport.markVehicleEntry(black, i);
            if ( airport.isClosed() )
                printError(" Error: airport closed at time 7.");
        }
        if ( airport.getVehiclesParkedInDistrict() != 28 )
            printError(" Expected 28 vehicles in airport");

        if ( !airport.toString().equals(targetTime7) ) {
            printError(" incorrect airport at time 7 toString()");
            showBeforeAfter(airport.toString(), targetTime7);
        }


        airport.markVehicleEntry(brown, 8);
        if ( airport.isClosed() )
            printError(" Error: airport closed at time 8.");

        if ( !airport.toString().equals(targetTime8) ) {
            printError(" incorrect airport at time 8 toString()");
            showBeforeAfter(airport.toString(), targetTime8);
        }

        ParkingLot blackLot = airport.getLot(black);
        if ( !blackLot.getName().equals("black") )
            printError(" Black lot has the wrong name.");
        if ( blackLot.getVehiclesInLot() != 7 )
            printError(" Expecting 7 vehicles in black lot at time 8.");
        airport.markVehicleEntry(black, 9);
        airport.markVehicleEntry(black, 10);
        airport.markVehicleEntry(black, 10);
        if ( blackLot.getVehiclesInLot() != 10 )
            printError(" Expecting 10 vehicles in black lot at time 10.");
        if ( !airport.isClosed() )
            printError(" Error: airport not closed at time 10.");
        if ( airport.getVehiclesParkedInDistrict() != 32 )
            printError(" Expected 32 vehicles in airport");

        if ( !airport.toString().equals(targetTime10) ) {
            printError(" incorrect airport at time 10 toString()");
            showBeforeAfter(airport.toString(), targetTime10);
        }
    }

    public static void tier11_testPaidDistrict() {
        System.out.println("Tier 11: Testing a District with 2 normal, and 3 paid parking lots.......................");

        String result = "District status:\n" +
                "Status for pink parking lot: 1 vehicles (50%)\n" +
                "Status for blue parking lot: 2 vehicles (66.7%)\n" +
                "Status for red paid parking lot: 1 vehicles (50%) Money Collected: $1.42\n" +
                "Status for gray paid parking lot: 2 vehicles (66.7%) Money Collected: $2.57\n" +
                "Status for green paid parking lot: 2 vehicles (50%) Money Collected: $0.73\n";

        DecimalFormat format = new DecimalFormat("#.##");

        District town = new District();
        town.add(new FreeParkingLot("pink", 2));
        town.add(new FreeParkingLot("blue", 3));
        town.add(new ParkingLot("red paid", 2, 5));
        town.add(new ParkingLot("gray paid", 3, 7));
        town.add(new ParkingLot("green paid", 4));

        //many entries
        town.markVehicleEntry(0, 1);
        town.markVehicleEntry(1, 2);
        int car1 = town.markVehicleEntry(2, 3);
        int car2 = town.markVehicleEntry(3, 4);
        int car3 = town.markVehicleEntry(4, 5);
        town.markVehicleEntry(1, 6);
        town.markVehicleEntry(1, 7);
        town.markVehicleEntry(0, 8);
        town.markVehicleEntry(2, 9);
        town.markVehicleEntry(2, 10);
        town.markVehicleEntry(3, 11);
        town.markVehicleEntry(3, 11);
        town.markVehicleEntry(4, 11);
        town.markVehicleEntry(4, 11);
        int car4 = town.markVehicleEntry(4, 11);

        town.markVehicleExit(0, 15, 0);
        town.markVehicleExit(1, 16, 0);
        town.markVehicleExit(2, 20, car1);
        town.markVehicleExit(3, 26, car2);
        town.markVehicleExit(4, 30, car3);
        town.markVehicleExit(4, 30, car4);


        if ( town.getVehiclesParkedInDistrict() != 8 )
            printError(" Expected 10 vehicles in town. Got: " + town.getVehiclesParkedInDistrict());

        if ( town.getClosedMinutes() != 4 )
            printError(" At end of day, all lots should be closed 4 minutes. They were closed "
                    + town.getClosedMinutes() + " min.");

        if ( Math.abs(town.getTotalMoneyCollected() - 4.72) > 0.01 )
            printError(" At end of day, $4.72 should have been collected. $"
                    + format.format(town.getTotalMoneyCollected()) + " was collected.");


        if ( !town.toString().equals(result) ) {
            printError(" incorrect final paid district toString()");
            showBeforeAfter(town.toString(), result);
        }
    }


}
