package edu.sdsmt.hcats_millage_zoe;//TODO add packages

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tier1ModelTest {
    public static final int MOVE_START = 15;
    public static final int TREAT_START = 3;
    public static final int CAT_START = 40;
    public static final int CAT_START_PER_CELL = 5;
    public static final int GRID_SIZE = 3;
    @Test
    public void a_initialValues() {
        Game g = new Game();
        assertEquals(MOVE_START, g.getMoves());
        assertEquals(TREAT_START, g.getTreats());
        assertEquals(CAT_START, g.getTotalCatsLeft());
        assertEquals(0, g.getCatsCaught());
        assertEquals(MOVE_START, g.getMoves());

        //all except "capture area" should have equal number of cats
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!(i == GRID_SIZE - 1 && j == GRID_SIZE - 1))
                    assertEquals(CAT_START_PER_CELL,  g.getCatsAt(i,j));
            }
        }
    }

    @Test
    public void b1_sweepRightMin() {
        Game g = new Game();
        g.sweepRight(1, 0); //should move only 1 cat in each cell

        //sweeping takes 1 move
        assertEquals(MOVE_START-1, g.getMoves() );

        //first colum should lose 1 cat
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL -1,  g.getCatsAt(0,i));
        }

        //second colum should stay the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1,i));
        }

        //third colum should gain 1 cat
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL +1,  g.getCatsAt(2,i));
        }
    }

    @Test
    public void b2_sweepRightPercent() {
        Game g = new Game();
        g.sweepRight(1, 60); //should move only 3 cats in each cell

        //sweeping takes 1 move
        assertEquals(MOVE_START-1, g.getMoves() );

        //first colum should lose 3 cat
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL -3,  g.getCatsAt(0,i));
        }

        //second colum should stay the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1,i));
        }

        //third colum should gain 3 cats
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL +3,  g.getCatsAt(2,i));
        }
    }

    @Test
    public void b3_sweepRightEmpty() {
        Game g = new Game();
        g.sweepRight(5, 60); //should move only 5 cats in each cell

        //sweeping takes 1 move
        assertEquals(MOVE_START-1, g.getMoves() );

        //first colum should lose all cats
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(0,  g.getCatsAt(0,i));
        }

        //second colum should stay the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1,i));
        }

        //third colum should gain 5 cats
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL +5,  g.getCatsAt(2,i));
        }

        //check that double pull does not go negative
        //AND the the max of the max of the two wins
        int MOVE_NUM = 3;
        g.sweepRight(1, 60); //should move only 3 cats in each cell
        assertEquals(MOVE_START - 2, g.getMoves() );

        //first colum should still be 0
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(0,  g.getCatsAt(0,i));
        }

        //second colum should lose 3 cats the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL - MOVE_NUM,  g.getCatsAt(1,i));
        }

        //third colum should gain 3 cats
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL + 5 + MOVE_NUM,  g.getCatsAt(2,i));
        }
    }

    @Test
    public void c1_sweepDownMin() {
        Game g = new Game();
        g.sweepDown(1, 0); //should move only 1 cat in each cell

        //sweeping takes 1 move
        assertEquals(MOVE_START-1, g.getMoves() );
        //first row should lose 1 cat
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL -1,  g.getCatsAt(i, 0));
        }

        //second row should stay the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL,  g.getCatsAt(i, 1));
        }

        //third row should gain 1 cat
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL +1,  g.getCatsAt(i, 2));
        }
    }

    @Test
    public void c2_sweepDownPercent() {
        Game g = new Game();
        g.sweepDown(1, 60); //should move only 3 cats in each cell

        //sweeping takes 1 move
        assertEquals(MOVE_START-1, g.getMoves() );

        //first colum should lose 3 cat
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL -3,  g.getCatsAt(i, 0));
        }

        //second colum should stay the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL,  g.getCatsAt(i,1));
        }

        //third colum should gain 3 cats
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL +3,  g.getCatsAt(i,2));
        }
    }

    @Test
    public void c3_sweepDownEmpty() {
        Game g = new Game();
        g.sweepDown(5, 60); //should move only 5 cats in each cell

        //sweeping takes 1 move
        assertEquals(MOVE_START-1, g.getMoves() );

        //first colum should lose all cats
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(0,  g.getCatsAt(i,0));
        }

        //second colum should stay the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL,  g.getCatsAt(i,1));
        }

        //third colum should gain 5 cats
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL +5,  g.getCatsAt(i,2));
        }

        //check that double pull does not go negative
        //AND the the max of the max of the two wins
        int MOVE_NUM = 3;
        g.sweepDown(1, 60); //should move only 3 cats in each cell
        assertEquals(MOVE_START - 2, g.getMoves() );

        //first colum should still be 0
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(0,  g.getCatsAt(i,0));
        }

        //second colum should lose 3 cats the same
        for (int i = 0; i < GRID_SIZE; i++) {
            assertEquals(CAT_START_PER_CELL - MOVE_NUM,  g.getCatsAt(i,1));
        }

        //third colum should gain 3 cats
        for (int i = 0; i < GRID_SIZE - 1; i++) { //minus one to skip capture cell
            assertEquals(CAT_START_PER_CELL + 5 + MOVE_NUM,  g.getCatsAt(i,2));
        }
    }

    @Test
    public void d_useTreat() {
        Game g = new Game();
        g.useTreat();

        //doesn't do anything yet (until the sweep direction is known), but it does use up a treat
        //and a move
        assertEquals(MOVE_START - 1, g.getMoves());
        assertEquals(TREAT_START-1, g.getTreats());

        g.useTreat();
        assertEquals(MOVE_START - 2, g.getMoves());
        assertEquals(TREAT_START-2, g.getTreats());

        g.useTreat();
        assertEquals(MOVE_START - 3, g.getMoves());
        assertEquals(0, g.getTreats());

        //make sure only a max number of treats can be pulled
        g.useTreat();
        assertEquals(MOVE_START - 3, g.getMoves());
        assertEquals(0, g.getTreats());

    }

    @Test
    public void e_win() {
        Game g = new Game();

        //shove all the to the right
        g.sweepRight(100, 100);
        g.sweepRight(100, 100);
        g.sweepRight(100, 100);

        //shove all the to the capture area
        g.sweepDown(100, 100);
        g.sweepDown(100, 100);
        g.sweepDown(100, 100);

        assertEquals(MOVE_START - 6, g.getMoves());
        assertEquals(CAT_START, g.getCatsCaught());
        assertTrue(g.isWon());

    }

    @Test
    public void f_lose() {
        Game g = new Game();

       for(int i = 0; i < MOVE_START + 1; i++){
          g.sweepRight(0,0);
       }
        assertTrue(g.getMoves() < 0);
        assertTrue(g.isLost());
    }

    @Test
    public void g_reset() {
        Game g = new Game();
        g.sweepDown(1, 0); //should move only 1 cat each cell
        g.sweepRight(1, 0); //should move only 1 cat each cell

        assertEquals(MOVE_START-2, g.getMoves() );

        //check all counts to ensure they updated
        //row1
        assertEquals(CAT_START_PER_CELL -2,  g.getCatsAt(0, 0));
        assertEquals(CAT_START_PER_CELL -1,  g.getCatsAt(1, 0));
        assertEquals(CAT_START_PER_CELL,  g.getCatsAt(2, 0));
        //row2
        assertEquals(CAT_START_PER_CELL -1,  g.getCatsAt(0, 1));
        assertEquals(CAT_START_PER_CELL,  g.getCatsAt(1, 1));
        assertEquals(CAT_START_PER_CELL +1,  g.getCatsAt(2, 1));
        //row3
        assertEquals(CAT_START_PER_CELL,  g.getCatsAt(0, 2));
        assertEquals(CAT_START_PER_CELL +1,  g.getCatsAt(1, 2));
        //check caught count
        assertEquals(2,  g.getCatsAt(2, 2));


        //should return to initial values
        g.reset();
        a_initialValues();

    }
}