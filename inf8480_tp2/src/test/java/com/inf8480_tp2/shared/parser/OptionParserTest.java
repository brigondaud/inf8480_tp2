package com.inf8480_tp2.shared.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class OptionParserTest {

    @Test
    public void corruptOptionTest() {
        String[] notCorrupted = {"--portDir", "5000"};
        String[] corrupted = {"--unsafe", "--corrupt", "75,5"};
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
    public void NotFloatCorruptRate() {
        String[] notFloat = {"--corrupt", "invalid"};
        OptionParser parser = new OptionParser(notFloat);
    }
}
