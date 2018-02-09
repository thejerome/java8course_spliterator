//package spliterators.part0.example;
//
//import com.google.common.collect.ImmutableList;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Spliterator;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//public class CustomTakeWhile<T> implements Spliterator<T> {
//
//  private final Stream<T> innerStream;
//  private final Spliterator<T> innerSpliterator;
//  private Predicate<T> predicate;
//  private final List<T> container = new ArrayList<>(1);
//  private boolean finished = false;
//
//  public CustomTakeWhile(Stream<T> innerStream, Predicate<T> predicate) {
//    this.innerStream = innerStream;
//    this.predicate = predicate;
//
//    innerSpliterator = innerStream.spliterator();
//  }
//
//  @Override
//  public boolean tryAdvance(Consumer<? super T> action) {
//    if (finished) {
//      return false;
//    }
//    final boolean innerAdvanced = innerSpliterator.tryAdvance(container::add);
//
//    if (innerAdvanced) {
//      T t = container.remove(0);
//      final boolean predicateResult = predicate.test(t);
//      if (predicateResult) {
//        action.accept(t);
//        return true;
//      } else {
//        finished = true;
//      }
//    }
//    return false;
//  }
//
//  @Override
//  public Spliterator<T> trySplit() {
//    Spliterator<T> innerSubSpliterator = innerSpliterator.trySplit();
//
//    final CustomTakeWhile<T> subSpliterator = new CustomTakeWhile<>(
//        StreamSupport.stream(innerSubSpliterator, false), predicate);
//    predicate
//  }
//
//  @Override
//  public long estimateSize() {
//    return innerSpliterator.estimateSize();
//  }
//
//  @Override
//  public int characteristics() {
//    return innerSpliterator.characteristics();
//  }
//
//  public static <T> Stream takeWhile(Stream<T> innerStream, Predicate<T> predicate) {
//    return StreamSupport.stream(new CustomTakeWhile<>(innerStream, predicate), false);
//  }
//
//  public static <T> Stream takeWhile1(Stream<T> innerStream, Predicate<T> predicate) {
//    final CustomTakeWhile spliterator1 = new CustomTakeWhile<>(innerStream, predicate);
//    final Spliterator spliterator2 = spliterator1.trySplit();
//
//    return Stream.of(spliterator2, spliterator1).flatMap(spl -> StreamSupport.stream(spl, false));
//  }
//
//  public static <T> Stream takeWhile2(Stream<T> innerStream, Predicate<T> predicate) {
//    final CustomTakeWhile spliteratorRR = new CustomTakeWhile<>(innerStream, predicate);
//    final Spliterator spliteratorRL = spliteratorRR.trySplit();
//    final Spliterator spliteratorLR = spliteratorRR.trySplit();
//    final Spliterator spliteratorLL = spliteratorLR.trySplit();
//
//    return Stream.of(spliteratorLL, spliteratorLR, spliteratorRL, spliteratorRR)
//        .flatMap(spl -> StreamSupport.stream(spl, false));
//  }
//
//  public static void main(String[] args) {
//
//    Stream<Integer> integerStream = ImmutableList
//        .of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20).stream();
//
//    takeWhile2(integerStream, i -> i % 5 != 0).forEach(System.out::println);
//  }
//}
