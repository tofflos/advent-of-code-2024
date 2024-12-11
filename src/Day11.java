void main() throws IOException {
    var stones = Arrays.stream(Files.readString(Path.of("11.txt")).split(" "))
            .map(Long::parseLong)
            .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

    System.out.println(calculate(stones, 25));
    System.out.println(calculate(stones, 75));
}

long calculate(Map<Long, Long> stones, int blinks) {
    var current = new HashMap<>(stones);

    for (int i = 0; i < blinks; i++) {
        var next = new HashMap<Long, Long>();

        for (var entry : current.entrySet()) {
            var n = entry.getKey();

            if (n == 0) {
                next.merge(1L, entry.getValue(), Long::sum);
                continue;
            }

            var t = String.valueOf(n);

            if (t.length() % 2 == 0) {

                next.merge(Long.parseLong(t.substring(0, t.length() / 2)), entry.getValue(), Long::sum);
                next.merge(Long.parseLong(t.substring(t.length() / 2)), entry.getValue(), Long::sum);
                continue;
            }

            next.merge(n * 2024, entry.getValue(), Long::sum);
        }

        current = next;
    }

    return current.values().stream().mapToLong(Long::longValue).sum();
}
