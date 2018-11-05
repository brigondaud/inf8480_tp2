package com.inf8480_tp2.shared.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class OptionParserTest {

    @Test
    public void corruptOptionTest() {
        String[] notCorrupted = {"--portDir", "5001"};
        String[] corrupted = {"--unsafe", "--corrupt", "75"};
        String[] corruptOptionOnly = {"--corrupt", "50"};
        OptionParser parser = new OptionParser(notCorrupted);
        assertTrue(parser.isSafeMode());
        parser = new OptionParser(corrupted);
        assertFalse(parser.isSafeMode());
        parser = new OptionParser(corruptOptionOnly);
        assertFalse(parser.isSafeMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCorruptRate() {
        String[] invalid = {"--corrupt", "102"};
        OptionParser parser = new OptionParser(invalid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notFloatCorruptRate() {
        String[] notFloat = {"--corrupt", "invalid"};
        OptionParser parser = new OptionParser(notFloat);
    }

    @Test
    public void capacityOptionTest() {
        // Test if the default capacity value is correct
        String[] args1 = {"--portDir", "5001"};
        OptionParser parser = new OptionParser(args1);
        assertEquals(4, parser.getServerCapacity());
        // Test the option
        String[] args2 = {"--capacity", "25"};
        parser = new OptionParser(args2);
        assertEquals(25, parser.getServerCapacity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCapacityTest() {
        String[] invalid = {"--capacity", "-12"};
        OptionParser parser = new OptionParser(invalid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void floatCapacity() {
        String[] floatCapacity = {"--capacity", "12.3"};
        OptionParser parser = new OptionParser(floatCapacity);
    }

    @Test
    public void portOptionTest() {
        String[] args = {"--portDir", "5005", "--portServer", "5010"};
        OptionParser parser = new OptionParser(args);
        assertEquals(5005, parser.getDirectoryPort());
        assertEquals(5010, parser.getServerPort());
    }

}
