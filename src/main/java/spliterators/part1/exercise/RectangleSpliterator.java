package spliterators.part1.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int innerLength;
    private int[][] array;
    private int startOuterInclusive;
    private int endOuterExclusive;
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
        final int length = endOuterExclusive - startOuterInclusive;
        if (length < 2) {
            return null;
        }

        final int mid = startOuterInclusive + length / 2;
        final RectangleSpliterator rectSplit = new RectangleSpliterator(array, startOuterInclusive, mid, 0);
        startOuterInclusive = mid;
        return rectSplit;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startOuterInclusive < endOuterExclusive) {
            if (startInnerInclusive < innerLength) {
                action.accept(array[startOuterInclusive][startInnerInclusive++]);
                return true;
            } else {
                startOuterInclusive++;
                startInnerInclusive = 0;
                return tryAdvance(action);
            }
        }
        return false;
    }
}
