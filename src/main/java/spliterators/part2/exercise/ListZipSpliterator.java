package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    private List<T> initValues;
    private int startVal;
    private int endVal;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        initValues = IntStream.range(0, Math.min(list1.size(), list2.size())).mapToObj(i ->
                combiner.apply(list1.get(i), list2.get(i))).collect(Collectors.toList());
        endVal = initValues.size();
    }

    public ListZipSpliterator(List<T> initValues, int startVal, int endVal) {
        this.initValues = initValues;
        this.startVal = startVal;
        this.endVal = endVal;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (startVal < endVal) {
            action.accept(initValues.get(startVal++));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        int mid = (initValues.size() + startVal) / 2;
        ListZipSpliterator<L, R, T> lrtListZipSpliterator = new ListZipSpliterator<>(
                initValues, startVal, mid);
        this.startVal = mid;
        return lrtListZipSpliterator;
    }

    @Override
    public long estimateSize() {
        return endVal - startVal;
    }

    @Override
    public int characteristics() {
        return initValues.spliterator().characteristics();
    }
}