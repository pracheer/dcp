package branch.server;
import java.util.Vector;

/**
 * 
 * @author qsh2
 *
 * This provides a very limited command line parameter parsing facility.
 * The parses expects strict format for command line arguments.
 * To provide port number for example:
 * 		-port 300
 */

public class FlagParser {
	public static class FlagParseException extends Exception {
		public FlagParseException(String error) {
			super(error);
		}
	}

	public static class Argument {
		private String argName_;
		private String argValue_;

		public Argument(String name, String value) {
			argName_ = name;
			argValue_ = value;
		}

		public String getName() {
			return argName_;
		}

		public String getValue() {
			return argValue_;
		}
	}

	public Vector<FlagParser.Argument> parseFlags(String[] args) throws FlagParseException {
		Vector<FlagParser.Argument> parsedArguments = new Vector<FlagParser.Argument>();
		for (int i = 0; i < args.length; ++i) {
			String argName = args[i];

			if (!argName.startsWith("-")) {
				throw new FlagParseException(argName
						+ " is expected to start with '-'");
			}

			// Removing the '-' from the argument name.
			argName = argName.substring(1);

			if ((i + 1) >= args.length) {
				throw new FlagParseException(
						"Value not present for argument: "	+ argName);
			} else {
				parsedArguments.add(new FlagParser.Argument(argName, args[++i]));
			}
		}

		return parsedArguments;
	}
}
