package johansteyn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

// TODO: IllegalArgumentException (ie. check all arguments)...
/**
 * The Paint Shop Code Challenge
 * @see <a href="https://github.com/johansteyn/paintshop" target="_blank">Paint Shop on GitHub</a>
 */
public class Paintshop {
	private static int MAX_WIDTH = 64; // Not sure what to use here yet...
	private static boolean verbose;
	private int width;
	private List<String> requirements = new ArrayList<String>();

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
					width = Integer.parseInt(line);
					if (width <= 0 || width > MAX_WIDTH) {
						throw new ParseException("Width value must be between 1 and " + MAX_WIDTH);
					}
				} else {
					String requirement = parseLine(line);
					if (!requirements.contains(requirement)) {
						// Ignore duplicate requirements
						requirements.add(requirement);
					}
				}
			}
        } catch (IOException ioe) {
			// Simply re-throw the exception - resources will close automatically
			throw ioe;
		}
	}

	// Returns a simpler representation of a requirement,
	// where 'G' and 'M' are in their positions.
	// and _ indicates unspecified (ie. doesn't matter).
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
	 * @return The solution string, which can <code>No solution</code>
	 */
	public String solve() {
		StringBuilder sb = new StringBuilder(width);
		for (int i = 0; i < width; i++) {
			sb.append('_');
		}
		String solution = sb.toString();
		solution = solve(requirements, solution);
		if (solution == null) {
			return "No solution";
		}
		solution = solution.replaceAll("G", "G ");
		solution = solution.replaceAll("M", "M ");
		return solution;
	}

	// Recursive method
	// Returns the solution for the specified requirements and proposed solution
	// If no solution is found, it will return null
	private String solve(List<String> requirements, String solution) {
		// Mask the proposed solution over all requirements, 
		// to produce a new list of requirements
		List<String> list = new ArrayList<String>();
		for (String requirement : requirements) {
			if (!satisfies(solution, requirement)) {
				String r = mask(solution, requirement);
				if (!list.contains(r)) {
					list.add(r);
				}
			}
		}
		if (list.size() == 0) {
			// All requirements have been met :)
			return solution.replaceAll("_", "G");
		}
		if (solution.indexOf('_') < 0) {
			// We have a full proposed solution, but it doesn't meet all requirements...
			return null;
		}

		// Any single requirement?
		for (int row = 0; row < list.size(); row++) {
			String requirement = list.get(row);
			if (weight(requirement) == 1) {
				// This is a single requirement  
				for (int col = 0; col < width; col++) {
					char c = requirement.charAt(col);
					if ((c == 'G' || c == 'M')) {
						return solve(list, replace(solution, col, c));
					}
				}
			}
		}

		// Remaining unsolved columns
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
		// Return the solution with the fewest M's (not the most G's, as some may still be _)
		int fewest = width;
		String bestSolution = solutions.get(0);
		for (int i = 1; i < solutions.size(); i++) {
			String s = solutions.get(i);
			int count = 0;
			for (int j = 0; j < width; j++) {
				char c = s.charAt(j);
				if (c == 'M') {
					count++;
				}
			}
			if (count < fewest) {
				fewest = count;
				bestSolution = s;
			}
		}
		return bestSolution;
	}

	// Returns the number of G's + M's in a requirement
	private int weight(String string) {
		int weight = 0;
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c == 'G' || c == 'M') {
				weight++;
			}
		}
		return weight;
	}

	// Returns string with character at specified
	// index replaced by the specified character
	private String replace(String string, int index, char c) {
		StringBuilder sb = new StringBuilder(string);
		sb.replace(index, index + 1, "" + c);
		return sb.toString();
	}

	// Returns true if the solution satisfies the requirement.
	// Checks if any G or M character in the solution matches
	// the corresponding character in the requirement
	private boolean satisfies(String solution, String requirement) {
		for (int i = 0; i < solution.length(); i++) {
			char c = solution.charAt(i);
			if ((c == 'G' || c == 'M') && c == requirement.charAt(i)) {
				return true;
			}
		}
		return false;
	}

	// Masks the specified solution over the requirement such that
	// each solved position (G or M in the solution)
	// removes a requirement (_ in the corresponding position),
	// while unsolved positions are left unchanged.
	// Example:
	//   GMG___  <= solution
	//   __GM_G  <= requirement
	//   ___M_G  <= returned
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

	/**
	 * The entry point for standalone, command line operation. <br>
	 * Checks command line arguments, creates an instance of {@link #Paintshop Paintshop}, 
	 * and calls it's {@link #solve() solve} method.
	 * @param args Array of command line arguments.<br>
	 * Use the <code><b>-h</b></code> argument to show the <i>usage</i> text listing all arguments.
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
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
				verbose = true;
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
		String solution = paintshop.solve();
		System.out.println(solution);
		if (verbose) {
			long end = System.currentTimeMillis();
			System.out.println("Time: " + (end - start) + " milliseconds");
		}
	}

	private static void usage() {
		System.out.println("Paint Shop Code Challenge - Johan Steyn");
		System.out.println("");
		System.out.println("Usage: java -jar paintshop.jar [-h] [-v] <file>");
		System.out.println("");
		System.out.println("Args:");
		System.out.println("  -h Help (shows this usage text)");
		System.out.println("  -v Verbose mode (shows the time taken)");
		System.out.println("  <file> Input file");
		System.out.println("");
	}
}
