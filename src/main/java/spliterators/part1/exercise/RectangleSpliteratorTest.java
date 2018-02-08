package spliterators.part1.exercise;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

/**
 * Test the RectangleSpliterator.
 */
public class RectangleSpliteratorTest {
    private static int[][] array;
    private static long expectedSumToCheck;

    /**
     * Init the array for testing.
     */
    @BeforeClass
    public static void setUp() {
        final int outerLength = 20000;
        final int innerLength = 20000;
        array = new int[outerLength][innerLength];

        IntStream.range(0, outerLength).parallel()
                .forEach(x -> IntStream.range(0, innerLength)
                        .forEach(y -> array[x][y] = ThreadLocalRandom.current().nextInt()));

        //calculate the sum
        expectedSumToCheck = Arrays.stream(array)
                .sequential()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();
    }

    /**
     * Test that RectangleSpliterator works properly by summing the given multidimensional
     * array's values and compare with default result.
     */
    @Test
    public void isRectangleSpliteratorWorksProperlySequential() {
        final long res = StreamSupport.intStream(new RectangleSpliterator(array), false)
                .asLongStream()
                .sum();

        assertEquals(expectedSumToCheck, res);
    }

    @Test
    public void isRectangleSpliteratorWorksProperlyParallel() {
        final long res = StreamSupport.intStream(new RectangleSpliterator(array), true)
                .asLongStream()
                .sum();

        assertEquals(expectedSumToCheck, res);
    }
}