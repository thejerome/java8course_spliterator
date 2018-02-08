package spliterators.part2.exercise;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class ListZipSpliterator<L, R, T>  implements Spliterator<T> {
    private Iterator<L> firstIterator;
    private Iterator<R> secondIterator;
    private final BiFunction<L, R, T> combiner;
    private List<L> list1;
    private List<R> list2;
    private long size;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.list1 = list1;
        this.list2 = list2;
        this.firstIterator = list1.iterator();
        this.secondIterator = list2.iterator();
        this.combiner = combiner;
        size = Math.min(list1.size(), list2.size());
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        if(firstIterator.hasNext() && secondIterator.hasNext()) {
            action.accept(combiner.apply(firstIterator.next(), secondIterator.next()));
            size--;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        long min = size;
        long newSize = min / 2;
        final List<L> newList1 = StreamSupport.stream(Spliterators.spliteratorUnknownSize(firstIterator, list1.spliterator().characteristics()), false)
                .limit(newSize)
                .collect(toImmutableList());
        final List<R> newList2 = StreamSupport.stream(Spliterators.spliteratorUnknownSize(secondIterator, list2.spliterator().characteristics()), false)
                .limit(newSize)
                .collect(toImmutableList());
        final List<L> newSecondList1 = list1.stream()
                .filter(e -> !newList1.contains(e))
                .collect(toImmutableList());
        final List<R> newSecondList2 = list2.stream()
                .filter(e -> !newList2.contains(e))
                .collect(toImmutableList());
        list1 = newList1;
        firstIterator = newList1.iterator();
        list2 = newList2;
        secondIterator = newList2.iterator();
        size = newSize;

        return  new ListZipSpliterator<>(newSecondList1, newSecondList2, combiner);
    }

    @Override
    public long estimateSize() {
        //TODO
        return size;
    }

    @Override
    public int characteristics() {
        //TODO
        return list1.spliterator().characteristics();
    }
}
