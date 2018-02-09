package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private final int startOuterInclusive;
    private int endOuterExclusive;
    private final int startInnerInclusive;

    private int positionOuter;
    private int positionInner;

    private boolean hasNext() {
        return positionOuter < endOuterExclusive
                && positionInner < array[positionOuter].length;
    }

    private int next() {
        if (!hasNext()) throw new IndexOutOfBoundsException();

        int value = array[positionOuter][positionInner];

        positionInner++;

        if (positionInner >= array[positionOuter].length) {
            positionInner = 0;
            positionOuter++;
        }

        return value;
    }


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

        this.positionOuter = startOuterInclusive;
        this.positionInner = startInnerInclusive;
    }

    @Override
    public RectangleSpliterator trySplit() {
        int rowsLeft = endOuterExclusive - positionOuter;
        if (rowsLeft == 1) return null;

        int rowsDelegated = rowsLeft / 2;

        int delegatedEndOuterExclusive = endOuterExclusive;
        endOuterExclusive -= rowsDelegated;
        int delegatedStartOuterInclusive = endOuterExclusive;

        return new RectangleSpliterator(array, delegatedStartOuterInclusive, delegatedEndOuterExclusive, 0);

        // TODO
        // throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - positionOuter) * innerLength - positionInner;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (!hasNext()) return false;
        action.accept(next());
        return true;
        // TODO
        // throw new UnsupportedOperationException();
    }
}
