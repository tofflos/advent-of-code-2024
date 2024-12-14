void main() throws IOException {

    var pattern = Pattern.compile("\\d+");
    var s = Files.readString(Path.of("13.txt"));

    var machines = pattern.matcher(s).results()
            .map(MatchResult::group)
            .map(Integer::parseInt)
            .gather(Gatherers.windowFixed(6))
            .map(w -> new Machine(w.get(0), w.get(1), w.get(2), w.get(3), w.get(4), w.get(5)))
            .collect(Collectors.toSet());

    var tokens1 = machines.stream().mapToLong(this::play).filter(n -> n != Long.MAX_VALUE).sum();
    var tokens2 = machines.stream()
            .map(m -> new Machine(m.ax, m.ay, m.bx, m.by, 10_000_000_000_000L + m.px, 10_000_000_000_000L + m.py))
            .mapToLong(this::play)
            .filter(n -> n != Long.MAX_VALUE)
            .sum();

    System.out.println(tokens1);
    System.out.println(tokens2);
}

long play(Machine machine) {
    var b = (machine.py * machine.ax - machine.px * machine.ay) / (machine.by * machine.ax - machine.ay * machine.bx);
    var a = (machine.px - machine.bx * b) / machine.ax;

    return a % 1 == 0 && b % 1 == 0 ? (long) (a * 3 + b) : Long.MAX_VALUE;
}

record Machine(double ax, double ay, double bx, double by, long px, long py) {

}
