package spliterators.part2.exercise;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T>  implements Spliterator<T> {

    private List<L> list1;
    private List<R> list2;
    private BiFunction<L, R, T> combiner;
    private Iterator<L> iterator1;
    private Iterator<R> iterator2;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;

        this.iterator1 = list1.iterator();
        this.iterator2 = list2.iterator();
    }

    private ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner, Iterator<L> iterator1, Iterator<R> iterator2) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.iterator1 = iterator1;
        this.iterator2 = iterator2;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if  (iterator1.hasNext() && iterator2.hasNext()){
            action.accept(combiner.apply(iterator1.next(), iterator2.next()));
            return true;
        }

        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        return new ListZipSpliterator<>(list1.subList(0, list1.size() / 2),
            list2.subList(0, list2.size() / 2), combiner, iterator1, iterator2);
    }

    @Override
    public long estimateSize() {
        return list1.size() < list2.size() ? list1.size() : list2.size();
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
