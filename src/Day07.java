
void main() throws IOException {

    var pattern = Pattern.compile("\\d+");
    var equations = Files.lines(Path.of("07.txt"))
            .map(line -> pattern.matcher(line).results().map(MatchResult::group).map(Long::parseLong).toList())
            .toList();

    var sum1 = 0L;

    for (var equation : equations) {
        var expected = equation.get(0);
        var operands = equation.subList(1, equation.size());
        var operators = operators(List.of('+', '*'), operands.size() - 1);

        for (var branch : operators) {
            var actual = calculate(operands, branch);

            if (actual == expected) {
                sum1 += actual;
                break;
            }
        }
    }

    System.out.println(sum1);

    var sum2 = 0L;

    for (var equation : equations) {
        var expected = equation.get(0);
        var operands = equation.subList(1, equation.size());
        var operators = operators(List.of('+', '*', '|'), operands.size() - 1);

        for (var branch : operators) {
            var actual = calculate(operands, branch);

            if (actual == expected) {
                sum2 += actual;
                break;
            }
        }
    }

    System.out.println(sum2);
}

long calculate(List<Long> operands, List<Character> operators) {
    var result = operands.get(0);

    for (int i = 0; i < operators.size(); i++) {
        var operator = operators.get(i);
        var operand = operands.get(i + 1);

        result = switch (operator) {
            case '+' -> Math.addExact(result, operand);
            case '*' -> Math.multiplyExact(result, operand);
            case '|' -> Long.parseLong(result + "" + operand);
            default -> throw new IllegalArgumentException();
        };
    }

    return result;
}


ArrayDeque<List<Character>> operators(List<Character> operators, int size) {

    var branches = new ArrayDeque<List<Character>>();
    var results = new ArrayDeque<List<Character>>();

    if (size < 1) {
        throw new IllegalArgumentException();
    }

    for (var operator : operators) {
        var branch = new ArrayList<Character>();
        branch.add(operator);
        branches.push(branch);
    }

    while (!branches.isEmpty()) {
        var current = branches.pop();

        if (current.size() == size) {
            results.push(current);
            continue;
        }

        for (var operator : operators) {
            var next = new ArrayList<Character>(current);
            next.add(operator);
            branches.push(next);
        }
    }

    return results;
}
