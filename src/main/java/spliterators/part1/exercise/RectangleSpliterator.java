package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int innerLength;
    private final int[][] array;
    private final int startOuterInclusive;
    private int endOuterExclusive;
    private final int startInnerInclusive;
    private int counter;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0,
            array.length == 0 ? 0 : array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive,
                                 int startInnerInclusive, int innerLength) {
        super(Long.MAX_VALUE,
            Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED
                | Spliterator.NONNULL);

        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
        this.innerLength = innerLength;
    }

    @Override
    public RectangleSpliterator trySplit() {
        if ((endOuterExclusive - startOuterInclusive) >= innerLength) {
            int midOuter = (endOuterExclusive + startOuterInclusive) / 2;
            final RectangleSpliterator rectangleSpliterator = new RectangleSpliterator(
                array, midOuter, endOuterExclusive, startInnerInclusive, innerLength
            );
            endOuterExclusive = midOuter;
            return rectangleSpliterator;
        } else {
            innerLength /= 2;
            return new RectangleSpliterator(
                array, startOuterInclusive, endOuterExclusive, innerLength,
                2 * innerLength - innerLength
            );
        }
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - counter;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (counter >= ((endOuterExclusive - startOuterInclusive) * innerLength)) {
            return false;
        }

        final int out = counter / innerLength + startOuterInclusive;
        final int in = counter - (counter / innerLength) * innerLength + startInnerInclusive;
        action.accept(array[out][in]);
        counter++;
        return true;
    }
}
