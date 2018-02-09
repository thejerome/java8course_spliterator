package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collector;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    List<L> list1;
    List<R> list2;
    BiFunction<L, R, T> combiner;

    int startInclusive;
    int position;
    int endExclusive;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this(list1, list2, combiner, 0, Math.min(list1.size(), list2.size()));
    }

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner, int startInclusive, int endExclusive) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        this.position = startInclusive;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (position >= endExclusive) return false;
        action.accept(
                combiner.apply(
                        list1.get(position),
                        list2.get(position)));

        position++;

        return true;
        //TODO
        //throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> trySplit() {
        int itemsLeft = endExclusive - position;
        if (itemsLeft <= 1) return null;

        int delegatedItemsCount = itemsLeft / 2;

        ListZipSpliterator subspliterator = new ListZipSpliterator<>(
                list1,
                list2,
                combiner,
                startInclusive,
                startInclusive + delegatedItemsCount);

        this.position += delegatedItemsCount;

        return subspliterator;

        //TODO
        //throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return endExclusive - position;
        //TODO
        // throw new UnsupportedOperationException();
    }

    @Override
    public int characteristics() {
        return Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED;
        //TODO
        // throw new UnsupportedOperationException();
    }
}
