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
        // TODO
        int length = endOuterExclusive - startOuterInclusive;
        int mid = startOuterInclusive + length / 2;
        startOuterInclusive = mid;
        startInnerInclusive = 0;
        return new RectangleSpliterator(array, startOuterInclusive, mid, startInnerInclusive);
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (startInnerInclusive < innerLength && startOuterInclusive < endOuterExclusive) {
            action.accept(array[startOuterInclusive][startInnerInclusive]);
            startInnerInclusive++;
            if (startInnerInclusive >= innerLength) {
                startInnerInclusive = 0;
                startOuterInclusive++;
            }
            return true;
        } else {
            return false;
        }

    }
}
