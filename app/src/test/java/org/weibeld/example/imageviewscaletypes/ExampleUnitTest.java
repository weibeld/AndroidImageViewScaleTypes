package org.weibeld.example.imageviewscaletypes;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.weibeld.example.imageviewscaletypes.Util.parseDimensionValue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    // Test Util.parseDimensionValue
    @Test
    public void parseDimensionValue_test() throws Exception {

        // First, test valid dimension value strings
        String[] validStrings = new String[] {
                "1", "5274", "1.0", "0.000", "342.34532", ".5", ".2442", "2.", "452.", "200"
        };
        float[] validFloats = new float[] {
                1, 5274, 1, 0, 342.34532f, 0.5f, 0.2442f, 2, 452, 200
        };
        for (int i = 0; i < validStrings.length; i++) {
            assertEquals(validFloats[i], parseDimensionValue(validStrings[i]), 0.0001f);
        }

        // Then, test invalid dimension value strings (parseDimensionValue should throw exception)
        String[] invalidStrings = new String[] {
                "", " ", ".", "1.1.", "1.2.2", "2,300", "14dp", "1 ", "1.0 ", " 1 ", "14d", "-1", "-0", "-1.1"
        };
        for (String str : invalidStrings) {
            try {
                // Fail the test if parseDimensionValue does not throw a NumberFormatException
                parseDimensionValue(str);
                fail(str + " should throw NumberFormatException.");
            } catch (NumberFormatException e) {}
        }
    }

    @Test
    public void isValidLayoutWidthHeightEntry_test() throws Exception {
        String msg;

        // First, test if valid layout_width/layout_height entries are positively validated
        // Note: the entry for layout_width/layout_height must NOT be empty
        String[] validEntries = new String[] {
                "1dp", "5274sp", "1.0px", "0.000in", "342.34532mm", ".5dp", ".2442sp", "2.px",
                "452.in", "-10mm", "-.5dp", "-2.sp", "-0.px", "-.0in", "wrap_content", "match_parent"
        };
        for (int i = 0; i < validEntries.length; i++) {
            msg = "\"" + validEntries[i] + "\"" + " should be valid.";
            assertTrue(msg, Validator.isValidLayoutDimenEntry(validEntries[i]));
        }

        // Next, test if invalid layout_width/layout_height entries are negatively validated
        String[] invalidEntries = new String[] {
                "", "1", "200.5", "10 dp", "10d", "10fd", ".dp", "-.px", "1,0dp", "1cm",
                "WRAP_CONTENT", "MATCH_PARENT"
        };
        for (int i = 0; i < invalidEntries.length; i++) {
            msg = "\"" + invalidEntries[i] + "\"" + " should be invalid.";
            assertFalse(msg, Validator.isValidLayoutDimenEntry(invalidEntries[i]));
        }
    }


    @Test
    public void isValidDimenEntry_test() throws Exception {
        String msg;

        // First, test if valid dimension entries are positively validated
        // Note: the entry for a dimension field may be empty
        String[] validEntries = new String[] {
                "", "1dp", "5274sp", "1.0px", "0.000in", "342.34532mm", ".5dp", ".2442sp", "2.px",
                "452.in", "-10mm", "-.5dp", "-2.sp", "-0.px", "-.0in"
        };
        for (int i = 0; i < validEntries.length; i++) {
            msg = "\"" + validEntries[i] + "\"" + " should be valid.";
            assertTrue(msg, Validator.isValidDimenEntry(validEntries[i]));
        }

        // Next, test if invalid dimension entries are negatively validated
        String[] invalidEntries = new String[] {
                "1", "200.5", "10 dp", "10d", "10fd", ".dp", "-.px", "1,0dp", "1cm", "wrap_content",
                "match_parent"
        };
        for (int i = 0; i < invalidEntries.length; i++) {
            msg = "\"" + invalidEntries[i] + "\"" + " should be invalid.";
            assertFalse(msg, Validator.isValidDimenEntry(invalidEntries[i]));
        }
    }

    @Test
    public void isValidBooleanEntry_test() throws Exception {
        String msg;

        // First, test if valid boolean entries are positively validated
        String[] validEntries = new String[] { "true", "false" };
        for (int i = 0; i < validEntries.length; i++) {
            msg = "\"" + validEntries[i] + "\"" + " should be valid.";
            assertTrue(msg, Validator.isValidBooleanEntry(validEntries[i]));
        }

        // Next, test if invalid boolean entries are negatively validated
        String[] invalidEntries = new String[] {
                "", " ", "True", "False", "TRUE", "FALSE", " true", "false "
        };
        for (int i = 0; i < invalidEntries.length; i++) {
            msg = "\"" + invalidEntries[i] + "\"" + " should be invalid.";
            assertFalse(msg, Validator.isValidBooleanEntry(invalidEntries[i]));
        }
    }

    @Test
    public void isValidColorEntry_test() throws Exception {
        String msg;

        // First, test if valid colour entries are positively validated
        String[] validEntries = new String[] {
                "#FFFFFF", "#FFFFFFFF", "#000000", "#00000000", "#aaaaaa", "#aaaaaaaa",
                "#fA0fA0", "#fA0efA0e", "green", "cyan", ""
        };
        for (int i = 0; i < validEntries.length; i++) {
            msg = "\"" + validEntries[i] + "\"" + " should be valid.";
            assertTrue(msg, Validator.isValidColorEntry(validEntries[i]));
        }

        // Next, test if invalid colour entries are negatively validated
        String[] invalidEntries = new String[] {
                "#fff", "#ffff", "#fffff", "#fffffff", "#fffffffff", "Green", "Cyan"
        };
        for (int i = 0; i < invalidEntries.length; i++) {
            msg = "\"" + invalidEntries[i] + "\"" + " should be invalid.";
            assertFalse(msg, Validator.isValidColorEntry(invalidEntries[i]));
        }
    }

    // Make sure that the "value" strings that we want to accept in our dimension entries are also
    // accepted by Float.parseFloat. This is because we use Float.parseFloat to convert these
    // "value" strings to floats when the ImageView is drawn in PageFragment.
    @Test
    public void parseFloat_test() throws Exception {
        String[] validStrings = new String[] {
                "1", "1.0", "1.", ".1", "-1", "-1.0", "-1.", "-.1", "0.0", "0.", ".0",
                "-0.0", "-0.", "-.0"
        };
        for (int i = 0; i < validStrings.length; i++) {
            // Throws a NumberFormatException if it does not recognise the string
            Float.parseFloat(validStrings[i]);
        }
    }


    @Test
    public void autoComplTest() throws Exception {
        String[] input = new String[] {
                "100", "1", "01", "00.1", "0006", "00024.3543", "0.0001", "0", "0.", ".0"
        };
        //String regex = "^\\-?((\\d+)|(\\d+\\.)|(\\.\\d+)|(\\d+\\.\\d+))";
        String regex = "((0\\.\\d+)|([1-9]\\d*\\.\\d+)|(0\\.)|([1-9]\\d*\\.)|(\\.\\d+)|(0)|([1-9]\\d*))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher("");

        for (String s : input) {
            m.reset(s);
            System.out.println(s + ": matches == " + m.matches() + ", hitEnd == " + m.hitEnd() + ", lookingAt == " + m.lookingAt() + ", find() == " + m.find());
        }
    }

    @Test
    public void autoComplTest2() throws Exception {
        String[] input = new String[] {
                "1", ".", "d", "dp", "ff1dp"
        };
        Pattern patNum = Pattern.compile("\\-?((\\d+)|(\\d+\\.)|(\\.\\d+)|(\\d+\\.\\d+))");
        Pattern patUnitD = Pattern.compile(patNum.pattern() + "d");
        Pattern patUnitS = Pattern.compile(patNum.pattern() + "s");
        Pattern patUnitP = Pattern.compile(patNum.pattern() + "p");
        Pattern patUnitI = Pattern.compile(patNum.pattern() + "i");
        Pattern patUnitM = Pattern.compile(patNum.pattern() + "m");
        Pattern patUnitDP = Pattern.compile(patUnitD.pattern() + "p");
        Pattern patUnitSP = Pattern.compile(patUnitS.pattern() + "p");
        Pattern patUnitPX = Pattern.compile(patUnitP.pattern() + "x");
        Pattern patUnitIN = Pattern.compile(patUnitI.pattern() + "n");
        Pattern patUnitMM = Pattern.compile(patUnitM.pattern() + "m");

//        Pattern p = Pattern.compile("^[a-zA-Z]+([0-9]+).*");
//        Matcher m = p.matcher("Testing123Testing");
//
//        if (m.find()) {
//            System.out.println(m.group(1));
//        }

        String mydata = "some string with the data i want inside";
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(mydata);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }

//        Map<Pattern, String[]> mapSuggestions = new HashMap<>();
//        mapSuggestions.put(patNum, new String[]);
//
//        for (String s : input) {
//            m.reset(s);
//            System.out.println(s + ": matches == " + m.matches() + ", hitEnd == " + m.hitEnd() + ", lookingAt == " + m.lookingAt() + ", find(0) == " + m.find());
//        }
    }
}