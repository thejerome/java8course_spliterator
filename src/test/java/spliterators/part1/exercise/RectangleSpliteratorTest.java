package spliterators.part1.exercise;

import java.util.Spliterator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class RectangleSpliteratorTest {

    private int[][] array = new int[1000][100];

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = ThreadLocalRandom.current().nextInt(5);
            }
        }
    }

    @Test
    public void trySplit() throws Exception {

        RectangleSpliterator spliterator = new RectangleSpliterator(array);

        RectangleSpliterator split = spliterator.trySplit();
        assertEquals(50000, split.estimateSize());
        split = spliterator.trySplit();
        assertEquals(25000, split.estimateSize());
        split = spliterator.trySplit();
        assertEquals(12500, split.estimateSize());

        split.tryAdvance((IntConsumer) i -> {});
        assertEquals(12499, split.estimateSize());
        assertEquals(12499, StreamSupport.intStream(split, true).count());
    }

    @Test
    public void estimateSize() throws Exception {
        assertEquals(100000, new RectangleSpliterator(array).estimateSize());
    }

    @Test
    public void tryAdvance() throws Exception {
        int expected = Arrays.stream(array).flatMapToInt(Arrays::stream).sum();
        assertEquals(expected, StreamSupport.intStream(new RectangleSpliterator(array), false).sum());
        assertEquals(expected, StreamSupport.intStream(new RectangleSpliterator(array), true).sum());
    }

}