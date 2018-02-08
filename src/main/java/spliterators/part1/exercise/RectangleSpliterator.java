package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int innerLength;
    private int[][] array;
    private int startOuterInclusive;
    private final int endOuterExclusive;
    private int startInnerInclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
    }

    @Override
    public RectangleSpliterator trySplit() {
        if ((endOuterExclusive - startOuterInclusive) < 2) {
            return null;
        }

        final int mid = startOuterInclusive + (endOuterExclusive - startOuterInclusive) / 2;
        RectangleSpliterator res = new RectangleSpliterator(array, startOuterInclusive, mid, 0);
        startOuterInclusive = mid;
        return res;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive >= endOuterExclusive) {
            return false;
        }
        if (startInnerInclusive < array[0].length) {
            final int value = array[startOuterInclusive][startInnerInclusive];
            startInnerInclusive++;
            action.accept(value);
            return true;
        }

        startInnerInclusive = 0;
        startOuterInclusive++;
        return tryAdvance(action);
    }
}
