package fr.berger.jexec;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class JExec {
	
	@NotNull
	public static String cmd(@NotNull String command, int timeout, @Nullable TimeUnit timeoutUnit, boolean verbose) throws CommandException, IOException, InterruptedException {
		StringBuilder content = new StringBuilder();
		String line;
		Process proc;
		BufferedReader br = null;
		
		try {
			proc = Runtime.getRuntime().exec(command);
			
			if (timeout > 0 && timeoutUnit != null)
				proc.waitFor(timeout, timeoutUnit);
			
			br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			while ((line = br.readLine()) != null) {
				content.append(line).append("\n");
				
				if (verbose)
					System.out.println(line);
			}
			
			// Get the error if "raw_content" is empty
			if (content.length() == 0) {
				br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				
				while ((line = br.readLine()) != null) {
					content.append(line).append("\n");
					
					if (verbose)
						System.out.println(line);
				}
				
				return content.toString();
				//throw new CommandException(content.toString());
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return content.toString();
	}
	@NotNull
	public static String cmd(@NotNull String command, int timeout, TimeUnit timeoutUnit) throws IOException, InterruptedException {
		return cmd(command, timeout, timeoutUnit, false);
	}
	@NotNull
	public static String cmd(@NotNull String command, boolean verbose) throws IOException, InterruptedException {
		return cmd(command, -1, null, verbose);
	}
	@NotNull
	public static String cmd(@NotNull String command) throws IOException, InterruptedException {
		return cmd(command, -1, null, false);
	}
}
