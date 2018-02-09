package spliterators.part2.exercise;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T>  implements Spliterator<T> {

    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;

    private Iterator<L> listIterator1;
    private Iterator<R> listIterator2;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.listIterator1 = list1.listIterator();
        this.listIterator2 = list2.listIterator();
    }

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner,
        Iterator<L> listIterator1, Iterator<R> listIterator2) {

        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.listIterator1 = listIterator1;
        this.listIterator2 = listIterator2;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (listIterator1.hasNext() && listIterator2.hasNext()) {
            action.accept(combiner.apply(listIterator1.next(), listIterator2.next()));

            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        return new ListZipSpliterator<>(list1.subList(0, list1.size()/2),
            list2.subList(0, list2.size()/2), combiner, listIterator1, listIterator2);
    }

    @Override
    public long estimateSize() {
        return Integer.min(list1.size(), list2.size());
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
