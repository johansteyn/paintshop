package johansteyn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The Paint Shop Code Challenge
 * @see <a href="https://github.com/johansteyn/paintshop" target="_blank">Paint Shop on GitHub</a>
 */
public class Paintshop {
	private static int MAX_WIDTH = Integer.MAX_VALUE;
	private static int verbosity;
	private int width;
	private List<String> requirements = new ArrayList<String>();
	private String bestSolution;

	/**
	 * Parses the specified input file to capture the problem requirements. 
	 * @param filename Path to input file containing width and customer requirements.
	 * @throws IOException If a general system I/O error occurs.
	 * @throws ParseException If the input file contains incorrect content.
	 */
	public Paintshop(String filename) throws IOException, ParseException {
		parse(filename);
	}

	// Parses the specified input file to populate the datastructure (width + requirements)
	private void parse(String filename) throws IOException, ParseException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				line = line.trim();
				if (line.length() <= 0) {
					continue;
				}
				if (line.startsWith("#")) {
					continue;
				}
				if (width == 0) {
					try {
						width = Integer.parseInt(line);
					} catch (NumberFormatException nfe) {
						throw new ParseException("Invalid width: " + line);
					}
					if (width <= 0 || width > MAX_WIDTH) {
						throw new ParseException("Width value must be between 1 and " + MAX_WIDTH);
					}
				} else {
					String requirement = parseLine(line);
					if (weight(requirement, 'M') > 1) {
						throw new ParseException("Customer may not require more than one Matte: " + line);
					}
					if (!requirements.contains(requirement)) {
						// Ignore duplicate requirements
						requirements.add(requirement);
						if (verbosity == 2) {
							System.out.println(requirement);
						}
					}
				}
			}
			if (verbosity == 2) {
				System.out.println(emptySolution().replaceAll("_", "|"));
			}
        } catch (IOException ioe) {
			// Simply re-throw the exception - resources will close automatically
			throw ioe;
		}
	}

	// Returns a simpler representation of a requirement,
	// where 'G' and 'M' are in their positions and an
	// underscore (_) indicates an "not specified"
	// Eg: for width=5 and line "2 G 3 M 5G" it will return "_GM_G"
	private String parseLine(String line) throws ParseException {
		StringBuilder sb = new StringBuilder(width);
		for (int i = 0; i < width; i++) {
			sb.append('_');
		}
		StringTokenizer st = new StringTokenizer(line, " ");
		int number = 0;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (number <= 0) {
				try {
					number = Integer.parseInt(token);
				} catch (NumberFormatException nfe) {
					throw new ParseException("Invalid number '" + token + "' in line: " + line);
				}
				if (number < 1 || number > width) {
					throw new ParseException("Number '" + number + "' out of range in line: " + line);
				}
			} else {
				if (!"G".equals(token) && !"M".equals(token)) {
					throw new ParseException("Invalid token '" + token + "' in line: " + line);
				}
				sb.setCharAt(number - 1, token.charAt(0));
				number = 0;
			}
		}
		return sb.toString();
	}

	/**
	 * Finds the most optimal solution for the problem,
	 * as per the requirements contained in the
	 * <code>width</code> and <code>requirements</code> fields.
	 * @return The solution string, or null if no solution was found.
	 */
	public String solve() {
		bestSolution = null;
		// Go down the rabbit hole with an empty proposed solution (ie. all underscores)
		String solution = emptySolution();
		return solve(requirements, solution);
	}

	// The rabbit hole: a recursive method that takes a proposed solution for
	// the list of requirements, and returns either a full solution or null.
	private String solve(List<String> requirements, String solution) {
		if (bestSolution != null && weight(solution, 'M') >= weight(bestSolution, 'M')) {
			// Don't bother going down this rabbit hole - we already have a better solution!
			return null;
		}
		// Improve the proposed solution by filling it with G's where we can
		solution = fillGloss(requirements, solution);
		if (verbosity == 2) {
			System.out.println(solution);
		}
		// Apply the proposed solution to yield a (hopefully reduced) list of requirements
		List<String> list = apply(solution, requirements);
		if (list.size() == 0) {
			// All requirements have been met!
			return solution.replaceAll("_", "G");
		}
		if (solution.indexOf('_') < 0) {
			// We have a full proposed solution, but it doesn't meet all requirements, so it is not a solution.
			return null;
		}
		// If there is any requirement for a single colour, go down the rabbit whole with it.
		for (int row = 0; row < list.size(); row++) {
			String requirement = list.get(row);
			if (weight(requirement) == 1) {
				for (int col = 0; col < width; col++) {
					char c = requirement.charAt(col);
					if ((c == 'G' || c == 'M')) {
						return solve(list, replace(solution, col, c));
					}
				}
			}
		}
		// Send remaining unsolved columns down the rabbit hole...
		List<String> solutions = new ArrayList<String>();
		for (int col = 0; col < width; col++) {
			char c = solution.charAt(col);
			if (c == '_') {
				String s = solve(list, replace(solution, col, 'G'));
				if (s != null) {
					solutions.add(s);
				}
				s = solve(list, replace(solution, col, 'M'));
				if (s != null) {
					solutions.add(s);
				}
			}
		}
		if (solutions.size() <= 0) {
			// No solution
			return null;
		}
		// Return the solution with the fewest M's
		solution = solutions.get(0);
		for (int i = 1; i < solutions.size(); i++) {
			String s = solutions.get(i);
			if (weight(s, 'M') < weight(solution, 'M')) {
				solution = s;
			}
		}
		if (bestSolution == null || weight(solution, 'M') < weight(bestSolution, 'M')) {
			bestSolution = solution;
		}
		return solution;
	}

	private String emptySolution() {
		StringBuilder sb = new StringBuilder(width);
		for (int i = 0; i < width; i++) {
			sb.append('_');
		}
		return sb.toString();
	}

	// Returns the number of G's + M's in a requirement
	private int weight(String string) {
		return weight(string, 'G') + weight(string, 'M');
	}

	// Returns the number of specified character in a requirement
	private int weight(String string, char character) {
		int weight = 0;
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c == character) {
				weight++;
			}
		}
		return weight;
	}

	// Returns a new solution with G's in every position that does not require M
	private String fillGloss(List<String> requirements, String solution) {
		StringBuilder sb = new StringBuilder(solution);
		for (int col = 0; col < width; col++) {
			char c = solution.charAt(col);
			if (c == '_') {
				if (!hasMatte(requirements, col)) {
					sb.setCharAt(col, 'G');
				}
			}
		}
		return sb.toString();
	}

	// Returns true if any requirement has an M in the specified column
	private boolean hasMatte(List<String> requirements, int col) {
		for (String requirement : requirements) {
			char c = requirement.charAt(col);
			if (c == 'M') {
				return true;
			}
		}
		return false;
	}

	// Returns string with character replaced at the specified index
	private String replace(String string, int index, char c) {
		StringBuilder sb = new StringBuilder(string);
		sb.setCharAt(index, c);
		return sb.toString();
	}

	// Apply the proposed solution to the requirements,
	// to yield a new list of requirements that contains
	// only those that are met by the proposed solution.
	private List<String> apply(String solution, List<String> requirements) {
		List<String> list = new ArrayList<String>();
		for (String requirement : requirements) {
			if (!satisfies(solution, requirement)) {
				requirement = mask(solution, requirement);
				if (!list.contains(requirement)) {
					// Ignore duplicate requirements
					list.add(requirement);
				}
			}
		}
		return list;
	}

	// Returns a new requirement with the solution "masked" over it 
	// so that each solved position (G or M in the solution)
	// removes a requirement (_ in the corresponding position),
	// while unsolved positions are left unchanged.
	// Example:
	//   GMM___  <= solution
	//   __GM_G  <= requirement
	//   ___M_G  <= masked requirement
	private String mask(String solution, String requirement) {
		StringBuilder sb = new StringBuilder(width);
		for (int i = 0; i < width; i++) {
			char c = solution.charAt(i);
			if (c == 'G' || c == 'M') {
				sb.append('_');
			} else {
				sb.append(requirement.charAt(i));
			}
		}
		return sb.toString();
	}

	// Returns true if the solution satisfies the requirement.
	private boolean satisfies(String solution, String requirement) {
		for (int i = 0; i < solution.length(); i++) {
			char c = solution.charAt(i);
			if ((c == 'G' || c == 'M') && c == requirement.charAt(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The entry point for standalone, command line operation. <br>
	 * Checks command line arguments, creates an instance of {@link #Paintshop Paintshop}, 
	 * and calls it's {@link #solve() solve} method.
	 * @param args Array of command line arguments.<br>
	 * Use the <code><b>-h</b></code> argument to show the <i>usage</i> text listing all arguments.
	 */
	public static void main(String[] args) {
		String filename = null;
		for (int i = 0; i < args.length; i++) {
			if (!args[i].startsWith("-")) {
				if (filename != null) {
					usage();
					System.err.println("Only one input file can be specified!");
					System.exit(1);
				}
				filename = args[i];
			} else if ("-h".equals(args[i])) {
				usage();
				System.exit(0);
			} else if ("-v".equals(args[i])) {
				verbosity = 1;
			} else if ("-vv".equals(args[i])) {
				verbosity = 2;
			} else {
				usage();
				System.err.println("Unknown option: " + args[i]);
				System.exit(1);
			}
		}
		if (filename == null) {
			usage();
			System.err.println("Input file not specified!");
			System.exit(1);
		}
		Paintshop paintshop = null;
		try {
			paintshop = new Paintshop(filename);
        } catch (IOException ioe) {
			System.err.println("Error reading input file: " + filename + "\n  " + ioe.getMessage());
			System.exit(2);
        } catch (ParseException pe) {
			System.err.println("Error parsing input file: " + filename + "\n  " + pe.getMessage());
			System.exit(3);
		}
		long start = System.currentTimeMillis();
		String solution = paintshop.solve();
		long end = System.currentTimeMillis();
		if (verbosity >= 1) {
			System.out.println("" + (end - start) + " milliseconds");
		}
		if (solution == null) {
			System.out.println("No solution");
			System.exit(1);
		}
		solution = solution.replaceAll("G", "G ");
		solution = solution.replaceAll("M", "M ");
		System.out.println(solution);
	}

	private static void usage() {
		System.out.println("Paint Shop Code Challenge - Johan Steyn");
		System.out.println("");
		System.out.println("Usage: java -jar paintshop.jar [-h] [-v[v]] <file>");
		System.out.println("");
		System.out.println("Args:");
		System.out.println("  -h     Help (shows this usage text)");
		System.out.println("  -v[v]  Verbosity level (-v = show time taken, -vv = show loads more)");
		System.out.println("  <file> Input file");
		System.out.println("");
	}
}

