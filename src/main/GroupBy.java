import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jian.zhangg
 * @version 1.0
 */
public class GroupBy {

    /**
     * 统计List中重复元素的个数
     */
    public void countList() {
        List<String> list = Arrays.asList("c", "a", "b", "a", "b", "b");

        Map<String, Long> collect = list.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(), Collectors.counting()
                ));

        System.out.println(collect);
    }

    private void sortList() {
        List<String> list = Arrays.asList("c", "a", "b", "a", "b", "a");

        Map<String, Long> collect = list.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(), Collectors.counting()
                ));

        Map<String, Long> finalMap = new LinkedHashMap<>();

        collect.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

        System.out.println(finalMap);
    }

    private void countAndSumBean() {
        List<Item> items = getItems();

        Map<String, Long> count = items.stream()
                .collect(Collectors
                        .groupingBy(Item::getName, Collectors.counting()));
        System.out.println(count);

        Map<String, Integer> sum = items.stream()
                .collect(Collectors
                        .groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));
        System.out.println(sum);
    }

    public void mapping() {
        List<Item> items = getItems();

        Map<BigDecimal, List<Item>> collect = items.stream()
                .collect(Collectors.groupingBy(Item::getPrice));
        System.out.println(collect);

        Map<BigDecimal, Set<String>> map = items.stream()
                .collect(Collectors.groupingBy(Item::getPrice,
                        Collectors.mapping(Item::getName, Collectors.toSet())));
        System.out.println(map);

        List<Item> sort = items.stream()
                // 可以在sort的时候调用外部数据比较
                .sorted(Comparator.comparing((Item i)->items.get(i.getQty()).getName())
                        .thenComparing(Item::getPrice).reversed())
                .collect(Collectors.toList());
        System.out.println(sort);

        // 多条件分组
        /*Map<List<String>, Integer> map =
                myList.stream()
                        .collect(Collectors.groupingBy(
                                f -> Arrays.asList(f.getType(), f.getCode()),
                                Collectors.summingInt(Foo::getQuantity)
                        ));

        // map转Bean
        List<Foo> result =
                map.entrySet()
                        .stream()
                        .map(e -> new Foo(e.getKey().get(0), e.getValue(), e.getKey().get(1)))
                        .collect(Collectors.toList());*/

        // 装逼写法
       /* List<Foo> result = new ArrayList<>(
                myList.stream()
                        .collect(Collectors.groupingBy(
                                f -> Arrays.<String>asList(f.getType(), f.getCode()),
                                MoreCollectors.pairing(
                                        Collectors.collectingAndThen(MoreCollectors.first(), Optional::get),
                                        Collectors.summingInt(Foo::getQuantity),
                                        (f, s) -> new Foo(f.getType(), s, f.getCode())
                                )
                        )).values());*/

    }

    private List<Item> getItems() {
        return Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );
    }

    class Item {

        private String name;
        private int qty;
        private BigDecimal price;

        public Item(String name, int qty, BigDecimal price) {
            this.name = name;
            this.qty = qty;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", qty=" + qty +
                    ", price=" + price +
                    '}';
        }
    }


    public static void main(String[] args) {
        GroupBy groupBy = new GroupBy();
        groupBy.countList();
        groupBy.sortList();
        groupBy.countAndSumBean();
        groupBy.mapping();
    }
}
